package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.WmoMinPressureLevel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WmoMinPressureLevelDAO extends BaseObjDAO {
    private final String FIELDS = "id,sonde_id,sonde_code,sonde_name,ecv,isday,min_press";
    private final String TABLE_NAME = "wmo_min_pressure_level";

    public WmoMinPressureLevelDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new WmoMinPressureLevel();
    }

    @Override
    protected Object build(ResultSet rs) {
        WmoMinPressureLevel wmoMinPressureLevel = new WmoMinPressureLevel();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        wmoMinPressureLevel.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
                        break;
                    case "sonde_id":
                        wmoMinPressureLevel.setSondeId(rs.getObject("sonde_id") != null ? rs.getString("sonde_id") : null);
                        break;
                    case "sonde_code":
                        wmoMinPressureLevel.setSonde_code(rs.getObject("sonde_code") != null ? rs.getString("sonde_code") : null);
                        break;
                    case "sonde_name":
                        wmoMinPressureLevel.setSonde_name(rs.getObject("sonde_name") != null ? rs.getString("sonde_name") : null);
                        break;
                    case "ecv":
                        wmoMinPressureLevel.setEcv(rs.getObject("ecv") != null ? rs.getString("ecv") : null);
                        break;
                    case "isday":
                        wmoMinPressureLevel.setDay(rs.getObject("isday") != null ? rs.getBoolean("isday") : null);
                        break;
                    case "min_press":
                        wmoMinPressureLevel.setMin_press(rs.getObject("min_press") != null ? rs.getFloat("min_press") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return wmoMinPressureLevel;
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
                case "sonde_id":
                case "sonde_code":
                case "sonde_name":
                case "ecv":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "isday":
                    setParameter(pstmt, indexParams, params.get(key), Types.BOOLEAN);
                    indexParams++;
                    break;
                case "min_press":
                    setParameter(pstmt, indexParams, params.get(key), Types.REAL);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        WmoMinPressureLevel wmoMinPressureLevel = (WmoMinPressureLevel) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (wmoMinPressureLevel.getId() != null) params.put("id", wmoMinPressureLevel.getId());
        if (wmoMinPressureLevel.getSondeId() != null) params.put("sonde_id", wmoMinPressureLevel.getSondeId());
        if (wmoMinPressureLevel.getSonde_code() != null) params.put("sonde_code", wmoMinPressureLevel.getSonde_code());
        if (wmoMinPressureLevel.getSonde_name() != null) params.put("sonde_name", wmoMinPressureLevel.getSonde_name());
        if (wmoMinPressureLevel.getEcv() != null) params.put("ecv", wmoMinPressureLevel.getEcv());
        if (wmoMinPressureLevel.getDay() != null) params.put("isday", wmoMinPressureLevel.getDay());
        if (wmoMinPressureLevel.getMin_press() != null) params.put("min_press", wmoMinPressureLevel.getMin_press());

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
        WmoMinPressureLevel wmoMinPressureLevel = (WmoMinPressureLevel) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();
        if (wmoMinPressureLevel.getId() != null) updateSetStatementList.add("id = ?");
        if (wmoMinPressureLevel.getSondeId() != null) updateSetStatementList.add("sonde_id = ?");
        if (wmoMinPressureLevel.getSonde_code() != null) updateSetStatementList.add("sonde_code = ?");
        if (wmoMinPressureLevel.getSonde_name() != null) updateSetStatementList.add("sonde_name = ?");
        if (wmoMinPressureLevel.getEcv() != null) updateSetStatementList.add("ecv = ?");
        if (wmoMinPressureLevel.getDay() != null) updateSetStatementList.add("isday = ?");
        if (wmoMinPressureLevel.getMin_press() != null) updateSetStatementList.add("min_press = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
