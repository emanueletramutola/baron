package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.UscrnHourly;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class UscrnHourlyDAO extends BaseObjDAO {
    private final String FIELDS = "sitename,wbanno,utc_date,utc_time,lst_date,lst_time,crx_vn,longitude,latitude,t_calc,t_hr_avg,t_max,t_min,p_calc,solarad,solarad_flag,solarad_max,solarad_max_flag,solarad_min,solarad_min_flag,sur_temp_type,sur_temp,sur_temp_flag,sur_temp_max,sur_temp_max_flag,sur_temp_min,sur_temp_min_flag,rh_hr_avg,rh_hr_avg_flag,soil_moisture_5,soil_moisture_10,soil_moisture_20,soil_moisture_50,soil_moisture_100,soil_temp_5,soil_temp_10,soil_temp_20,soil_temp_50,soil_temp_100,lastmod,date_of_observation_utc,date_of_observation_lst";
    private final String TABLE_NAME = "hourly";

    public UscrnHourlyDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new UscrnHourly();
    }

    @Override
    protected Object build(ResultSet rs) {
        UscrnHourly uscrnHourly = new UscrnHourly();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        uscrnHourly.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
                        break;
                    case "sitename":
                        uscrnHourly.setSitename(rs.getObject("sitename") != null ? rs.getString("sitename") : null);
                        break;
                    case "wbanno":
                        uscrnHourly.setWbanno(rs.getObject("wbanno") != null ? rs.getInt("wbanno") : null);
                        break;
                    case "utc_date":
                        uscrnHourly.setUtc_date(rs.getObject("utc_date") != null ? rs.getString("utc_date") : null);
                        break;
                    case "utc_time":
                        uscrnHourly.setUtc_time(rs.getObject("utc_time") != null ? rs.getString("utc_time") : null);
                        break;
                    case "lst_date":
                        uscrnHourly.setLst_date(rs.getObject("lst_date") != null ? rs.getString("lst_date") : null);
                        break;
                    case "lst_time":
                        uscrnHourly.setLst_time(rs.getObject("lst_time") != null ? rs.getString("lst_time") : null);
                        break;
                    case "crx_vn":
                        uscrnHourly.setCrx_vn(rs.getObject("crx_vn") != null ? rs.getString("crx_vn") : null);
                        break;
                    case "longitude":
                        uscrnHourly.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
                        break;
                    case "latitude":
                        uscrnHourly.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                        break;
                    case "t_calc":
                        uscrnHourly.setT_calc(rs.getObject("t_calc") != null ? rs.getDouble("t_calc") : null);
                        break;
                    case "t_hr_avg":
                        uscrnHourly.setT_hr_avg(rs.getObject("t_hr_avg") != null ? rs.getDouble("t_hr_avg") : null);
                        break;
                    case "t_max":
                        uscrnHourly.setT_max(rs.getObject("t_max") != null ? rs.getDouble("t_max") : null);
                        break;
                    case "t_min":
                        uscrnHourly.setT_min(rs.getObject("t_min") != null ? rs.getDouble("t_min") : null);
                        break;
                    case "p_calc":
                        uscrnHourly.setP_calc(rs.getObject("p_calc") != null ? rs.getDouble("p_calc") : null);
                        break;
                    case "solarad":
                        uscrnHourly.setSolarad(rs.getObject("solarad") != null ? rs.getDouble("solarad") : null);
                        break;
                    case "solarad_flag":
                        uscrnHourly.setSolarad_flag(rs.getObject("solarad_flag") != null ? rs.getString("solarad_flag") : null);
                        break;
                    case "solarad_max":
                        uscrnHourly.setSolarad_max(rs.getObject("solarad_max") != null ? rs.getDouble("solarad_max") : null);
                        break;
                    case "solarad_max_flag":
                        uscrnHourly.setSolarad_max_flag(rs.getObject("solarad_max_flag") != null ? rs.getString("solarad_max_flag") : null);
                        break;
                    case "solarad_min":
                        uscrnHourly.setSolarad_min(rs.getObject("solarad_min") != null ? rs.getDouble("solarad_min") : null);
                        break;
                    case "solarad_min_flag":
                        uscrnHourly.setSolarad_min_flag(rs.getObject("solarad_min_flag") != null ? rs.getString("solarad_min_flag") : null);
                        break;
                    case "sur_temp_type":
                        uscrnHourly.setSur_temp_type(rs.getObject("sur_temp_type") != null ? rs.getString("sur_temp_type") : null);
                        break;
                    case "sur_temp":
                        uscrnHourly.setSur_temp(rs.getObject("sur_temp") != null ? rs.getDouble("sur_temp") : null);
                        break;
                    case "sur_temp_flag":
                        uscrnHourly.setSur_temp_flag(rs.getObject("sur_temp_flag") != null ? rs.getString("sur_temp_flag") : null);
                        break;
                    case "sur_temp_max":
                        uscrnHourly.setSur_temp_max(rs.getObject("sur_temp_max") != null ? rs.getDouble("sur_temp_max") : null);
                        break;
                    case "sur_temp_max_flag":
                        uscrnHourly.setSur_temp_max_flag(rs.getObject("sur_temp_max_flag") != null ? rs.getString("sur_temp_max_flag") : null);
                        break;
                    case "sur_temp_min":
                        uscrnHourly.setSur_temp_min(rs.getObject("sur_temp_min") != null ? rs.getDouble("sur_temp_min") : null);
                        break;
                    case "sur_temp_min_flag":
                        uscrnHourly.setSur_temp_min_flag(rs.getObject("sur_temp_min_flag") != null ? rs.getString("sur_temp_min_flag") : null);
                        break;
                    case "rh_hr_avg":
                        uscrnHourly.setRh_hr_avg(rs.getObject("rh_hr_avg") != null ? rs.getDouble("rh_hr_avg") : null);
                        break;
                    case "rh_hr_avg_flag":
                        uscrnHourly.setRh_hr_avg_flag(rs.getObject("rh_hr_avg_flag") != null ? rs.getString("rh_hr_avg_flag") : null);
                        break;
                    case "soil_moisture_5":
                        uscrnHourly.setSoil_moisture_5(rs.getObject("soil_moisture_5") != null ? rs.getDouble("soil_moisture_5") : null);
                        break;
                    case "soil_moisture_10":
                        uscrnHourly.setSoil_moisture_10(rs.getObject("soil_moisture_10") != null ? rs.getDouble("soil_moisture_10") : null);
                        break;
                    case "soil_moisture_20":
                        uscrnHourly.setSoil_moisture_20(rs.getObject("soil_moisture_20") != null ? rs.getDouble("soil_moisture_20") : null);
                        break;
                    case "soil_moisture_50":
                        uscrnHourly.setSoil_moisture_50(rs.getObject("soil_moisture_50") != null ? rs.getDouble("soil_moisture_50") : null);
                        break;
                    case "soil_moisture_100":
                        uscrnHourly.setSoil_moisture_100(rs.getObject("soil_moisture_100") != null ? rs.getDouble("soil_moisture_100") : null);
                        break;
                    case "soil_temp_5":
                        uscrnHourly.setSoil_temp_5(rs.getObject("soil_temp_5") != null ? rs.getDouble("soil_temp_5") : null);
                        break;
                    case "soil_temp_10":
                        uscrnHourly.setSoil_temp_10(rs.getObject("soil_temp_10") != null ? rs.getDouble("soil_temp_10") : null);
                        break;
                    case "soil_temp_20":
                        uscrnHourly.setSoil_temp_20(rs.getObject("soil_temp_20") != null ? rs.getDouble("soil_temp_20") : null);
                        break;
                    case "soil_temp_50":
                        uscrnHourly.setSoil_temp_50(rs.getObject("soil_temp_50") != null ? rs.getDouble("soil_temp_50") : null);
                        break;
                    case "soil_temp_100":
                        uscrnHourly.setSoil_temp_100(rs.getObject("soil_temp_100") != null ? rs.getDouble("soil_temp_100") : null);
                        break;
                    case "lastmod":
                        uscrnHourly.setLastmod(rs.getObject("lastmod") != null ? rs.getTimestamp("lastmod").toInstant() : null);
                        break;
                    case "date_of_observation_utc":
                        uscrnHourly.setDate_of_observation_utc(rs.getObject("date_of_observation_utc") != null ? rs.getTimestamp("date_of_observation_utc").toInstant() : null);
                        break;
                    case "date_of_observation_lst":
                        uscrnHourly.setDate_of_observation_lst(rs.getObject("date_of_observation_lst") != null ? rs.getTimestamp("date_of_observation_lst").toInstant() : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return uscrnHourly;
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
                case "utc_date":
                case "utc_time":
                case "lst_date":
                case "lst_time":
                case "crx_vn":
                case "solarad_flag":
                case "solarad_max_flag":
                case "solarad_min_flag":
                case "sur_temp_type":
                case "sur_temp_flag":
                case "sur_temp_max_flag":
                case "sur_temp_min_flag":
                case "rh_hr_avg_flag":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "longitude":
                case "latitude":
                case "t_calc":
                case "t_hr_avg":
                case "t_max":
                case "t_min":
                case "p_calc":
                case "solarad":
                case "solarad_max":
                case "solarad_min":
                case "sur_temp":
                case "sur_temp_max":
                case "sur_temp_min":
                case "rh_hr_avg":
                case "soil_moisture_5":
                case "soil_moisture_10":
                case "soil_moisture_20":
                case "soil_moisture_50":
                case "soil_moisture_100":
                case "soil_temp_5":
                case "soil_temp_10":
                case "soil_temp_20":
                case "soil_temp_50":
                case "soil_temp_100":
                    setParameter(pstmt, indexParams, params.get(key), Types.DOUBLE);
                    indexParams++;
                    break;
                case "lastmod":
                case "date_of_observation_utc":
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
        UscrnHourly uscrnHourly = (UscrnHourly) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (uscrnHourly.getSitename() != null) params.put("sitename", uscrnHourly.getSitename());
        } else {
            params.put("sitename", uscrnHourly.getSitename());
            params.put("wbanno", uscrnHourly.getWbanno());
            params.put("utc_date", uscrnHourly.getUtc_date());
            params.put("utc_time", uscrnHourly.getUtc_time());
            params.put("lst_date", uscrnHourly.getLst_date());
            params.put("lst_time", uscrnHourly.getLst_time());
            params.put("crx_vn", uscrnHourly.getCrx_vn());
            params.put("longitude", uscrnHourly.getLongitude());
            params.put("latitude", uscrnHourly.getLatitude());
            params.put("t_calc", uscrnHourly.getT_calc());
            params.put("t_hr_avg", uscrnHourly.getT_hr_avg());
            params.put("t_max", uscrnHourly.getT_max());
            params.put("t_min", uscrnHourly.getT_min());
            params.put("p_calc", uscrnHourly.getP_calc());
            params.put("solarad", uscrnHourly.getSolarad());
            params.put("solarad_flag", uscrnHourly.getSolarad_flag());
            params.put("solarad_max", uscrnHourly.getSolarad_max());
            params.put("solarad_max_flag", uscrnHourly.getSolarad_max_flag());
            params.put("solarad_min", uscrnHourly.getSolarad_min());
            params.put("solarad_min_flag", uscrnHourly.getSolarad_min_flag());
            params.put("sur_temp_type", uscrnHourly.getSur_temp_type());
            params.put("sur_temp", uscrnHourly.getSur_temp());
            params.put("sur_temp_flag", uscrnHourly.getSur_temp_flag());
            params.put("sur_temp_max", uscrnHourly.getSur_temp_max());
            params.put("sur_temp_max_flag", uscrnHourly.getSur_temp_max_flag());
            params.put("sur_temp_min", uscrnHourly.getSur_temp_min());
            params.put("sur_temp_min_flag", uscrnHourly.getSur_temp_min_flag());
            params.put("rh_hr_avg", uscrnHourly.getRh_hr_avg());
            params.put("rh_hr_avg_flag", uscrnHourly.getRh_hr_avg_flag());
            params.put("soil_moisture_5", uscrnHourly.getSoil_moisture_5());
            params.put("soil_moisture_10", uscrnHourly.getSoil_moisture_10());
            params.put("soil_moisture_20", uscrnHourly.getSoil_moisture_20());
            params.put("soil_moisture_50", uscrnHourly.getSoil_moisture_50());
            params.put("soil_moisture_100", uscrnHourly.getSoil_moisture_100());
            params.put("soil_temp_5", uscrnHourly.getSoil_temp_5());
            params.put("soil_temp_10", uscrnHourly.getSoil_temp_10());
            params.put("soil_temp_20", uscrnHourly.getSoil_temp_20());
            params.put("soil_temp_50", uscrnHourly.getSoil_temp_50());
            params.put("soil_temp_100", uscrnHourly.getSoil_temp_100());
            params.put("lastmod", uscrnHourly.getLastmod());
            params.put("date_of_observation_utc", uscrnHourly.getDate_of_observation_utc());
            params.put("date_of_observation_lst", uscrnHourly.getDate_of_observation_lst());
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
