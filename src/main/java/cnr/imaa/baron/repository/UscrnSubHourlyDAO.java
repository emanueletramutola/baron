package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.UscrnSubHourly;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class UscrnSubHourlyDAO extends BaseObjDAO {
    private final String FIELDS = "sitename,lastmod,wbanno,utc_date,utc_time,lst_date,lst_time,crx_vn,longitude,latitude,air_temperature,precipitation,solar_radiation,sr_flag,surface_temperature,st_type,st_flag,relative_humidity,rh_flag,soil_moisture_5,soil_temperature_5,wetness,wet_flag,wind_1_5,wind_flag,date_of_observation_utc,date_of_observation_lst";
    private final String TABLE_NAME = "subhourly";

    public UscrnSubHourlyDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new UscrnSubHourly();
    }

    @Override
    protected Object build(ResultSet rs) {
        UscrnSubHourly uscrnSubHourly = new UscrnSubHourly();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        uscrnSubHourly.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
                        break;
                    case "sitename":
                        uscrnSubHourly.setSitename(rs.getObject("sitename") != null ? rs.getString("sitename") : null);
                        break;
                    case "lastmod":
                        uscrnSubHourly.setLastmod(rs.getObject("lastmod") != null ? rs.getTimestamp("lastmod").toInstant() : null);
                        break;
                    case "wbanno":
                        uscrnSubHourly.setWbanno(rs.getObject("wbanno") != null ? rs.getInt("wbanno") : null);
                        break;
                    case "utc_date":
                        uscrnSubHourly.setUtc_date(rs.getObject("utc_date") != null ? rs.getString("utc_date") : null);
                        break;
                    case "utc_time":
                        uscrnSubHourly.setUtc_time(rs.getObject("utc_time") != null ? rs.getString("utc_time") : null);
                        break;
                    case "lst_date":
                        uscrnSubHourly.setLst_date(rs.getObject("lst_date") != null ? rs.getString("lst_date") : null);
                        break;
                    case "lst_time":
                        uscrnSubHourly.setLst_time(rs.getObject("lst_time") != null ? rs.getString("lst_time") : null);
                        break;
                    case "crx_vn":
                        uscrnSubHourly.setCrx_vn(rs.getObject("crx_vn") != null ? rs.getString("crx_vn") : null);
                        break;
                    case "longitude":
                        uscrnSubHourly.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
                        break;
                    case "latitude":
                        uscrnSubHourly.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                        break;
                    case "air_temperature":
                        uscrnSubHourly.setAir_temperature(rs.getObject("air_temperature") != null ? rs.getDouble("air_temperature") : null);
                        break;
                    case "precipitation":
                        uscrnSubHourly.setPrecipitation(rs.getObject("precipitation") != null ? rs.getDouble("precipitation") : null);
                        break;
                    case "solar_radiation":
                        uscrnSubHourly.setSolar_radiation(rs.getObject("solar_radiation") != null ? rs.getDouble("solar_radiation") : null);
                        break;
                    case "sr_flag":
                        uscrnSubHourly.setSr_flag(rs.getObject("sr_flag") != null ? rs.getString("sr_flag") : null);
                        break;
                    case "surface_temperature":
                        uscrnSubHourly.setSurface_temperature(rs.getObject("surface_temperature") != null ? rs.getDouble("surface_temperature") : null);
                        break;
                    case "st_type":
                        uscrnSubHourly.setSt_type(rs.getObject("st_type") != null ? rs.getString("st_type") : null);
                        break;
                    case "st_flag":
                        uscrnSubHourly.setSt_flag(rs.getObject("st_flag") != null ? rs.getString("st_flag") : null);
                        break;
                    case "relative_humidity":
                        uscrnSubHourly.setRelative_humidity(rs.getObject("relative_humidity") != null ? rs.getDouble("relative_humidity") : null);
                        break;
                    case "rh_flag":
                        uscrnSubHourly.setRh_flag(rs.getObject("rh_flag") != null ? rs.getString("rh_flag") : null);
                        break;
                    case "soil_moisture_5":
                        uscrnSubHourly.setSoil_moisture_5(rs.getObject("soil_moisture_5") != null ? rs.getDouble("soil_moisture_5") : null);
                        break;
                    case "soil_temperature_5":
                        uscrnSubHourly.setSoil_temperature_5(rs.getObject("soil_temperature_5") != null ? rs.getDouble("soil_temperature_5") : null);
                        break;
                    case "wetness":
                        uscrnSubHourly.setWetness(rs.getObject("wetness") != null ? rs.getDouble("wetness") : null);
                        break;
                    case "wet_flag":
                        uscrnSubHourly.setWet_flag(rs.getObject("wet_flag") != null ? rs.getString("wet_flag") : null);
                        break;
                    case "wind_1_5":
                        uscrnSubHourly.setWind_1_5(rs.getObject("wind_1_5") != null ? rs.getDouble("wind_1_5") : null);
                        break;
                    case "wind_flag":
                        uscrnSubHourly.setWind_flag(rs.getObject("wind_flag") != null ? rs.getString("wind_flag") : null);
                        break;
                    case "date_of_observation_utc":
                        uscrnSubHourly.setDate_of_observation_utc(rs.getObject("date_of_observation_utc") != null ? rs.getTimestamp("date_of_observation_utc").toInstant() : null);
                        break;
                    case "date_of_observation_lst":
                        uscrnSubHourly.setDate_of_observation_lst(rs.getObject("date_of_observation_lst") != null ? rs.getTimestamp("date_of_observation_lst").toInstant() : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return uscrnSubHourly;
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
                case "sr_flag":
                case "st_type":
                case "st_flag":
                case "rh_flag":
                case "wet_flag":
                case "wind_flag":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "longitude":
                case "latitude":
                case "air_temperature":
                case "precipitation":
                case "solar_radiation":
                case "surface_temperature":
                case "relative_humidity":
                case "soil_moisture_5":
                case "soil_temperature_5":
                case "wetness":
                case "wind_1_5":
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
        UscrnSubHourly uscrnSubHourly = (UscrnSubHourly) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (uscrnSubHourly.getSitename() != null) params.put("sitename", uscrnSubHourly.getSitename());
        } else {
            params.put("sitename", uscrnSubHourly.getSitename());
            params.put("lastmod", uscrnSubHourly.getLastmod());
            params.put("wbanno", uscrnSubHourly.getWbanno());
            params.put("utc_date", uscrnSubHourly.getUtc_date());
            params.put("utc_time", uscrnSubHourly.getUtc_time());
            params.put("lst_date", uscrnSubHourly.getLst_date());
            params.put("lst_time", uscrnSubHourly.getLst_time());
            params.put("crx_vn", uscrnSubHourly.getCrx_vn());
            params.put("longitude", uscrnSubHourly.getLongitude());
            params.put("latitude", uscrnSubHourly.getLatitude());
            params.put("air_temperature", uscrnSubHourly.getAir_temperature());
            params.put("precipitation", uscrnSubHourly.getPrecipitation());
            params.put("solar_radiation", uscrnSubHourly.getSolar_radiation());
            params.put("sr_flag", uscrnSubHourly.getSr_flag());
            params.put("surface_temperature", uscrnSubHourly.getSurface_temperature());
            params.put("st_type", uscrnSubHourly.getSt_type());
            params.put("st_flag", uscrnSubHourly.getSt_flag());
            params.put("relative_humidity", uscrnSubHourly.getRelative_humidity());
            params.put("rh_flag", uscrnSubHourly.getRh_flag());
            params.put("soil_moisture_5", uscrnSubHourly.getSoil_moisture_5());
            params.put("soil_temperature_5", uscrnSubHourly.getSoil_temperature_5());
            params.put("wetness", uscrnSubHourly.getWetness());
            params.put("wet_flag", uscrnSubHourly.getWet_flag());
            params.put("wind_1_5", uscrnSubHourly.getWind_1_5());
            params.put("wind_flag", uscrnSubHourly.getWind_flag());
            params.put("date_of_observation_utc", uscrnSubHourly.getDate_of_observation_utc());
            params.put("date_of_observation_lst", uscrnSubHourly.getDate_of_observation_lst());
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
