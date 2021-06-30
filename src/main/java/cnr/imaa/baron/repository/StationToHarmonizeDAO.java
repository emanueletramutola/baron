package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.Station;
import cnr.imaa.baron.model.StationToHarmonize;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StationToHarmonizeDAO extends BaseObjDAO {
    private final String FIELDS = "idstation,launches,initlist,readigra,product,calch,endseth,interpfinal,db,total,status,lastupdate";
    private final String TABLE_NAME = "station_to_harmonize";

    public StationToHarmonizeDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getUpdateSetStatement(Object obj) {
        StationToHarmonize stationToHarmonize = (StationToHarmonize) obj;
        String updateSetStatement = "";

        List<String> updateSetStatementList = new ArrayList<>();
        if (stationToHarmonize.getLaunches() != null) updateSetStatementList.add("launches = ?");
        if (stationToHarmonize.getInitlist() != null) updateSetStatementList.add("initlist = ?");
        if (stationToHarmonize.getReadigra() != null) updateSetStatementList.add("readigra = ?");
        if (stationToHarmonize.getProduct() != null) updateSetStatementList.add("product = ?");
        if (stationToHarmonize.getCalch() != null) updateSetStatementList.add("calch = ?");
        if (stationToHarmonize.getEndseth() != null) updateSetStatementList.add("endseth = ?");
        if (stationToHarmonize.getInterpfinal() != null) updateSetStatementList.add("interpfinal = ?");
        if (stationToHarmonize.getDb() != null) updateSetStatementList.add("db = ?");
        if (stationToHarmonize.getTotal() != null) updateSetStatementList.add("total = ?");
        if (stationToHarmonize.getStatus() != null) updateSetStatementList.add("status = ?");
        if (stationToHarmonize.getLastupdate() != null) updateSetStatementList.add("lastupdate = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement += ",";

            updateSetStatement += updateSetStatementList.get(i);
        }

        return updateSetStatement;
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new StationToHarmonize();
    }

    @Override
    protected Object build(ResultSet rs) {
        StationToHarmonize stationToHarmonize = new StationToHarmonize();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "idstation":
                        if (rs.getObject("idstation") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("idstation"));

                            stationToHarmonize.setStation(station);
                        }

                        break;
                    case "launches":
                        stationToHarmonize.setLaunches(rs.getObject("launches") != null ? rs.getInt("launches") : null);
                        break;
                    case "initlist":
                        stationToHarmonize.setInitlist(rs.getObject("initlist") != null ? rs.getInt("initlist") : null);
                        break;
                    case "readigra":
                        stationToHarmonize.setReadigra(rs.getObject("readigra") != null ? rs.getInt("readigra") : null);
                        break;
                    case "product":
                        stationToHarmonize.setProduct(rs.getObject("product") != null ? rs.getInt("product") : null);
                        break;
                    case "calch":
                        stationToHarmonize.setCalch(rs.getObject("calch") != null ? rs.getInt("calch") : null);
                        break;
                    case "endseth":
                        stationToHarmonize.setEndseth(rs.getObject("endseth") != null ? rs.getInt("endseth") : null);
                        break;
                    case "interpfinal":
                        stationToHarmonize.setInterpfinal(rs.getObject("interpfinal") != null ? rs.getInt("interpfinal") : null);
                        break;
                    case "db":
                        stationToHarmonize.setDb(rs.getObject("db") != null ? rs.getInt("db") : null);
                        break;
                    case "total":
                        stationToHarmonize.setTotal(rs.getObject("total") != null ? rs.getInt("total") : null);
                        break;
                    case "status":
                        stationToHarmonize.setStatus(rs.getObject("status") != null ? rs.getString("status").charAt(0) : null);
                        break;
                    case "lastupdate":
                        stationToHarmonize.setLastupdate(rs.getObject("lastupdate") != null ? rs.getTimestamp("lastupdate").toInstant() : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return stationToHarmonize;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "idstation":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "status":
                    setParameter(pstmt, indexParams, params.get(key), Types.CHAR);
                    indexParams++;
                    break;
                case "launches":
                case "initlist":
                case "readigra":
                case "product":
                case "calch":
                case "endseth":
                case "interpfinal":
                case "db":
                case "total":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "lastupdate":
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
        StationToHarmonize stationToHarmonize = (StationToHarmonize) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sqlCommand != SQL_COMMAND.UPDATE) {
            if (stationToHarmonize.getStation() != null && stationToHarmonize.getStation().getIdStation() != null)
                params.put("idstation", stationToHarmonize.getStation().getIdStation());
        }

        if (stationToHarmonize.getLaunches() != null) params.put("launches", stationToHarmonize.getLaunches());
        if (stationToHarmonize.getInitlist() != null) params.put("initlist", stationToHarmonize.getInitlist());
        if (stationToHarmonize.getReadigra() != null) params.put("readigra", stationToHarmonize.getReadigra());
        if (stationToHarmonize.getProduct() != null) params.put("product", stationToHarmonize.getProduct());
        if (stationToHarmonize.getCalch() != null) params.put("calch", stationToHarmonize.getCalch());
        if (stationToHarmonize.getEndseth() != null) params.put("endseth", stationToHarmonize.getEndseth());
        if (stationToHarmonize.getInterpfinal() != null)
            params.put("interpfinal", stationToHarmonize.getInterpfinal());
        if (stationToHarmonize.getDb() != null) params.put("db", stationToHarmonize.getDb());
        if (stationToHarmonize.getTotal() != null) params.put("total", stationToHarmonize.getTotal());
        if (stationToHarmonize.getStatus() != null) params.put("status", stationToHarmonize.getStatus());
        if (stationToHarmonize.getLastupdate() != null)
            params.put("lastupdate", stationToHarmonize.getLastupdate());

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
}
