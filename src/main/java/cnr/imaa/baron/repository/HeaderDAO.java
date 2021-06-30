package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.Header;
import cnr.imaa.baron.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class HeaderDAO extends BaseHeaderDAO {
    private final Logger log = LoggerFactory.getLogger(HeaderDAO.class);
    private final String TABLE_NAME = "header";
    private final BaseDataDAO harmonizedDataDAO;
    private final String FIELDS = "conventions,title,source,history,references_,disclaimer,id,year,month,day,hour,reltime," +
            "numlev,lat,lon,name,state,elevation,wmo_index,radiosonde_code,radiosonde_code_source,equipment_code,equipment_code_source," +
            "date_of_observation,radiosonde_id,radiosonde_name";

    public HeaderDAO(DataSource dataSource) {
        super(dataSource);

        harmonizedDataDAO = new HarmonizedDataDAO(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new Header();
    }

    @Override
    protected Object build(ResultSet rs) {
        Header header = new Header();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "header_id":
                        header.setHeaderId(rs.getObject("header_id") != null ? rs.getInt("header_id") : null);
                        break;
                    case "conventions":
                        header.setConventions(rs.getObject("conventions") != null ? rs.getString("conventions") : null);
                        break;
                    case "title":
                        header.setTitle(rs.getObject("title") != null ? rs.getString("title") : null);
                        break;
                    case "source":
                        header.setSource(rs.getObject("source") != null ? rs.getString("source") : null);
                        break;
                    case "history":
                        header.setHistory(rs.getObject("history") != null ? rs.getString("history") : null);
                        break;
                    case "references_":
                        header.setReferences(rs.getObject("references_") != null ? rs.getString("references_") : null);
                        break;
                    case "disclaimer":
                        header.setDisclaimer(rs.getObject("disclaimer") != null ? rs.getString("disclaimer") : null);
                        break;
                    case "id":
                        if (rs.getObject("id") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("id"));

                            header.setStation(station);
                        }
                        break;
                    case "year":
                        header.setYear(rs.getObject("year") != null ? rs.getInt("year") : null);
                        break;
                    case "month":
                        header.setMonth(rs.getObject("month") != null ? rs.getInt("month") : null);
                        break;
                    case "day":
                        header.setDay(rs.getObject("day") != null ? rs.getInt("day") : null);
                        break;
                    case "hour":
                        header.setHour(rs.getObject("hour") != null ? rs.getInt("hour") : null);
                        break;
                    case "reltime":
                        header.setReltime(rs.getObject("reltime") != null ? rs.getTimestamp("reltime").toInstant() : null);
                        break;
                    case "numlev":
                        header.setNumlev(rs.getObject("numlev") != null ? rs.getInt("numlev") : null);
                        break;
                    case "lat":
                        header.setLat(rs.getObject("lat") != null ? rs.getFloat("lat") : null);
                        break;
                    case "lon":
                        header.setLon(rs.getObject("lon") != null ? rs.getFloat("lon") : null);
                        break;
                    case "name":
                        header.setName(rs.getObject("name") != null ? rs.getString("name") : null);
                        break;
                    case "state":
                        header.setState(rs.getObject("state") != null ? rs.getString("state") : null);
                        break;
                    case "elevation":
                        header.setElevation(rs.getObject("elevation") != null ? rs.getFloat("elevation") : null);
                        break;
                    case "wmo_index":
                        header.setWmo_index(rs.getObject("wmo_index") != null ? rs.getInt("wmo_index") : null);
                        break;
                    case "radiosonde_code":
                        header.setRadiosonde_code(rs.getObject("radiosonde_code") != null ? rs.getInt("radiosonde_code") : null);
                        break;
                    case "radiosonde_code_source":
                        header.setRadiosonde_code_source(rs.getObject("radiosonde_code_source") != null ? rs.getString("radiosonde_code_source") : null);
                        break;
                    case "equipment_code":
                        header.setEquipment_code(rs.getObject("equipment_code") != null ? rs.getInt("equipment_code") : null);
                        break;
                    case "equipment_code_source":
                        header.setEquipment_code_source(rs.getObject("equipment_code_source") != null ? rs.getString("equipment_code_source") : null);
                        break;
                    case "date_of_observation":
                        header.setDateOfObservation(rs.getObject("date_of_observation") != null ? rs.getTimestamp("date_of_observation").toInstant() : null);
                        break;
                    case "radiosonde_id":
                        header.setRadiosonde_id(rs.getObject("radiosonde_id") != null ? rs.getInt("radiosonde_id") : null);
                        break;
                    case "radiosonde_name":
                        header.setRadiosonde_name(rs.getObject("radiosonde_name") != null ? rs.getString("radiosonde_name") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return header;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "header_id":
                case "year":
                case "month":
                case "day":
                case "hour":
                case "numlev":
                case "wmo_index":
                case "radiosonde_code":
                case "equipment_code":
                case "radiosonde_id":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "conventions":
                case "title":
                case "source":
                case "history":
                case "references_":
                case "disclaimer":
                case "id":
                case "name":
                case "state":
                case "radiosonde_code_source":
                case "equipment_code_source":
                case "radiosonde_name":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "reltime":
                case "date_of_observation":
                    setParameter(pstmt, indexParams, params.get(key), Types.TIMESTAMP_WITH_TIMEZONE);
                    indexParams++;
                    break;
                case "lat":
                case "lon":
                case "elevation":
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
        Header header = (Header) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.INSERT) {
            if (header.getHeaderId() != null)
                params.put("header_id", header.getHeaderId());
            if (header.getStation() != null && header.getStation().getIdStation() != null)
                params.put("id", header.getStation().getIdStation());
            if (header.getDateOfObservation() != null)
                params.put("date_of_observation", header.getDateOfObservation());
        } else {
            params.put("conventions", header.getConventions());
            params.put("title", header.getTitle());
            params.put("source", header.getSource());
            params.put("history", header.getHistory());
            params.put("references_", header.getReferences());
            params.put("disclaimer", header.getDisclaimer());
            params.put("id", header.getStation().getIdStation());
            params.put("year", header.getYear());
            params.put("month", header.getMonth());
            params.put("day", header.getDay());
            params.put("hour", header.getHour());
            params.put("reltime", header.getReltime());
            params.put("numlev", header.getNumlev());
            params.put("lat", header.getLat());
            params.put("lon", header.getLon());
            params.put("name", header.getName());
            params.put("state", header.getState());
            params.put("elevation", header.getElevation());
            params.put("wmo_index", header.getWmo_index());
            params.put("radiosonde_code", header.getRadiosonde_code());
            params.put("radiosonde_code_source", header.getRadiosonde_code_source());
            params.put("equipment_code", header.getEquipment_code());
            params.put("equipment_code_source", header.getEquipment_code_source());
            params.put("date_of_observation", header.getDateOfObservation());
            params.put("radiosonde_id", header.getRadiosonde_id());
            params.put("radiosonde_name", header.getRadiosonde_name());
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
        return harmonizedDataDAO;
    }
}
