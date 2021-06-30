package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;

public class StationDAO extends BaseObjDAO {
    private final Logger log = LoggerFactory.getLogger(StationDAO.class);

    private final String FIELDS = "id,continent,countrycode,elevation,idstation,latitude,longitude,name,network,wmoid";
    private final String TABLE_NAME = "station";

    public StationDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new Station();
    }

    @Override
    public Object build(ResultSet rs) {
        Station station = new Station();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        station.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
                        break;
                    case "continent":
                        station.setContinent(rs.getObject("continent") != null ? rs.getString("continent") : null);
                        break;
                    case "countrycode":
                        station.setCountryCode(rs.getObject("countrycode") != null ? rs.getString("countrycode") : null);
                        break;
                    case "elevation":
                        station.setElevation(rs.getObject("elevation") != null ? rs.getFloat("elevation") : null);
                        break;
                    case "idstation":
                        station.setIdStation(rs.getObject("idstation") != null ? rs.getString("idstation") : null);
                        break;
                    case "latitude":
                        station.setLatitude(rs.getObject("latitude") != null ? rs.getFloat("latitude") : null);
                        break;
                    case "longitude":
                        station.setLongitude(rs.getObject("longitude") != null ? rs.getFloat("longitude") : null);
                        break;
                    case "name":
                        station.setName(rs.getObject("name") != null ? rs.getString("name") : null);
                        break;
                    case "network":
                        station.setNetwork(rs.getObject("network") != null ? rs.getString("network") : null);
                        break;
                    case "wmoid":
                        station.setWmoid(rs.getObject("wmoid") != null ? rs.getInt("wmoid") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return station;
    }

    public List<Station> getByIdStationAndNetwork(String idStation, String network) {
        Station station = new Station();
        station.setIdStation(idStation);
        station.setNetwork(network);

        return (List<Station>) get(getParams(station, SQL_COMMAND.SELECT));
    }

    @Override
    public void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "id":
                    setParameter(pstmt, indexParams, params.get(key), Types.BIGINT);
                    indexParams++;
                    break;
                case "continent":
                case "countrycode":
                case "idstation":
                case "name":
                case "network":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "elevation":
                case "latitude":
                case "longitude":
                    setParameter(pstmt, indexParams, params.get(key), Types.REAL);
                    indexParams++;
                    break;
                case "wmoid":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    public LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        Station station = (Station) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (station.getId() != null) params.put("id", station.getId());
        if (station.getContinent() != null) params.put("continent", station.getContinent());
        if (station.getCountryCode() != null) params.put("countrycode", station.getCountryCode());
        if (station.getElevation() != null) params.put("elevation", station.getElevation());
        if (station.getIdStation() != null) params.put("idstation", station.getIdStation());
        if (station.getLatitude() != null) params.put("latitude", station.getLatitude());
        if (station.getLongitude() != null) params.put("longitude", station.getLongitude());
        if (station.getName() != null) params.put("name", station.getName());
        if (station.getNetwork() != null) params.put("network", station.getNetwork());
        if (station.getWmoid() != null) params.put("wmoid", station.getWmoid());

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
