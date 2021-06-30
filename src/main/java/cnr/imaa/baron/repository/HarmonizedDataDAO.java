package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.HarmonizedData;
import cnr.imaa.baron.model.Header;
import cnr.imaa.baron.model.Station;
import org.postgresql.jdbc.PgArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class HarmonizedDataDAO extends BaseDataDAO {
    private static final Logger log = LoggerFactory.getLogger(HarmonizedDataDAO.class);

    private final String TABLE_NAME = "harmonized_data";
    private static final String FIELDS = "header_id,idstation,date_of_observation,press,time,geopot,lvltyp1,lvltyp2,pflag,zflag,tflag,dpdp,fp,asc_,sza," +
            "temp," +
            "temp_product,temp_product_cor_temp,temp_product_u_cor_temp,temp_product_cor_temp_tl,temp_product_u_cor_temp_tl,temp_product_cor_intercomparison_temp,temp_product_u_cor_intercomparison_temp," +
            "temp_h,err_temp_h," +
            "rh," +
            "rh_product,rh_product_cor_rh,rh_product_u_cor_rh,rh_product_cor_rh_tl,rh_product_u_cor_rh_tl,rh_product_cor_intercomparison_rh,rh_product_u_cor_intercomparison_rh," +
            "rh_h,err_rh_h," +
            "u," +
            "u_product,u_product_cor_u,u_product_u_cor_u,u_product_cor_u_rs92,u_product_u_cor_u_rs92,u_product_cor_u_not_rs92,u_product_u_cor_u_not_rs92," +
            "u_h,err_u_h," +
            "v," +
            "v_product,v_product_cor_v,v_product_u_cor_v,v_product_cor_v_rs92,v_product_u_cor_v_rs92,v_product_cor_v_not_rs92,v_product_u_cor_v_not_rs92," +
            "v_h,err_v_h," +
            "wvmr," +
            "wvmr_product," +
            "wvmr_h," +
            "wdir," +
            "wdir_h,err_wdir_h," +
            "wspeed," +
            "wspeed_h,err_wspeed_h," +
            "reltime,check_values";

    public HarmonizedDataDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new HarmonizedData();
    }

    @Override
    protected Object build(ResultSet rs) {
        HarmonizedData harmonizedData = new HarmonizedData();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        harmonizedData.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
                        break;
                    case "header_id":
                        if (rs.getObject("header_id") != null) {
                            Header header = new Header();
                            header.setHeaderId(rs.getInt("header_id"));

                            harmonizedData.setHeader(header);
                        }
                    case "time":
                        harmonizedData.setTime(rs.getObject("time") != null ? rs.getFloat("time") : null);
                        break;
                    case "press":
                        harmonizedData.setPress(rs.getObject("press") != null ? rs.getFloat("press") : null);
                        break;
                    case "geopot":
                        harmonizedData.setGeopot(rs.getObject("geopot") != null ? rs.getFloat("geopot") : null);
                        break;
                    case "lvltyp1":
                        harmonizedData.setLvltyp1(rs.getObject("lvltyp1") != null ? rs.getFloat("lvltyp1") : null);
                        break;
                    case "lvltyp2":
                        harmonizedData.setLvltyp2(rs.getObject("lvltyp2") != null ? rs.getFloat("lvltyp2") : null);
                        break;
                    case "pflag":
                        harmonizedData.setPflag(rs.getObject("pflag") != null ? rs.getString("pflag") : null);
                        break;
                    case "zflag":
                        harmonizedData.setZflag(rs.getObject("zflag") != null ? rs.getString("zflag") : null);
                        break;
                    case "tflag":
                        harmonizedData.setTflag(rs.getObject("tflag") != null ? rs.getString("tflag") : null);
                        break;
                    case "dpdp":
                        harmonizedData.setDpdp(rs.getObject("dpdp") != null ? rs.getFloat("dpdp") : null);
                        break;
                    case "fp":
                        harmonizedData.setFp(rs.getObject("fp") != null ? rs.getFloat("fp") : null);
                        break;
                    case "asc_":
                        harmonizedData.setAsc(rs.getObject("asc_") != null ? rs.getFloat("asc_") : null);
                        break;
                    case "sza":
                        harmonizedData.setSza(rs.getObject("sza") != null ? rs.getFloat("sza") : null);
                        break;
                    case "temp":
                        harmonizedData.setTemp(rs.getObject("temp") != null ? rs.getFloat("temp") : null);
                        break;
                    case "temp_product":
                        harmonizedData.setTemp_product(rs.getObject("temp_product") != null ? rs.getFloat("temp_product") : null);
                        break;
                    case "temp_product_cor_temp":
                        harmonizedData.setTemp_product_cor_temp(rs.getObject("temp_product_cor_temp") != null ? rs.getFloat("temp_product_cor_temp") : null);
                        break;
                    case "temp_product_u_cor_temp":
                        harmonizedData.setTemp_product_u_cor_temp(rs.getObject("temp_product_u_cor_temp") != null ? rs.getFloat("temp_product_u_cor_temp") : null);
                        break;
                    case "temp_product_cor_temp_tl":
                        harmonizedData.setTemp_product_cor_temp_tl(rs.getObject("temp_product_cor_temp_tl") != null ? rs.getFloat("temp_product_cor_temp_tl") : null);
                        break;
                    case "temp_product_u_cor_temp_tl":
                        harmonizedData.setTemp_product_u_cor_temp_tl(rs.getObject("temp_product_u_cor_temp_tl") != null ? rs.getFloat("temp_product_u_cor_temp_tl") : null);
                        break;
                    case "temp_product_cor_intercomparison_temp":
                        harmonizedData.setTemp_product_cor_intercomparison_temp(rs.getObject("temp_product_cor_intercomparison_temp") != null ? rs.getFloat("temp_product_cor_intercomparison_temp") : null);
                        break;
                    case "temp_product_u_cor_intercomparison_temp":
                        harmonizedData.setTemp_product_u_cor_intercomparison_temp(rs.getObject("temp_product_u_cor_intercomparison_temp") != null ? rs.getFloat("temp_product_u_cor_intercomparison_temp") : null);
                        break;
                    case "temp_h":
                        harmonizedData.setTemp_h(rs.getObject("temp_h") != null ? rs.getFloat("temp_h") : null);
                        break;
                    case "err_temp_h":
                        harmonizedData.setErr_temp_h(rs.getObject("err_temp_h") != null ? rs.getFloat("err_temp_h") : null);
                        break;
                    case "rh":
                        harmonizedData.setRh(rs.getObject("rh") != null ? rs.getFloat("rh") : null);
                        break;
                    case "rh_product":
                        harmonizedData.setRh_product(rs.getObject("rh_product") != null ? rs.getFloat("rh_product") : null);
                        break;
                    case "rh_product_cor_rh":
                        harmonizedData.setRh_product_cor_rh(rs.getObject("rh_product_cor_rh") != null ? rs.getFloat("rh_product_cor_rh") : null);
                        break;
                    case "rh_product_u_cor_rh":
                        harmonizedData.setRh_product_u_cor_rh(rs.getObject("rh_product_u_cor_rh") != null ? rs.getFloat("rh_product_u_cor_rh") : null);
                        break;
                    case "rh_product_cor_rh_tl":
                        harmonizedData.setRh_product_cor_rh_tl(rs.getObject("rh_product_cor_rh_tl") != null ? rs.getFloat("rh_product_cor_rh_tl") : null);
                        break;
                    case "rh_product_u_cor_rh_tl":
                        harmonizedData.setRh_product_u_cor_rh_tl(rs.getObject("rh_product_u_cor_rh_tl") != null ? rs.getFloat("rh_product_u_cor_rh_tl") : null);
                        break;
                    case "rh_product_cor_intercomparison_rh":
                        harmonizedData.setRh_product_cor_intercomparison_rh(rs.getObject("rh_product_cor_intercomparison_rh") != null ? rs.getFloat("rh_product_cor_intercomparison_rh") : null);
                        break;
                    case "rh_product_u_cor_intercomparison_rh":
                        harmonizedData.setRh_product_u_cor_intercomparison_rh(rs.getObject("rh_product_u_cor_intercomparison_rh") != null ? rs.getFloat("rh_product_u_cor_intercomparison_rh") : null);
                        break;
                    case "rh_h":
                        harmonizedData.setRh_h(rs.getObject("rh_h") != null ? rs.getFloat("rh_h") : null);
                        break;
                    case "err_rh_h":
                        harmonizedData.setErr_rh_h(rs.getObject("err_rh_h") != null ? rs.getFloat("err_rh_h") : null);
                        break;
                    case "u":
                        harmonizedData.setU(rs.getObject("u") != null ? rs.getFloat("u") : null);
                        break;
                    case "u_product":
                        harmonizedData.setU_product(rs.getObject("u_product") != null ? rs.getFloat("u_product") : null);
                        break;
                    case "u_product_cor_u":
                        harmonizedData.setU_product_cor_u(rs.getObject("u_product_cor_u") != null ? rs.getFloat("u_product_cor_u") : null);
                        break;
                    case "u_product_u_cor_u":
                        harmonizedData.setU_product_u_cor_u(rs.getObject("u_product_u_cor_u") != null ? rs.getFloat("u_product_u_cor_u") : null);
                        break;
                    case "u_product_cor_u_rs92":
                        harmonizedData.setU_product_cor_u_rs92(rs.getObject("u_product_cor_u_rs92") != null ? rs.getFloat("u_product_cor_u_rs92") : null);
                        break;
                    case "u_product_u_cor_u_rs92":
                        harmonizedData.setU_product_u_cor_u_rs92(rs.getObject("u_product_u_cor_u_rs92") != null ? rs.getFloat("u_product_u_cor_u_rs92") : null);
                        break;
                    case "u_product_cor_u_not_rs92":
                        harmonizedData.setU_product_cor_u_notRs92(rs.getObject("u_product_cor_u_not_rs92") != null ? rs.getFloat("u_product_cor_u_not_rs92") : null);
                        break;
                    case "u_product_u_cor_u_not_rs92":
                        harmonizedData.setU_product_u_cor_u_notRs92(rs.getObject("u_product_u_cor_u_not_rs92") != null ? rs.getFloat("u_product_u_cor_u_not_rs92") : null);
                        break;
                    case "u_h":
                        harmonizedData.setU_h(rs.getObject("u_h") != null ? rs.getFloat("u_h") : null);
                        break;
                    case "err_u_h":
                        harmonizedData.setErr_u_h(rs.getObject("err_u_h") != null ? rs.getFloat("err_u_h") : null);
                        break;
                    case "v":
                        harmonizedData.setV(rs.getObject("v") != null ? rs.getFloat("v") : null);
                        break;
                    case "v_product":
                        harmonizedData.setV_product(rs.getObject("v_product") != null ? rs.getFloat("v_product") : null);
                        break;
                    case "v_product_cor_v":
                        harmonizedData.setV_product_cor_v(rs.getObject("v_product_cor_v") != null ? rs.getFloat("v_product_cor_v") : null);
                        break;
                    case "v_product_u_cor_v":
                        harmonizedData.setV_product_u_cor_v(rs.getObject("v_product_u_cor_v") != null ? rs.getFloat("v_product_u_cor_v") : null);
                        break;
                    case "v_product_cor_v_rs92":
                        harmonizedData.setV_product_cor_v_rs92(rs.getObject("v_product_cor_v_rs92") != null ? rs.getFloat("v_product_cor_v_rs92") : null);
                        break;
                    case "v_product_u_cor_v_rs92":
                        harmonizedData.setV_product_u_cor_v_rs92(rs.getObject("v_product_u_cor_v_rs92") != null ? rs.getFloat("v_product_u_cor_v_rs92") : null);
                        break;
                    case "v_product_cor_v_not_rs92":
                        harmonizedData.setV_product_cor_v_notRs92(rs.getObject("v_product_cor_v_not_rs92") != null ? rs.getFloat("v_product_cor_v_not_rs92") : null);
                        break;
                    case "v_product_u_cor_v_not_rs92":
                        harmonizedData.setV_product_u_cor_v_notRs92(rs.getObject("v_product_u_cor_v_not_rs92") != null ? rs.getFloat("v_product_u_cor_v_not_rs92") : null);
                        break;
                    case "v_h":
                        harmonizedData.setV_h(rs.getObject("v_h") != null ? rs.getFloat("v_h") : null);
                        break;
                    case "err_v_h":
                        harmonizedData.setErr_v_h(rs.getObject("err_v_h") != null ? rs.getFloat("err_v_h") : null);
                        break;
                    case "wvmr":
                        harmonizedData.setWvmr(rs.getObject("wvmr") != null ? rs.getFloat("wvmr") : null);
                        break;
                    case "wvmr_product":
                        harmonizedData.setWvmr_product(rs.getObject("wvmr_product") != null ? rs.getFloat("wvmr_product") : null);
                        break;
                    case "wvmr_h":
                        harmonizedData.setWvmr_h(rs.getObject("wvmr_h") != null ? rs.getFloat("wvmr_h") : null);
                        break;
                    case "wdir":
                        harmonizedData.setWdir(rs.getObject("wdir") != null ? rs.getFloat("wdir") : null);
                        break;
                    case "wdir_h":
                        harmonizedData.setWdir_h(rs.getObject("wdir_h") != null ? rs.getFloat("wdir_h") : null);
                        break;
                    case "err_wdir_h":
                        harmonizedData.setErr_wdir_h(rs.getObject("err_wdir_h") != null ? rs.getFloat("err_wdir_h") : null);
                        break;
                    case "wspeed":
                        harmonizedData.setWspeed(rs.getObject("wspeed") != null ? rs.getFloat("wspeed") : null);
                        break;
                    case "wspeed_h":
                        harmonizedData.setWspeed_h(rs.getObject("wspeed_h") != null ? rs.getFloat("wspeed_h") : null);
                        break;
                    case "err_wspeed_h":
                        harmonizedData.setErr_wspeed_h(rs.getObject("err_wspeed_h") != null ? rs.getFloat("err_wspeed_h") : null);
                        break;
                    case "date_of_observation":
                        harmonizedData.setDateOfObservation(rs.getObject("date_of_observation") != null ? rs.getTimestamp("date_of_observation").toInstant() : null);
                        break;
                    case "idstation":
                        if (rs.getObject("idstation") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("idstation"));

                            harmonizedData.setStation(station);
                        }
                        break;
                    case "reltime":
                        harmonizedData.setReltime(rs.getObject("reltime") != null ? rs.getTimestamp("reltime").toInstant() : null);
                        break;
                    case "check_values":
                        harmonizedData.setCheck(rs.getObject("check_values") != null ? (Integer[]) ((PgArray) rs.getObject("check_values")).getArray() : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return harmonizedData;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;
        for (String key : params.keySet()) {
            switch (key) {
                case "id":
                case "header_id":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "pflag":
                case "zflag":
                case "tflag":
                case "idstation":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "date_of_observation":
                case "reltime":
                    setParameter(pstmt, indexParams, params.get(key), Types.TIMESTAMP_WITH_TIMEZONE);
                    indexParams++;
                    break;
                case "time":
                case "press":
                case "geopot":
                case "lvltyp1":
                case "lvltyp2":
                case "dpdp":
                case "fp":
                case "asc_":
                case "sza":
                case "temp":
                case "temp_product":
                case "temp_product_cor_temp":
                case "temp_product_u_cor_temp":
                case "temp_product_cor_temp_tl":
                case "temp_product_u_cor_temp_tl":
                case "temp_product_cor_intercomparison_temp":
                case "temp_product_u_cor_intercomparison_temp":
                case "temp_h":
                case "err_temp_h":
                case "rh":
                case "rh_product":
                case "rh_product_cor_rh":
                case "rh_product_u_cor_rh":
                case "rh_product_cor_rh_tl":
                case "rh_product_u_cor_rh_tl":
                case "rh_product_cor_intercomparison_rh":
                case "rh_product_u_cor_intercomparison_rh":
                case "rh_h":
                case "err_rh_h":
                case "u":
                case "u_product":
                case "u_product_cor_u":
                case "u_product_u_cor_u":
                case "u_product_cor_u_rs92":
                case "u_product_u_cor_u_rs92":
                case "u_product_cor_u_not_rs92":
                case "u_product_u_cor_u_not_rs92":
                case "u_h":
                case "err_u_h":
                case "v":
                case "v_product":
                case "v_product_cor_v":
                case "v_product_u_cor_v":
                case "v_product_cor_v_rs92":
                case "v_product_u_cor_v_rs92":
                case "v_product_cor_v_not_rs92":
                case "v_product_u_cor_v_not_rs92":
                case "v_h":
                case "err_v_h":
                case "wvmr":
                case "wvmr_product":
                case "wvmr_h":
                case "wdir":
                case "wdir_h":
                case "err_wdir_h":
                case "wspeed":
                case "wspeed_h":
                case "err_wspeed_h":
                    setParameter(pstmt, indexParams, params.get(key), Types.REAL);
                    indexParams++;
                    break;
                case "check_values":
                    setParameter(pstmt, indexParams, params.get(key), Types.ARRAY);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        HarmonizedData harmonizedData = (HarmonizedData) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (harmonizedData.getHeader() != null && ((Header) harmonizedData.getHeader()).getHeaderId() != null)
                params.put("header_id", ((Header) harmonizedData.getHeader()).getHeaderId());
            if (harmonizedData.getStation() != null && harmonizedData.getStation().getIdStation() != null)
                params.put("idstation", harmonizedData.getStation().getIdStation());
            if (harmonizedData.getDateOfObservation() != null)
                params.put("date_of_observation", harmonizedData.getDateOfObservation());
            if (harmonizedData.getPress() != null)
                params.put("press", harmonizedData.getPress());
        } else {
            params.put("header_id", ((Header) harmonizedData.getHeader()).getHeaderId());
            params.put("idstation", harmonizedData.getStation().getIdStation());
            params.put("date_of_observation", harmonizedData.getDateOfObservation());
            params.put("press", harmonizedData.getPress());
            params.put("time", harmonizedData.getTime());
            params.put("geopot", harmonizedData.getGeopot());
            params.put("lvltyp1", harmonizedData.getLvltyp1());
            params.put("lvltyp2", harmonizedData.getLvltyp2());
            params.put("pflag", harmonizedData.getPflag());
            params.put("zflag", harmonizedData.getZflag());
            params.put("tflag", harmonizedData.getTflag());
            params.put("dpdp", harmonizedData.getDpdp());
            params.put("fp", harmonizedData.getFp());
            params.put("asc_", harmonizedData.getAsc());
            params.put("sza", harmonizedData.getSza());
            params.put("temp", harmonizedData.getTemp());
            params.put("temp_product", harmonizedData.getTemp_product());
            params.put("temp_product_cor_temp", harmonizedData.getTemp_product_cor_temp());
            params.put("temp_product_u_cor_temp", harmonizedData.getTemp_product_u_cor_temp());
            params.put("temp_product_cor_temp_tl", harmonizedData.getTemp_product_cor_temp_tl());
            params.put("temp_product_u_cor_temp_tl", harmonizedData.getTemp_product_u_cor_temp_tl());
            params.put("temp_product_cor_intercomparison_temp", harmonizedData.getTemp_product_cor_intercomparison_temp());
            params.put("temp_product_u_cor_intercomparison_temp", harmonizedData.getTemp_product_u_cor_intercomparison_temp());
            params.put("temp_h", harmonizedData.getTemp_h());
            params.put("err_temp_h", harmonizedData.getErr_temp_h());
            params.put("rh", harmonizedData.getRh());
            params.put("rh_product", harmonizedData.getRh_product());
            params.put("rh_product_cor_rh", harmonizedData.getRh_product_cor_rh());
            params.put("rh_product_u_cor_rh", harmonizedData.getRh_product_u_cor_rh());
            params.put("rh_product_cor_rh_tl", harmonizedData.getRh_product_cor_rh_tl());
            params.put("rh_product_u_cor_rh_tl", harmonizedData.getRh_product_u_cor_rh_tl());
            params.put("rh_product_cor_intercomparison_rh", harmonizedData.getRh_product_cor_intercomparison_rh());
            params.put("rh_product_u_cor_intercomparison_rh", harmonizedData.getRh_product_u_cor_intercomparison_rh());
            params.put("rh_h", harmonizedData.getRh_h());
            params.put("err_rh_h", harmonizedData.getErr_rh_h());
            params.put("u", harmonizedData.getU());
            params.put("u_product", harmonizedData.getU_product());
            params.put("u_product_cor_u", harmonizedData.getU_product_cor_u());
            params.put("u_product_u_cor_u", harmonizedData.getU_product_u_cor_u());
            params.put("u_product_cor_u_rs92", harmonizedData.getU_product_cor_u_rs92());
            params.put("u_product_u_cor_u_rs92", harmonizedData.getU_product_u_cor_u_rs92());
            params.put("u_product_cor_u_not_rs92", harmonizedData.getU_product_cor_u_notRs92());
            params.put("u_product_u_cor_u_not_rs92", harmonizedData.getU_product_u_cor_u_notRs92());
            params.put("u_h", harmonizedData.getU_h());
            params.put("err_u_h", harmonizedData.getErr_u_h());
            params.put("v", harmonizedData.getV());
            params.put("v_product", harmonizedData.getV_product());
            params.put("v_product_cor_v", harmonizedData.getV_product_cor_v());
            params.put("v_product_u_cor_v", harmonizedData.getV_product_u_cor_v());
            params.put("v_product_cor_v_rs92", harmonizedData.getV_product_cor_v_rs92());
            params.put("v_product_u_cor_v_rs92", harmonizedData.getV_product_u_cor_v_rs92());
            params.put("v_product_cor_v_not_rs92", harmonizedData.getV_product_cor_v_notRs92());
            params.put("v_product_u_cor_v_not_rs92", harmonizedData.getV_product_u_cor_v_notRs92());
            params.put("v_h", harmonizedData.getV_h());
            params.put("err_v_h", harmonizedData.getErr_v_h());
            params.put("wvmr", harmonizedData.getWvmr());
            params.put("wvmr_product", harmonizedData.getWvmr_product());
            params.put("wvmr_h", harmonizedData.getWvmr_h());
            params.put("wdir", harmonizedData.getWdir());
            params.put("wdir_h", harmonizedData.getWdir_h());
            params.put("err_wdir_h", harmonizedData.getErr_wdir_h());
            params.put("wspeed", harmonizedData.getWspeed());
            params.put("wspeed_h", harmonizedData.getWspeed_h());
            params.put("err_wspeed_h", harmonizedData.getErr_wspeed_h());
            params.put("reltime", harmonizedData.getReltime());
            params.put("check_values", harmonizedData.getCheck());
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
