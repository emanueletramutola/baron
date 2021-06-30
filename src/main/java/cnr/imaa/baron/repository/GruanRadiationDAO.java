package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.GruanRadiation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class GruanRadiationDAO extends BaseObjDAO {
    private final String FIELDS = "id,clear,cloudy,quota,sza";
    private final String TABLE_NAME = "gruan_radiation";

    public GruanRadiationDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new GruanRadiation();
    }

    @Override
    protected Object build(ResultSet rs) {
        GruanRadiation gruanRadiation = new GruanRadiation();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        gruanRadiation.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
                        break;
                    case "clear":
                        gruanRadiation.setClear(rs.getObject("clear") != null ? rs.getDouble("clear") : null);
                        break;
                    case "cloudy":
                        gruanRadiation.setCloudy(rs.getObject("cloudy") != null ? rs.getDouble("cloudy") : null);
                        break;
                    case "quota":
                        gruanRadiation.setQuota(rs.getObject("quota") != null ? rs.getDouble("quota") : null);
                        break;
                    case "sza":
                        gruanRadiation.setSza(rs.getObject("sza") != null ? rs.getDouble("sza") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return gruanRadiation;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "id":
                    setParameter(pstmt, indexParams, params.get(key), Types.BIGINT);
                    indexParams++;
                    break;
                case "clear":
                case "cloudy":
                case "quota":
                case "sza":
                    setParameter(pstmt, indexParams, params.get(key), Types.DOUBLE);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        GruanRadiation gruanRadiation = (GruanRadiation) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (gruanRadiation.getId() != null) params.put("id", gruanRadiation.getId());
        if (gruanRadiation.getClear() != null) params.put("clear", gruanRadiation.getClear());
        if (gruanRadiation.getCloudy() != null) params.put("cloudy", gruanRadiation.getCloudy());
        if (gruanRadiation.getQuota() != null) params.put("quota", gruanRadiation.getQuota());
        if (gruanRadiation.getSza() != null) params.put("sza", gruanRadiation.getSza());

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
        //TODO
        return null;
    }
}
