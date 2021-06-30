package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.GuanDataHeader;
import cnr.imaa.baron.model.GuanDataValue;
import cnr.imaa.baron.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class GuanDataValueDAO extends BaseDataDAO {
    private static final Logger log = LoggerFactory.getLogger(GuanDataValueDAO.class);

    private final String TABLE_NAME = "guan_data_value";
    private static final String FIELDS = "guan_data_header_id,idstation,date_of_observation,press,etime,gph,lvltyp1,lvltyp2,pflag,tflag,wdir,zflag," +
            "version,dpdp,temp,rh,wspd";

    public GuanDataValueDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new GuanDataValue();
    }

    @Override
    protected Object build(ResultSet rs) {
        GuanDataValue guanDataValue = new GuanDataValue();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        guanDataValue.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
                        break;
                    case "dpdp":
                        guanDataValue.setDpdp(rs.getObject("dpdp") != null ? rs.getFloat("dpdp") : null);
                        break;
                    case "etime":
                        guanDataValue.setEtime(rs.getObject("etime") != null ? rs.getInt("etime") : null);
                        break;
                    case "gph":
                        guanDataValue.setGph(rs.getObject("gph") != null ? rs.getInt("gph") : null);
                        break;
                    case "lvltyp1":
                        guanDataValue.setLvltyp1(rs.getObject("lvltyp1") != null ? rs.getInt("lvltyp1") : null);
                        break;
                    case "lvltyp2":
                        guanDataValue.setLvltyp2(rs.getObject("lvltyp2") != null ? rs.getInt("lvltyp2") : null);
                        break;
                    case "pflag":
                        guanDataValue.setPflag(rs.getObject("pflag") != null ? rs.getString("pflag") : null);
                        break;
                    case "press":
                        guanDataValue.setPress(rs.getObject("press") != null ? rs.getInt("press") : null);
                        break;
                    case "rh":
                        guanDataValue.setRh(rs.getObject("rh") != null ? rs.getFloat("rh") : null);
                        break;
                    case "temp":
                        guanDataValue.setTemp(rs.getObject("temp") != null ? rs.getFloat("temp") : null);
                        break;
                    case "tflag":
                        guanDataValue.setTflag(rs.getObject("tflag") != null ? rs.getString("tflag") : null);
                        break;
                    case "wdir":
                        guanDataValue.setWdir(rs.getObject("wdir") != null ? rs.getInt("wdir") : null);
                        break;
                    case "wspd":
                        guanDataValue.setWspd(rs.getObject("wspd") != null ? rs.getFloat("wspd") : null);
                        break;
                    case "zflag":
                        guanDataValue.setZflag(rs.getObject("zflag") != null ? rs.getString("zflag") : null);
                        break;
                    case "idstation":
                        if (rs.getObject("idstation") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("idstation"));

                            guanDataValue.setStation(station);
                        }
                        break;
                    case "date_of_observation":
                        guanDataValue.setDateOfObservation(rs.getObject("date_of_observation") != null ? rs.getTimestamp("date_of_observation").toInstant() : null);
                        break;
                    case "version":
                        guanDataValue.setVersion(rs.getObject("version") != null ? rs.getInt("version") : null);
                        break;
                    case "guan_data_header_id":
                        if (rs.getObject("guan_data_header_id") != null) {
                            GuanDataHeader guanDataHeader = new GuanDataHeader();
                            guanDataHeader.setGuandataheader_id(rs.getInt("guan_data_header_id"));
                            guanDataValue.setHeader(guanDataHeader);
                        }
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return guanDataValue;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;
        for (String key : params.keySet()) {
            switch (key) {
                case "etime":
                case "gph":
                case "lvltyp1":
                case "lvltyp2":
                case "press":
                case "wdir":
                case "version":
                case "guan_data_header_id":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "pflag":
                case "tflag":
                case "zflag":
                case "idstation":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "dpdp":
                case "temp":
                case "rh":
                case "wspd":
                    setParameter(pstmt, indexParams, params.get(key), Types.REAL);
                    indexParams++;
                    break;
                case "date_of_observation":
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
        GuanDataValue guanDataValue = (GuanDataValue) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (guanDataValue.getHeader() != null && ((GuanDataHeader) guanDataValue.getHeader()).getGuandataheader_id() != null)
                params.put("guan_data_header_id", ((GuanDataHeader) guanDataValue.getHeader()).getGuandataheader_id());
            if (guanDataValue.getStation() != null && guanDataValue.getStation().getIdStation() != null)
                params.put("idstation", guanDataValue.getStation().getIdStation());
            if (guanDataValue.getDateOfObservation() != null)
                params.put("date_of_observation", guanDataValue.getDateOfObservation());
            if (guanDataValue.getPress() != null)
                params.put("press", guanDataValue.getPress());
        } else {
            params.put("guan_data_header_id", ((GuanDataHeader) guanDataValue.getHeader()).getGuandataheader_id());
            params.put("idstation", guanDataValue.getStation().getIdStation());
            params.put("date_of_observation", guanDataValue.getDateOfObservation());
            params.put("press", guanDataValue.getPress());
            params.put("etime", guanDataValue.getEtime());
            params.put("gph", guanDataValue.getGph());
            params.put("lvltyp1", guanDataValue.getLvltyp1());
            params.put("lvltyp2", guanDataValue.getLvltyp2());
            params.put("pflag", guanDataValue.getPflag());
            params.put("tflag", guanDataValue.getTflag());
            params.put("wdir", guanDataValue.getWdir());
            params.put("zflag", guanDataValue.getZflag());
            params.put("version", guanDataValue.getVersion());
            params.put("dpdp", guanDataValue.getDpdp());
            params.put("temp", guanDataValue.getTemp());
            params.put("rh", guanDataValue.getRh());
            params.put("wspd", guanDataValue.getWspd());
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
