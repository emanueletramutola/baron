package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.GuanDataHeader;
import cnr.imaa.baron.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class GuanDataHeaderDAO extends BaseHeaderDAO {
    private static final Logger log = LoggerFactory.getLogger(GuanDataHeaderDAO.class);

    private final String TABLE_NAME = "guan_data_header";
    private final BaseDataDAO guanDataValueDAO = new GuanDataValueDAO(this.getDataSource());
    private static final String FIELDS = "idstation,date_of_observation,day,hour,month,np_src,numlev,p_src,reltime,year,version,lat,lon";

    public GuanDataHeaderDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;
        for (String key : params.keySet()) {
            switch (key) {
                case "lat":
                case "lon":
                    setParameter(pstmt, indexParams, params.get(key), Types.REAL);
                    indexParams++;
                    break;
                case "guandataheader_id":
                case "day":
                case "version":
                case "hour":
                case "month":
                case "numlev":
                case "reltime":
                case "year":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "idstation":
                case "np_src":
                case "p_src":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
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
    protected BaseObj getBaseObjFilter() {
        return new GuanDataHeader();
    }

    @Override
    protected Object build(ResultSet rs) {
        GuanDataHeader guanDataHeader = new GuanDataHeader();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "guandataheader_id":
                        guanDataHeader.setGuandataheader_id(rs.getObject("guandataheader_id") != null ? rs.getInt("guandataheader_id") : null);
                        break;
                    case "day":
                        guanDataHeader.setDay(rs.getObject("day") != null ? rs.getInt("day") : null);
                        break;
                    case "hour":
                        guanDataHeader.setHour(rs.getObject("hour") != null ? rs.getInt("hour") : null);
                        break;
                    case "month":
                        guanDataHeader.setMonth(rs.getObject("month") != null ? rs.getInt("month") : null);
                        break;
                    case "numlev":
                        guanDataHeader.setNumlev(rs.getObject("numlev") != null ? rs.getInt("numlev") : null);
                        break;
                    case "reltime":
                        guanDataHeader.setReltime(rs.getObject("reltime") != null ? rs.getInt("reltime") : null);
                        break;
                    case "year":
                        guanDataHeader.setYear(rs.getObject("year") != null ? rs.getInt("year") : null);
                        break;
                    case "version":
                        guanDataHeader.setVersion(rs.getObject("version") != null ? rs.getInt("version") : null);
                        break;
                    case "idstation":
                        if (rs.getObject("idstation") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("idstation"));

                            guanDataHeader.setStation(station);
                        }
                        break;
                    case "np_src":
                        guanDataHeader.setNp_src(rs.getObject("np_src") != null ? rs.getString("np_src") : null);
                        break;
                    case "p_src":
                        guanDataHeader.setP_src(rs.getObject("p_src") != null ? rs.getString("p_src") : null);
                        break;
                    case "date_of_observation":
                        guanDataHeader.setDateOfObservation(rs.getObject("date_of_observation") != null ? rs.getTimestamp("date_of_observation").toInstant() : null);
                        break;
                    case "lat":
                        guanDataHeader.setLat(rs.getObject("lat") != null ? rs.getFloat("lat") : null);
                        break;
                    case "lon":
                        guanDataHeader.setLon(rs.getObject("lon") != null ? rs.getFloat("lon") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return guanDataHeader;
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        GuanDataHeader guanDataHeader = (GuanDataHeader) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (guanDataHeader.getGuandataheader_id() != null)
                params.put("guandataheader_id", guanDataHeader.getGuandataheader_id());
            if (guanDataHeader.getStation() != null && guanDataHeader.getStation().getIdStation() != null)
                params.put("idstation", guanDataHeader.getStation().getIdStation());
            if (guanDataHeader.getDateOfObservation() != null)
                params.put("date_of_observation", guanDataHeader.getDateOfObservation());
        } else {
            params.put("idstation", guanDataHeader.getStation().getIdStation());
            params.put("date_of_observation", guanDataHeader.getDateOfObservation());
            params.put("day", guanDataHeader.getDay());
            params.put("hour", guanDataHeader.getHour());
            params.put("month", guanDataHeader.getMonth());
            params.put("np_src", guanDataHeader.getNp_src());
            params.put("numlev", guanDataHeader.getNumlev());
            params.put("p_src", guanDataHeader.getP_src());
            params.put("reltime", guanDataHeader.getReltime());
            params.put("year", guanDataHeader.getYear());
            params.put("version", guanDataHeader.getVersion());
            params.put("lat", guanDataHeader.getLat());
            params.put("lon", guanDataHeader.getLon());
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

    @Override
    protected BaseDataDAO getBaseDataDAO() {
        return guanDataValueDAO;
    }
}
