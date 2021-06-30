package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.GruanDataHeader;
import cnr.imaa.baron.model.GruanDataValue;
import cnr.imaa.baron.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class GruanDataValueDAO extends BaseDataDAO {
    private final Logger log = LoggerFactory.getLogger(GruanDataValueDAO.class);

    private final String TABLE_NAME = "gruan_data_value";
    private final String FIELDS = "gruan_data_header_id,g_general_site_code,date_of_observation,press,alt,asc_,cor_rh,cor_temp,fp,geopot,lat,lon,res_rh,rh,swrad,temp,time,u,u_alt,u_cor_rh,u_cor_temp," +
            "u_press,u_rh,u_std_rh,u_std_temp,u_swrad,u_temp,u_wdir,u_wspeed,v,wdir,wspeed,wvmr";

    public GruanDataValueDAO(DataSource dataSource) {
        super(dataSource);
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
                case "alt":
                case "asc_":
                case "cor_rh":
                case "cor_temp":
                case "fp":
                case "geopot":
                case "lat":
                case "lon":
                case "press":
                case "res_rh":
                case "rh":
                case "swrad":
                case "temp":
                case "time":
                case "u":
                case "u_alt":
                case "u_cor_rh":
                case "u_cor_temp":
                case "u_press":
                case "u_rh":
                case "u_std_rh":
                case "u_std_temp":
                case "u_swrad":
                case "u_temp":
                case "u_wdir":
                case "u_wspeed":
                case "v":
                case "wdir":
                case "wspeed":
                case "wvmr":
                    setParameter(pstmt, indexParams, params.get(key), Types.REAL);
                    indexParams++;
                    break;
                case "date_of_observation":
                    setParameter(pstmt, indexParams, params.get(key), Types.TIMESTAMP_WITH_TIMEZONE);
                    indexParams++;
                    break;
                case "g_general_site_code":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "gruan_data_header_id":
                    setParameter(pstmt, indexParams, params.get(key), Types.BIGINT);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new GruanDataValue();
    }

    @Override
    protected Object build(ResultSet rs) {
        GruanDataValue gruanDataValue = new GruanDataValue();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        gruanDataValue.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
                        break;
                    case "alt":
                        gruanDataValue.setAlt(rs.getObject("alt") != null ? rs.getFloat("alt") : null);
                        break;
                    case "asc_":
                        gruanDataValue.setAsc_(rs.getObject("asc_") != null ? rs.getFloat("asc_") : null);
                        break;
                    case "cor_rh":
                        gruanDataValue.setCor_rh(rs.getObject("cor_rh") != null ? rs.getFloat("cor_rh") : null);
                        break;
                    case "cor_temp":
                        gruanDataValue.setCor_temp(rs.getObject("cor_temp") != null ? rs.getFloat("cor_temp") : null);
                        break;
                    case "fp":
                        gruanDataValue.setFp(rs.getObject("fp") != null ? rs.getFloat("fp") : null);
                        break;
                    case "geopot":
                        gruanDataValue.setGeopot(rs.getObject("geopot") != null ? rs.getFloat("geopot") : null);
                        break;
                    case "lat":
                        gruanDataValue.setLat(rs.getObject("lat") != null ? rs.getFloat("lat") : null);
                        break;
                    case "lon":
                        gruanDataValue.setLon(rs.getObject("lon") != null ? rs.getFloat("lon") : null);
                        break;
                    case "press":
                        gruanDataValue.setPress(rs.getObject("press") != null ? rs.getFloat("press") : null);
                        break;
                    case "res_rh":
                        gruanDataValue.setRes_rh(rs.getObject("res_rh") != null ? rs.getFloat("res_rh") : null);
                        break;
                    case "rh":
                        gruanDataValue.setRh(rs.getObject("rh") != null ? rs.getFloat("rh") : null);
                        break;
                    case "swrad":
                        gruanDataValue.setSwrad(rs.getObject("swrad") != null ? rs.getFloat("swrad") : null);
                        break;
                    case "temp":
                        gruanDataValue.setTemp(rs.getObject("temp") != null ? rs.getFloat("temp") : null);
                        break;
                    case "time":
                        gruanDataValue.setTime(rs.getObject("time") != null ? rs.getFloat("time") : null);
                        break;
                    case "u":
                        gruanDataValue.setU(rs.getObject("u") != null ? rs.getFloat("u") : null);
                        break;
                    case "u_alt":
                        gruanDataValue.setU_alt(rs.getObject("u_alt") != null ? rs.getFloat("u_alt") : null);
                        break;
                    case "u_cor_rh":
                        gruanDataValue.setU_cor_rh(rs.getObject("u_cor_rh") != null ? rs.getFloat("u_cor_rh") : null);
                        break;
                    case "u_cor_temp":
                        gruanDataValue.setU_cor_temp(rs.getObject("u_cor_temp") != null ? rs.getFloat("u_cor_temp") : null);
                        break;
                    case "u_press":
                        gruanDataValue.setU_press(rs.getObject("u_press") != null ? rs.getFloat("u_press") : null);
                        break;
                    case "u_rh":
                        gruanDataValue.setU_rh(rs.getObject("u_rh") != null ? rs.getFloat("u_rh") : null);
                        break;
                    case "u_std_rh":
                        gruanDataValue.setU_std_rh(rs.getObject("u_std_rh") != null ? rs.getFloat("u_std_rh") : null);
                        break;
                    case "u_std_temp":
                        gruanDataValue.setU_std_temp(rs.getObject("u_std_temp") != null ? rs.getFloat("u_std_temp") : null);
                        break;
                    case "u_swrad":
                        gruanDataValue.setU_swrad(rs.getObject("u_swrad") != null ? rs.getFloat("u_swrad") : null);
                        break;
                    case "u_temp":
                        gruanDataValue.setU_temp(rs.getObject("u_temp") != null ? rs.getFloat("u_temp") : null);
                        break;
                    case "u_wdir":
                        gruanDataValue.setU_wdir(rs.getObject("u_wdir") != null ? rs.getFloat("u_wdir") : null);
                        break;
                    case "u_wspeed":
                        gruanDataValue.setU_wspeed(rs.getObject("u_wspeed") != null ? rs.getFloat("u_wspeed") : null);
                        break;
                    case "v":
                        gruanDataValue.setV(rs.getObject("v") != null ? rs.getFloat("v") : null);
                        break;
                    case "wdir":
                        gruanDataValue.setWdir(rs.getObject("wdir") != null ? rs.getFloat("wdir") : null);
                        break;
                    case "wspeed":
                        gruanDataValue.setWspeed(rs.getObject("wspeed") != null ? rs.getFloat("wspeed") : null);
                        break;
                    case "wvmr":
                        gruanDataValue.setWvmr(rs.getObject("wvmr") != null ? rs.getFloat("wvmr") : null);
                        break;
                    case "date_of_observation":
                        gruanDataValue.setDateOfObservation(rs.getObject("date_of_observation") != null ? rs.getTimestamp("date_of_observation").toInstant() : null);
                        break;
                    case "g_general_site_code":
                        if (rs.getObject("g_general_site_code") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("g_general_site_code"));

                            gruanDataValue.setStation(station);
                        }
                        break;
                    case "gruan_data_header_id":
                        if (rs.getObject("gruan_data_header_id") != null) {
                            GruanDataHeader gruanDataHeader = new GruanDataHeader();
                            gruanDataHeader.setG_product_id(rs.getLong("gruan_data_header_id"));
                            gruanDataValue.setHeader(gruanDataHeader);
                        }
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return gruanDataValue;
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        GruanDataValue gruanDataValue = (GruanDataValue) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (gruanDataValue.getId() != null)
                params.put("id", gruanDataValue.getId());
            if (gruanDataValue.getHeader() != null && ((GruanDataHeader) gruanDataValue.getHeader()).getG_product_id() != null)
                params.put("gruan_data_header_id", ((GruanDataHeader) gruanDataValue.getHeader()).getG_product_id());
            if (gruanDataValue.getStation() != null && gruanDataValue.getStation().getIdStation() != null)
                params.put("g_general_site_code", gruanDataValue.getStation().getIdStation());
            if (gruanDataValue.getDateOfObservation() != null)
                params.put("date_of_observation", gruanDataValue.getDateOfObservation());
            if (gruanDataValue.getPress() != null) params.put("press", gruanDataValue.getPress());
        } else {
            params.put("gruan_data_header_id", ((GruanDataHeader) gruanDataValue.getHeader()).getG_product_id());
            params.put("g_general_site_code", gruanDataValue.getStation().getIdStation());
            params.put("date_of_observation", gruanDataValue.getDateOfObservation());
            params.put("press", gruanDataValue.getPress());
            params.put("alt", gruanDataValue.getAlt());
            params.put("asc_", gruanDataValue.getAsc_());
            params.put("cor_rh", gruanDataValue.getCor_rh());
            params.put("cor_temp", gruanDataValue.getCor_temp());
            params.put("fp", gruanDataValue.getFp());
            params.put("geopot", gruanDataValue.getGeopot());
            params.put("lat", gruanDataValue.getLat());
            params.put("lon", gruanDataValue.getLon());
            params.put("res_rh", gruanDataValue.getRes_rh());
            params.put("rh", gruanDataValue.getRh());
            params.put("swrad", gruanDataValue.getSwrad());
            params.put("temp", gruanDataValue.getTemp());
            params.put("time", gruanDataValue.getTime());
            params.put("u", gruanDataValue.getU());
            params.put("u_alt", gruanDataValue.getU_alt());
            params.put("u_cor_rh", gruanDataValue.getU_cor_rh());
            params.put("u_cor_temp", gruanDataValue.getU_cor_temp());
            params.put("u_press", gruanDataValue.getU_press());
            params.put("u_rh", gruanDataValue.getU_rh());
            params.put("u_std_rh", gruanDataValue.getU_std_rh());
            params.put("u_std_temp", gruanDataValue.getU_std_temp());
            params.put("u_swrad", gruanDataValue.getU_swrad());
            params.put("u_temp", gruanDataValue.getU_temp());
            params.put("u_wdir", gruanDataValue.getU_wdir());
            params.put("u_wspeed", gruanDataValue.getU_wspeed());
            params.put("v", gruanDataValue.getV());
            params.put("wdir", gruanDataValue.getWdir());
            params.put("wspeed", gruanDataValue.getWspeed());
            params.put("wvmr", gruanDataValue.getWvmr());
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