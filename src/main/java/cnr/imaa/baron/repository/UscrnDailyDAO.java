package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.UscrnDaily;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class UscrnDailyDAO extends BaseObjDAO {
    private final String FIELDS = "sitename,wbanno,lst_date,crx_vn,longitude,latitude,t_daily_max,t_daily_min,t_daily_mean,t_daily_avg,p_daily_calc,solarad_daily,sur_temp_daily_type,sur_temp_daily_max,sur_temp_daily_min,sur_temp_daily_avg,rh_daily_max,rh_daily_min,rh_daily_avg,soil_moisture_5_daily,soil_moisture_10_daily,soil_moisture_20_daily,soil_moisture_50_daily,soil_moisture_100_daily,soil_temp_5_daily,soil_temp_10_daily,soil_temp_20_daily,soil_temp_50_daily,soil_temp_100_daily,lastmod,date_of_observation_lst";
    private final String TABLE_NAME = "daily";

    public UscrnDailyDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new UscrnDaily();
    }

    @Override
    protected Object build(ResultSet rs) {
        UscrnDaily uscrnDaily = new UscrnDaily();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "sitename":
                        uscrnDaily.setSitename(rs.getObject("sitename") != null ? rs.getString("sitename") : null);
                        break;
                    case "wbanno":
                        uscrnDaily.setWbanno(rs.getObject("wbanno") != null ? rs.getInt("wbanno") : null);
                        break;
                    case "lst_date":
                        uscrnDaily.setLst_date(rs.getObject("lst_date") != null ? rs.getString("lst_date") : null);
                        break;
                    case "crx_vn":
                        uscrnDaily.setCrx_vn(rs.getObject("crx_vn") != null ? rs.getString("crx_vn") : null);
                        break;
                    case "longitude":
                        uscrnDaily.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
                        break;
                    case "latitude":
                        uscrnDaily.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                        break;
                    case "t_daily_max":
                        uscrnDaily.setT_daily_max(rs.getObject("t_daily_max") != null ? rs.getDouble("t_daily_max") : null);
                        break;
                    case "t_daily_min":
                        uscrnDaily.setT_daily_min(rs.getObject("t_daily_min") != null ? rs.getDouble("t_daily_min") : null);
                        break;
                    case "t_daily_mean":
                        uscrnDaily.setT_daily_mean(rs.getObject("t_daily_mean") != null ? rs.getDouble("t_daily_mean") : null);
                        break;
                    case "t_daily_avg":
                        uscrnDaily.setT_daily_avg(rs.getObject("t_daily_avg") != null ? rs.getDouble("t_daily_avg") : null);
                        break;
                    case "p_daily_calc":
                        uscrnDaily.setP_daily_calc(rs.getObject("p_daily_calc") != null ? rs.getDouble("p_daily_calc") : null);
                        break;
                    case "solarad_daily":
                        uscrnDaily.setSolarad_daily(rs.getObject("solarad_daily") != null ? rs.getDouble("solarad_daily") : null);
                        break;
                    case "sur_temp_daily_type":
                        uscrnDaily.setSur_temp_daily_type(rs.getObject("sur_temp_daily_type") != null ? rs.getString("sur_temp_daily_type") : null);
                        break;
                    case "sur_temp_daily_max":
                        uscrnDaily.setSur_temp_daily_max(rs.getObject("sur_temp_daily_max") != null ? rs.getDouble("sur_temp_daily_max") : null);
                        break;
                    case "sur_temp_daily_min":
                        uscrnDaily.setSur_temp_daily_min(rs.getObject("sur_temp_daily_min") != null ? rs.getDouble("sur_temp_daily_min") : null);
                        break;
                    case "sur_temp_daily_avg":
                        uscrnDaily.setSur_temp_daily_avg(rs.getObject("sur_temp_daily_avg") != null ? rs.getDouble("sur_temp_daily_avg") : null);
                        break;
                    case "rh_daily_max":
                        uscrnDaily.setRh_daily_max(rs.getObject("rh_daily_max") != null ? rs.getDouble("rh_daily_max") : null);
                        break;
                    case "rh_daily_min":
                        uscrnDaily.setRh_daily_min(rs.getObject("rh_daily_min") != null ? rs.getDouble("rh_daily_min") : null);
                        break;
                    case "rh_daily_avg":
                        uscrnDaily.setRh_daily_avg(rs.getObject("rh_daily_avg") != null ? rs.getDouble("rh_daily_avg") : null);
                        break;
                    case "soil_moisture_5_daily":
                        uscrnDaily.setSoil_moisture_5_daily(rs.getObject("soil_moisture_5_daily") != null ? rs.getDouble("soil_moisture_5_daily") : null);
                        break;
                    case "soil_moisture_10_daily":
                        uscrnDaily.setSoil_moisture_10_daily(rs.getObject("soil_moisture_10_daily") != null ? rs.getDouble("soil_moisture_10_daily") : null);
                        break;
                    case "soil_moisture_20_daily":
                        uscrnDaily.setSoil_moisture_20_daily(rs.getObject("soil_moisture_20_daily") != null ? rs.getDouble("soil_moisture_20_daily") : null);
                        break;
                    case "soil_moisture_50_daily":
                        uscrnDaily.setSoil_moisture_50_daily(rs.getObject("soil_moisture_50_daily") != null ? rs.getDouble("soil_moisture_50_daily") : null);
                        break;
                    case "soil_moisture_100_daily":
                        uscrnDaily.setSoil_moisture_100_daily(rs.getObject("soil_moisture_100_daily") != null ? rs.getDouble("soil_moisture_100_daily") : null);
                        break;
                    case "soil_temp_5_daily":
                        uscrnDaily.setSoil_temp_5_daily(rs.getObject("soil_temp_5_daily") != null ? rs.getDouble("soil_temp_5_daily") : null);
                        break;
                    case "soil_temp_10_daily":
                        uscrnDaily.setSoil_temp_10_daily(rs.getObject("soil_temp_10_daily") != null ? rs.getDouble("soil_temp_10_daily") : null);
                        break;
                    case "soil_temp_20_daily":
                        uscrnDaily.setSoil_temp_20_daily(rs.getObject("soil_temp_20_daily") != null ? rs.getDouble("soil_temp_20_daily") : null);
                        break;
                    case "soil_temp_50_daily":
                        uscrnDaily.setSoil_temp_50_daily(rs.getObject("soil_temp_50_daily") != null ? rs.getDouble("soil_temp_50_daily") : null);
                        break;
                    case "soil_temp_100_daily":
                        uscrnDaily.setSoil_temp_100_daily(rs.getObject("soil_temp_100_daily") != null ? rs.getDouble("soil_temp_100_daily") : null);
                        break;
                    case "lastmod":
                        uscrnDaily.setLastmod(rs.getObject("lastmod") != null ? rs.getTimestamp("lastmod").toInstant() : null);
                        break;
                    case "date_of_observation_lst":
                        uscrnDaily.setDate_of_observation_lst(rs.getObject("date_of_observation_lst") != null ? rs.getTimestamp("date_of_observation_lst").toInstant() : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return uscrnDaily;
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
                case "lst_date":
                case "crx_vn":
                case "sur_temp_daily_type":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "longitude":
                case "latitude":
                case "t_daily_max":
                case "t_daily_min":
                case "t_daily_mean":
                case "t_daily_avg":
                case "p_daily_calc":
                case "solarad_daily":
                case "sur_temp_daily_max":
                case "sur_temp_daily_min":
                case "sur_temp_daily_avg":
                case "rh_daily_max":
                case "rh_daily_min":
                case "rh_daily_avg":
                case "soil_moisture_5_daily":
                case "soil_moisture_10_daily":
                case "soil_moisture_20_daily":
                case "soil_moisture_50_daily":
                case "soil_moisture_100_daily":
                case "soil_temp_5_daily":
                case "soil_temp_10_daily":
                case "soil_temp_20_daily":
                case "soil_temp_50_daily":
                case "soil_temp_100_daily":
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
        UscrnDaily uscrnDaily = (UscrnDaily) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (uscrnDaily.getSitename() != null) params.put("sitename", uscrnDaily.getSitename());
        } else {
            params.put("sitename", uscrnDaily.getSitename());
            params.put("wbanno", uscrnDaily.getWbanno());
            params.put("lst_date", uscrnDaily.getLst_date());
            params.put("crx_vn", uscrnDaily.getCrx_vn());
            params.put("longitude", uscrnDaily.getLongitude());
            params.put("latitude", uscrnDaily.getLatitude());
            params.put("t_daily_max", uscrnDaily.getT_daily_max());
            params.put("t_daily_min", uscrnDaily.getT_daily_min());
            params.put("t_daily_mean", uscrnDaily.getT_daily_mean());
            params.put("t_daily_avg", uscrnDaily.getT_daily_avg());
            params.put("p_daily_calc", uscrnDaily.getP_daily_calc());
            params.put("solarad_daily", uscrnDaily.getSolarad_daily());
            params.put("sur_temp_daily_type", uscrnDaily.getSur_temp_daily_type());
            params.put("sur_temp_daily_max", uscrnDaily.getSur_temp_daily_max());
            params.put("sur_temp_daily_min", uscrnDaily.getSur_temp_daily_min());
            params.put("sur_temp_daily_avg", uscrnDaily.getSur_temp_daily_avg());
            params.put("rh_daily_max", uscrnDaily.getRh_daily_max());
            params.put("rh_daily_min", uscrnDaily.getRh_daily_min());
            params.put("rh_daily_avg", uscrnDaily.getRh_daily_avg());
            params.put("soil_moisture_5_daily", uscrnDaily.getSoil_moisture_5_daily());
            params.put("soil_moisture_10_daily", uscrnDaily.getSoil_moisture_10_daily());
            params.put("soil_moisture_20_daily", uscrnDaily.getSoil_moisture_20_daily());
            params.put("soil_moisture_50_daily", uscrnDaily.getSoil_moisture_50_daily());
            params.put("soil_moisture_100_daily", uscrnDaily.getSoil_moisture_100_daily());
            params.put("soil_temp_5_daily", uscrnDaily.getSoil_temp_5_daily());
            params.put("soil_temp_10_daily", uscrnDaily.getSoil_temp_10_daily());
            params.put("soil_temp_20_daily", uscrnDaily.getSoil_temp_20_daily());
            params.put("soil_temp_50_daily", uscrnDaily.getSoil_temp_50_daily());
            params.put("soil_temp_100_daily", uscrnDaily.getSoil_temp_100_daily());
            params.put("lastmod", uscrnDaily.getLastmod());
            params.put("date_of_observation_lst", uscrnDaily.getDate_of_observation_lst());
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
