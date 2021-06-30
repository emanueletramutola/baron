package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.WindRs92Corr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WindRs92CorrDAO extends BaseObjDAO {
    private final String FIELDS = "id,pressure,cor_u,u_cor_u,cor_v,u_cor_v,isday";
    private final String TABLE_NAME = "wind_rs92_corr";

    public WindRs92CorrDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new WindRs92Corr();
    }

    @Override
    protected Object build(ResultSet rs) {
        WindRs92Corr windRs92Corr = new WindRs92Corr();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        windRs92Corr.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
                        break;
                    case "pressure":
                        windRs92Corr.setPressure(rs.getObject("pressure") != null ? rs.getFloat("pressure") : null);
                        break;
                    case "cor_u":
                        windRs92Corr.setCor_u(rs.getObject("cor_u") != null ? rs.getFloat("cor_u") : null);
                        break;
                    case "u_cor_u":
                        windRs92Corr.setU_cor_u(rs.getObject("u_cor_u") != null ? rs.getFloat("u_cor_u") : null);
                        break;
                    case "cor_v":
                        windRs92Corr.setCor_v(rs.getObject("cor_v") != null ? rs.getFloat("cor_v") : null);
                        break;
                    case "u_cor_v":
                        windRs92Corr.setU_cor_v(rs.getObject("u_cor_v") != null ? rs.getFloat("u_cor_v") : null);
                        break;
                    case "isday":
                        windRs92Corr.setDay(rs.getObject("isday") != null ? rs.getBoolean("isday") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return windRs92Corr;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "id":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "pressure":
                case "cor_u":
                case "u_cor_u":
                case "cor_v":
                case "u_cor_v":
                    setParameter(pstmt, indexParams, params.get(key), Types.REAL);
                    indexParams++;
                    break;
                case "isday":
                    setParameter(pstmt, indexParams, params.get(key), Types.BOOLEAN);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        WindRs92Corr windRs92Corr = (WindRs92Corr) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (windRs92Corr.getId() != null) params.put("id", windRs92Corr.getId());
        if (windRs92Corr.getPressure() != null) params.put("pressure", windRs92Corr.getPressure());
        if (windRs92Corr.getCor_u() != null) params.put("cor_u", windRs92Corr.getCor_u());
        if (windRs92Corr.getU_cor_u() != null) params.put("u_cor_u", windRs92Corr.getU_cor_u());
        if (windRs92Corr.getCor_v() != null) params.put("cor_v", windRs92Corr.getCor_v());
        if (windRs92Corr.getU_cor_v() != null) params.put("u_cor_v", windRs92Corr.getU_cor_v());
        if (windRs92Corr.getDay() != null) params.put("isday", windRs92Corr.getDay());

        return params;
    }

    @Override
    protected String getFields() {
        return FIELDS;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getUpdateSetStatement(Object obj) {
        WindRs92Corr windRs92Corr = (WindRs92Corr) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();
        if (windRs92Corr.getId() != null) updateSetStatementList.add("id = ?");
        if (windRs92Corr.getPressure() != null) updateSetStatementList.add("pressure = ?");
        if (windRs92Corr.getCor_u() != null) updateSetStatementList.add("cor_u = ?");
        if (windRs92Corr.getU_cor_u() != null) updateSetStatementList.add("u_cor_u = ?");
        if (windRs92Corr.getCor_v() != null) updateSetStatementList.add("cor_v = ?");
        if (windRs92Corr.getU_cor_v() != null) updateSetStatementList.add("u_cor_v = ?");
        if (windRs92Corr.getDay() != null) updateSetStatementList.add("isday = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
