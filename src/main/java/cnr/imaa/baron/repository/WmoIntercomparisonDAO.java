package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.WmoIntercomparison;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WmoIntercomparisonDAO extends BaseObjDAO {
    private final String FIELDS = "id,press,sonde_id,sonde_code,sonde_name,mean,std_dev,ecv,isday";
    private final String TABLE_NAME = "wmo_intercomparison_2010";

    public WmoIntercomparisonDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new WmoIntercomparison();
    }

    @Override
    protected Object build(ResultSet rs) {
        WmoIntercomparison wmoIntercomparison = new WmoIntercomparison();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        wmoIntercomparison.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
                        break;
                    case "press":
                        wmoIntercomparison.setPress(rs.getObject("press") != null ? rs.getFloat("press") : null);
                        break;
                    case "sonde_id":
                        wmoIntercomparison.setSondeId(rs.getObject("sonde_id") != null ? rs.getString("sonde_id") : null);
                        break;
                    case "sonde_code":
                        wmoIntercomparison.setSonde_code(rs.getObject("sonde_code") != null ? rs.getInt("sonde_code") : null);
                        break;
                    case "sonde_name":
                        wmoIntercomparison.setSonde_name(rs.getObject("sonde_name") != null ? rs.getString("sonde_name") : null);
                        break;
                    case "mean":
                        wmoIntercomparison.setMean(rs.getObject("mean") != null ? rs.getFloat("mean") : null);
                        break;
                    case "std_dev":
                        wmoIntercomparison.setStd_dev(rs.getObject("std_dev") != null ? rs.getFloat("std_dev") : null);
                        break;
                    case "ecv":
                        wmoIntercomparison.setEcv(rs.getObject("ecv") != null ? rs.getString("ecv") : null);
                        break;
                    case "isday":
                        wmoIntercomparison.setDay(rs.getObject("isday") != null ? rs.getBoolean("isday") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return wmoIntercomparison;
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
                case "press":
                case "mean":
                case "std_dev":
                    setParameter(pstmt, indexParams, params.get(key), Types.REAL);
                    indexParams++;
                    break;
                case "sonde_id":
                case "sonde_name":
                case "ecv":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "isday":
                    setParameter(pstmt, indexParams, params.get(key), Types.BOOLEAN);
                    indexParams++;
                    break;
                case "sonde_code":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        WmoIntercomparison wmoIntercomparison = (WmoIntercomparison) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (wmoIntercomparison.getId() != null) params.put("id", wmoIntercomparison.getId());
        if (wmoIntercomparison.getPress() != null) params.put("press", wmoIntercomparison.getPress());
        if (wmoIntercomparison.getSondeId() != null) params.put("sonde_id", wmoIntercomparison.getSondeId());
        if (wmoIntercomparison.getSonde_code() != null) params.put("sonde_code", wmoIntercomparison.getSonde_code());
        if (wmoIntercomparison.getSonde_name() != null) params.put("sonde_name", wmoIntercomparison.getSonde_name());
        if (wmoIntercomparison.getMean() != null) params.put("mean", wmoIntercomparison.getMean());
        if (wmoIntercomparison.getStd_dev() != null) params.put("std_dev", wmoIntercomparison.getStd_dev());
        if (wmoIntercomparison.getEcv() != null) params.put("ecv", wmoIntercomparison.getEcv());
        if (wmoIntercomparison.getDay() != null) params.put("isday", wmoIntercomparison.getDay());

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
        WmoIntercomparison wmoIntercomparison = (WmoIntercomparison) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();

        if (wmoIntercomparison.getId() != null) updateSetStatementList.add("id = ?");
        if (wmoIntercomparison.getPress() != null) updateSetStatementList.add("press = ?");
        if (wmoIntercomparison.getSondeId() != null) updateSetStatementList.add("sonde_id = ?");
        if (wmoIntercomparison.getSonde_code() != null) updateSetStatementList.add("sonde_code = ?");
        if (wmoIntercomparison.getSonde_name() != null) updateSetStatementList.add("sonde_name = ?");
        if (wmoIntercomparison.getMean() != null) updateSetStatementList.add("mean = ?");
        if (wmoIntercomparison.getStd_dev() != null) updateSetStatementList.add("std_dev = ?");
        if (wmoIntercomparison.getEcv() != null) updateSetStatementList.add("ecv = ?");
        if (wmoIntercomparison.getDay() != null) updateSetStatementList.add("isday = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
