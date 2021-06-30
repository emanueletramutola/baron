package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.UscrnMonthly;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class UscrnMonthlyDAO extends BaseObjDAO {
    private final String FIELDS = "sitename,wbanno,lst_yrmo,crx_vn_monthly,precise_longitude,precise_latitude,t_monthly_max,t_monthly_min,t_monthly_mean,t_monthly_avg,p_monthly_calc,solrad_monthly_avg,sur_temp_monthly_type,sur_temp_monthly_max,sur_temp_monthly_min,sur_temp_monthly_avg,lastmod,date_of_observation_lst";
    private final String TABLE_NAME = "monthly";

    public UscrnMonthlyDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new UscrnMonthly();
    }

    @Override
    protected Object build(ResultSet rs) {
        UscrnMonthly uscrnMonthly = new UscrnMonthly();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        uscrnMonthly.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
                        break;
                    case "sitename":
                        uscrnMonthly.setSitename(rs.getObject("sitename") != null ? rs.getString("sitename") : null);
                        break;
                    case "wbanno":
                        uscrnMonthly.setWbanno(rs.getObject("wbanno") != null ? rs.getInt("wbanno") : null);
                        break;
                    case "lst_yrmo":
                        uscrnMonthly.setLst_yrmo(rs.getObject("lst_yrmo") != null ? rs.getString("lst_yrmo") : null);
                        break;
                    case "crx_vn_monthly":
                        uscrnMonthly.setCrx_vn_monthly(rs.getObject("crx_vn_monthly") != null ? rs.getString("crx_vn_monthly") : null);
                        break;
                    case "precise_longitude":
                        uscrnMonthly.setPrecise_longitude(rs.getObject("precise_longitude") != null ? rs.getDouble("precise_longitude") : null);
                        break;
                    case "precise_latitude":
                        uscrnMonthly.setPrecise_latitude(rs.getObject("precise_latitude") != null ? rs.getDouble("precise_latitude") : null);
                        break;
                    case "t_monthly_max":
                        uscrnMonthly.setT_monthly_max(rs.getObject("t_monthly_max") != null ? rs.getDouble("t_monthly_max") : null);
                        break;
                    case "t_monthly_min":
                        uscrnMonthly.setT_monthly_min(rs.getObject("t_monthly_min") != null ? rs.getDouble("t_monthly_min") : null);
                        break;
                    case "t_monthly_mean":
                        uscrnMonthly.setT_monthly_mean(rs.getObject("t_monthly_mean") != null ? rs.getDouble("t_monthly_mean") : null);
                        break;
                    case "t_monthly_avg":
                        uscrnMonthly.setT_monthly_avg(rs.getObject("t_monthly_avg") != null ? rs.getDouble("t_monthly_avg") : null);
                        break;
                    case "p_monthly_calc":
                        uscrnMonthly.setP_monthly_calc(rs.getObject("p_monthly_calc") != null ? rs.getDouble("p_monthly_calc") : null);
                        break;
                    case "solrad_monthly_avg":
                        uscrnMonthly.setSolrad_monthly_avg(rs.getObject("solrad_monthly_avg") != null ? rs.getDouble("solrad_monthly_avg") : null);
                        break;
                    case "sur_temp_monthly_type":
                        uscrnMonthly.setSur_temp_monthly_type(rs.getObject("sur_temp_monthly_type") != null ? rs.getString("sur_temp_monthly_type") : null);
                        break;
                    case "sur_temp_monthly_max":
                        uscrnMonthly.setSur_temp_monthly_max(rs.getObject("sur_temp_monthly_max") != null ? rs.getDouble("sur_temp_monthly_max") : null);
                        break;
                    case "sur_temp_monthly_min":
                        uscrnMonthly.setSur_temp_monthly_min(rs.getObject("sur_temp_monthly_min") != null ? rs.getDouble("sur_temp_monthly_min") : null);
                        break;
                    case "sur_temp_monthly_avg":
                        uscrnMonthly.setSur_temp_monthly_avg(rs.getObject("sur_temp_monthly_avg") != null ? rs.getDouble("sur_temp_monthly_avg") : null);
                        break;
                    case "lastmod":
                        uscrnMonthly.setLastmod(rs.getObject("lastmod") != null ? rs.getTimestamp("lastmod").toInstant() : null);
                        break;
                    case "date_of_observation_lst":
                        uscrnMonthly.setDate_of_observation_lst(rs.getObject("date_of_observation_lst") != null ? rs.getTimestamp("date_of_observation_lst").toInstant() : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return uscrnMonthly;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;
        for (String key : params.keySet()) {
            switch (key) {
                case "id":
                case "wbanno":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "sitename":
                case "lst_yrmo":
                case "crx_vn_monthly":
                case "sur_temp_monthly_type":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "precise_longitude":
                case "precise_latitude":
                case "t_monthly_max":
                case "t_monthly_min":
                case "t_monthly_mean":
                case "t_monthly_avg":
                case "p_monthly_calc":
                case "solrad_monthly_avg":
                case "sur_temp_monthly_max":
                case "sur_temp_monthly_min":
                case "sur_temp_monthly_avg":
                    setParameter(pstmt, indexParams, params.get(key), Types.DOUBLE);
                    indexParams++;
                    break;
                case "lastmod":
                case "date_of_observation_lst":
                    setParameter(pstmt, indexParams, params.get(key), Types.TIMESTAMP_WITH_TIMEZONE);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        UscrnMonthly uscrnMonthly = (UscrnMonthly) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (uscrnMonthly.getSitename() != null) params.put("sitename", uscrnMonthly.getSitename());
        } else {
            params.put("sitename", uscrnMonthly.getSitename());
            params.put("wbanno", uscrnMonthly.getWbanno());
            params.put("lst_yrmo", uscrnMonthly.getLst_yrmo());
            params.put("crx_vn_monthly", uscrnMonthly.getCrx_vn_monthly());
            params.put("precise_longitude", uscrnMonthly.getPrecise_longitude());
            params.put("precise_latitude", uscrnMonthly.getPrecise_latitude());
            params.put("t_monthly_max", uscrnMonthly.getT_monthly_max());
            params.put("t_monthly_min", uscrnMonthly.getT_monthly_min());
            params.put("t_monthly_mean", uscrnMonthly.getT_monthly_mean());
            params.put("t_monthly_avg", uscrnMonthly.getT_monthly_avg());
            params.put("p_monthly_calc", uscrnMonthly.getP_monthly_calc());
            params.put("solrad_monthly_avg", uscrnMonthly.getSolrad_monthly_avg());
            params.put("sur_temp_monthly_type", uscrnMonthly.getSur_temp_monthly_type());
            params.put("sur_temp_monthly_max", uscrnMonthly.getSur_temp_monthly_max());
            params.put("sur_temp_monthly_min", uscrnMonthly.getSur_temp_monthly_min());
            params.put("sur_temp_monthly_avg", uscrnMonthly.getSur_temp_monthly_avg());
            params.put("lastmod", uscrnMonthly.getLastmod());
            params.put("date_of_observation_lst", uscrnMonthly.getDate_of_observation_lst());
        }

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
