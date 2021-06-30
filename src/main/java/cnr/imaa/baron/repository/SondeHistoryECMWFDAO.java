package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.SondeHistoryECMWF;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SondeHistoryECMWFDAO extends BaseObjDAO {
    private final String FIELDS = "id,wmoid,nrep,nwt,nww,n99,latitude,longitude,radiosonde_type,maxl,ndp,ndt,bufr_nrep,bufr_nwt,bufr_nww,bufr_n99,bufr_nhr,bufr_latitude,bufr_longitude,bufr_radiosonde_type,bufr_maxl,bufr_ndp,bufr_ndt,notes,date,verified";
    private final String TABLE_NAME = "sonde_history_ecmwf";

    public SondeHistoryECMWFDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new SondeHistoryECMWF();
    }

    @Override
    protected Object build(ResultSet rs) {
        SondeHistoryECMWF sondeHistoryECMWF = new SondeHistoryECMWF();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        sondeHistoryECMWF.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
                        break;
                    case "wmoid":
                        sondeHistoryECMWF.setWmoid(rs.getObject("wmoid") != null ? rs.getInt("wmoid") : null);
                        break;
                    case "nrep":
                        sondeHistoryECMWF.setNrep(rs.getObject("nrep") != null ? rs.getInt("nrep") : null);
                        break;
                    case "nwt":
                        sondeHistoryECMWF.setNwt(rs.getObject("nwt") != null ? rs.getInt("nwt") : null);
                        break;
                    case "nww":
                        sondeHistoryECMWF.setNww(rs.getObject("nww") != null ? rs.getInt("nww") : null);
                        break;
                    case "n99":
                        sondeHistoryECMWF.setN99(rs.getObject("n99") != null ? rs.getInt("n99") : null);
                        break;
                    case "latitude":
                        sondeHistoryECMWF.setLatitude(rs.getObject("latitude") != null ? rs.getFloat("latitude") : null);
                        break;
                    case "longitude":
                        sondeHistoryECMWF.setLongitude(rs.getObject("longitude") != null ? rs.getFloat("longitude") : null);
                        break;
                    case "radiosonde_type":
                        sondeHistoryECMWF.setRadiosonde_type(rs.getObject("radiosonde_type") != null ? rs.getInt("radiosonde_type") : null);
                        break;
                    case "maxl":
                        sondeHistoryECMWF.setMaxl(rs.getObject("maxl") != null ? rs.getInt("maxl") : null);
                        break;
                    case "ndp":
                        sondeHistoryECMWF.setNdp(rs.getObject("ndp") != null ? rs.getInt("ndp") : null);
                        break;
                    case "ndt":
                        sondeHistoryECMWF.setNdt(rs.getObject("ndt") != null ? rs.getInt("ndt") : null);
                        break;
                    case "bufr_nrep":
                        sondeHistoryECMWF.setBufr_nrep(rs.getObject("bufr_nrep") != null ? rs.getInt("bufr_nrep") : null);
                        break;
                    case "bufr_nwt":
                        sondeHistoryECMWF.setBufr_nwt(rs.getObject("bufr_nwt") != null ? rs.getInt("bufr_nwt") : null);
                        break;
                    case "bufr_nww":
                        sondeHistoryECMWF.setBufr_nww(rs.getObject("bufr_nww") != null ? rs.getInt("bufr_nww") : null);
                        break;
                    case "bufr_n99":
                        sondeHistoryECMWF.setBufr_n99(rs.getObject("bufr_n99") != null ? rs.getInt("bufr_n99") : null);
                        break;
                    case "bufr_nhr":
                        sondeHistoryECMWF.setBufr_nhr(rs.getObject("bufr_nhr") != null ? rs.getInt("bufr_nhr") : null);
                        break;
                    case "bufr_latitude":
                        sondeHistoryECMWF.setBufr_latitude(rs.getObject("bufr_latitude") != null ? rs.getFloat("bufr_latitude") : null);
                        break;
                    case "bufr_longitude":
                        sondeHistoryECMWF.setBufr_longitude(rs.getObject("bufr_longitude") != null ? rs.getFloat("bufr_longitude") : null);
                        break;
                    case "bufr_radiosonde_type":
                        sondeHistoryECMWF.setBufr_radiosonde_type(rs.getObject("bufr_radiosonde_type") != null ? rs.getInt("bufr_radiosonde_type") : null);
                        break;
                    case "bufr_maxl":
                        sondeHistoryECMWF.setBufr_maxl(rs.getObject("bufr_maxl") != null ? rs.getInt("bufr_maxl") : null);
                        break;
                    case "bufr_ndp":
                        sondeHistoryECMWF.setBufr_ndp(rs.getObject("bufr_ndp") != null ? rs.getInt("bufr_ndp") : null);
                        break;
                    case "bufr_ndt":
                        sondeHistoryECMWF.setBufr_ndt(rs.getObject("bufr_ndt") != null ? rs.getInt("bufr_ndt") : null);
                        break;
                    case "notes":
                        sondeHistoryECMWF.setNotes(rs.getObject("notes") != null ? rs.getString("notes") : null);
                        break;
                    case "date":
                        sondeHistoryECMWF.setDate(rs.getObject("date") != null ? rs.getTimestamp("date").toInstant() : null);
                        break;
                    case "verified":
                        sondeHistoryECMWF.setVerified(rs.getObject("verified") != null ? rs.getBoolean("verified") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return sondeHistoryECMWF;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "id":
                    setParameter(pstmt, indexParams, params.get(key), Types.BIGINT);
                    indexParams++;
                    break;
                case "wmoid":
                case "nrep":
                case "nwt":
                case "nww":
                case "n99":
                case "radiosonde_type":
                case "maxl":
                case "ndp":
                case "ndt":
                case "bufr_nrep":
                case "bufr_nwt":
                case "bufr_nww":
                case "bufr_n99":
                case "bufr_nhr":
                case "bufr_radiosonde_type":
                case "bufr_maxl":
                case "bufr_ndp":
                case "bufr_ndt":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "notes":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "date":
                    setParameter(pstmt, indexParams, params.get(key), Types.TIMESTAMP_WITH_TIMEZONE);
                    indexParams++;
                    break;
                case "verified":
                    setParameter(pstmt, indexParams, params.get(key), Types.BOOLEAN);
                    indexParams++;
                    break;
                case "latitude":
                case "longitude":
                case "bufr_latitude":
                case "bufr_longitude":
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
        SondeHistoryECMWF sondeHistoryECMWF = (SondeHistoryECMWF) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sondeHistoryECMWF.getId() != null) params.put("id", sondeHistoryECMWF.getId());
        if (sondeHistoryECMWF.getWmoid() != null) params.put("wmoid", sondeHistoryECMWF.getWmoid());
        if (sondeHistoryECMWF.getNrep() != null) params.put("nrep", sondeHistoryECMWF.getNrep());
        if (sondeHistoryECMWF.getNwt() != null) params.put("nwt", sondeHistoryECMWF.getNwt());
        if (sondeHistoryECMWF.getNww() != null) params.put("nww", sondeHistoryECMWF.getNww());
        if (sondeHistoryECMWF.getN99() != null) params.put("n99", sondeHistoryECMWF.getN99());
        if (sondeHistoryECMWF.getLatitude() != null) params.put("latitude", sondeHistoryECMWF.getLatitude());
        if (sondeHistoryECMWF.getLongitude() != null) params.put("longitude", sondeHistoryECMWF.getLongitude());
        if (sondeHistoryECMWF.getRadiosonde_type() != null)
            params.put("radiosonde_type", sondeHistoryECMWF.getRadiosonde_type());
        if (sondeHistoryECMWF.getMaxl() != null) params.put("maxl", sondeHistoryECMWF.getMaxl());
        if (sondeHistoryECMWF.getNdp() != null) params.put("ndp", sondeHistoryECMWF.getNdp());
        if (sondeHistoryECMWF.getNdt() != null) params.put("ndt", sondeHistoryECMWF.getNdt());
        if (sondeHistoryECMWF.getBufr_nrep() != null) params.put("bufr_nrep", sondeHistoryECMWF.getBufr_nrep());
        if (sondeHistoryECMWF.getBufr_nwt() != null) params.put("bufr_nwt", sondeHistoryECMWF.getBufr_nwt());
        if (sondeHistoryECMWF.getBufr_nww() != null) params.put("bufr_nww", sondeHistoryECMWF.getBufr_nww());
        if (sondeHistoryECMWF.getBufr_n99() != null) params.put("bufr_n99", sondeHistoryECMWF.getBufr_n99());
        if (sondeHistoryECMWF.getBufr_nhr() != null) params.put("bufr_nhr", sondeHistoryECMWF.getBufr_nhr());
        if (sondeHistoryECMWF.getBufr_latitude() != null)
            params.put("bufr_latitude", sondeHistoryECMWF.getBufr_latitude());
        if (sondeHistoryECMWF.getBufr_longitude() != null)
            params.put("bufr_longitude", sondeHistoryECMWF.getBufr_longitude());
        if (sondeHistoryECMWF.getBufr_radiosonde_type() != null)
            params.put("bufr_radiosonde_type", sondeHistoryECMWF.getBufr_radiosonde_type());
        if (sondeHistoryECMWF.getBufr_maxl() != null) params.put("bufr_maxl", sondeHistoryECMWF.getBufr_maxl());
        if (sondeHistoryECMWF.getBufr_ndp() != null) params.put("bufr_ndp", sondeHistoryECMWF.getBufr_ndp());
        if (sondeHistoryECMWF.getBufr_ndt() != null) params.put("bufr_ndt", sondeHistoryECMWF.getBufr_ndt());
        if (sondeHistoryECMWF.getNotes() != null) params.put("notes", sondeHistoryECMWF.getNotes());
        if (sondeHistoryECMWF.getDate() != null) params.put("date", sondeHistoryECMWF.getDate());
        if (sondeHistoryECMWF.getVerified() != null) params.put("verified", sondeHistoryECMWF.getVerified());

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
        SondeHistoryECMWF sondeHistoryECMWF = (SondeHistoryECMWF) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();
        if (sondeHistoryECMWF.getId() != null) updateSetStatementList.add("id = ?");
        if (sondeHistoryECMWF.getWmoid() != null) updateSetStatementList.add("wmoid = ?");
        if (sondeHistoryECMWF.getNrep() != null) updateSetStatementList.add("nrep = ?");
        if (sondeHistoryECMWF.getNwt() != null) updateSetStatementList.add("nwt = ?");
        if (sondeHistoryECMWF.getNww() != null) updateSetStatementList.add("nww = ?");
        if (sondeHistoryECMWF.getN99() != null) updateSetStatementList.add("n99 = ?");
        if (sondeHistoryECMWF.getLatitude() != null) updateSetStatementList.add("latitude = ?");
        if (sondeHistoryECMWF.getLongitude() != null) updateSetStatementList.add("longitude = ?");
        if (sondeHistoryECMWF.getRadiosonde_type() != null) updateSetStatementList.add("radiosonde_type = ?");
        if (sondeHistoryECMWF.getMaxl() != null) updateSetStatementList.add("maxl = ?");
        if (sondeHistoryECMWF.getNdp() != null) updateSetStatementList.add("ndp = ?");
        if (sondeHistoryECMWF.getNdt() != null) updateSetStatementList.add("ndt = ?");
        if (sondeHistoryECMWF.getBufr_nrep() != null) updateSetStatementList.add("bufr_nrep = ?");
        if (sondeHistoryECMWF.getBufr_nwt() != null) updateSetStatementList.add("bufr_nwt = ?");
        if (sondeHistoryECMWF.getBufr_nww() != null) updateSetStatementList.add("bufr_nww = ?");
        if (sondeHistoryECMWF.getBufr_n99() != null) updateSetStatementList.add("bufr_n99 = ?");
        if (sondeHistoryECMWF.getBufr_nhr() != null) updateSetStatementList.add("bufr_nhr = ?");
        if (sondeHistoryECMWF.getBufr_latitude() != null) updateSetStatementList.add("bufr_latitude = ?");
        if (sondeHistoryECMWF.getBufr_longitude() != null) updateSetStatementList.add("bufr_longitude = ?");
        if (sondeHistoryECMWF.getBufr_radiosonde_type() != null) updateSetStatementList.add("bufr_radiosonde_type = ?");
        if (sondeHistoryECMWF.getBufr_maxl() != null) updateSetStatementList.add("bufr_maxl = ?");
        if (sondeHistoryECMWF.getBufr_ndp() != null) updateSetStatementList.add("bufr_ndp = ?");
        if (sondeHistoryECMWF.getBufr_ndt() != null) updateSetStatementList.add("bufr_ndt = ?");
        if (sondeHistoryECMWF.getNotes() != null) updateSetStatementList.add("notes = ?");
        if (sondeHistoryECMWF.getDate() != null) updateSetStatementList.add("date = ?");
        if (sondeHistoryECMWF.getVerified() != null) updateSetStatementList.add("verified = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
