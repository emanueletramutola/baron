package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseData;
import cnr.imaa.baron.model.BaseHeader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public abstract class BaseDataDAO extends BaseObjBaronPartitionTableDAO {
    public BaseDataDAO(DataSource dataSource) {
        super(dataSource);
    }

    protected void saveInTransaction(Connection conn, Integer year, Object obj) {
        PreparedStatement pstmt = null;

        Exception exception = null;

        try {
            super.setYear(year);
            pstmt = conn.prepareStatement(getSQLInsert());

            for (BaseData baseData : (Set<BaseData>) obj) {
                setParameters(pstmt, getParams(baseData, SQL_COMMAND.INSERT));

                pstmt.addBatch();
            }

            pstmt.executeBatch();

            conn.commit();
        } catch (Exception ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException exx) {
                    throw new RuntimeException(exx);
                }
            }

            exception = ex;
        } finally {
            DataSource.close(pstmt);
            DataSource.close(conn);
        }

        if (exception != null)
            throw new RuntimeException(exception);
    }

    public Object getByFk(BaseHeader baseHeader, int year){
        BaseData baseObjFilter = (BaseData) getBaseObjFilter();
        baseObjFilter.setHeader(baseHeader);
        baseObjFilter.setIdFK(baseHeader.getIdPK());

        super.setYear(year);

        return get(getParams(baseObjFilter, SQL_COMMAND.SELECT));
    }
}
