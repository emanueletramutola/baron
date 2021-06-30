package cnr.imaa.baron.repository;

import cnr.imaa.baron.commons.BaronCommons;
import cnr.imaa.baron.model.BaseData;
import cnr.imaa.baron.model.BaseHeader;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cnr.imaa.baron.commons.BaronCommons.partitionByCores;
import static java.util.stream.Collectors.groupingBy;

public abstract class BaseHeaderDAO extends BaseObjBaronPartitionTableDAO {
    public BaseHeaderDAO(DataSource dataSource) {
        super(dataSource);
    }

    protected abstract BaseDataDAO getBaseDataDAO();

    @Override
    public void save(Object objToSave, SQL_COMMAND sqlCommand) {
        if (objToSave instanceof List) {
            saveList(objToSave, sqlCommand);
        } else {
            BaseHeader baseHeader = (BaseHeader) objToSave;
            Integer year = BaronCommons.getYear(baseHeader.getDateOfObservation());

            save(year, baseHeader);
        }
    }

    public void saveList(Object objToSave, SQL_COMMAND sqlCommand) {
        List<BaseHeader> baseHeaderList = (List<BaseHeader>) objToSave;
        Map<Integer, List<BaseHeader>> baseHeaderListPerYear = baseHeaderList
                .parallelStream()
                .collect(groupingBy(BaseHeader::getYear));

        Set<Integer> years = baseHeaderList
                .parallelStream()
                .map(BaseHeader::getYear)
                .collect(Collectors.toSet());

        years
                .stream()
                .forEach(year -> {
                    Collection<List<BaseHeader>> baseHeaderListPerYearToSave = partitionByCores(baseHeaderListPerYear.get(year));

                    baseHeaderListPerYearToSave
                            .stream()
                            .forEach(hl -> {
                                hl
                                        .parallelStream()
                                        .forEach(baseHeader -> {
                                            save(baseHeader, sqlCommand);
                                        });
                            });
                });
    }

    private void save(Integer year, BaseHeader baseHeader) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;

        Exception exception = null;

        try {
            conn = this.getDataSource().getConnection();
            conn.setAutoCommit(false);

            super.setYear(year);

            pstmt = conn.prepareStatement(getSQLInsert(), Statement.RETURN_GENERATED_KEYS);

            setParameters(pstmt, getParams(baseHeader, SQL_COMMAND.INSERT));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows <= 0) {
                conn.rollback();
            } else {
                ResultSet rs = pstmt.getGeneratedKeys();

                rs.next();

                baseHeader.setIdPK(rs.getObject(1));

                DataSource.close(rs);

                baseHeader.getData()
                        .parallelStream()
                        .forEach(data -> {
                            data.setIdFK(baseHeader.getIdPK());
                        });

                pstmt2 = conn.prepareStatement(getBaseDataDAO().getSQLInsert());

                for (BaseData baseData : baseHeader.getData()) {
                    getBaseDataDAO().setParameters(pstmt2, getBaseDataDAO().getParams(baseData, SQL_COMMAND.INSERT));

                    pstmt2.addBatch();
                }

                pstmt2.executeBatch();

                conn.commit();

            }
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
            DataSource.close(pstmt2);
            DataSource.close(conn);
        }

        if (exception != null)
            throw new RuntimeException(exception);
    }

    public Object getData(BaseHeader baseHeader) {
        return getBaseDataDAO().getByFk(baseHeader, baseHeader.getYear());
    }
}
