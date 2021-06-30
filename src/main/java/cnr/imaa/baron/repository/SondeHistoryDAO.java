package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.SondeHistory;
import cnr.imaa.baron.model.Station;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SondeHistoryDAO extends BaseObjDAO {
    private final String FIELDS = "id,fullrow,igraid,begyear,begmonth,begday,beghour,endyear,endmonth,endday,endhour,code,begin,enddate";
    private final String TABLE_NAME = "sonde_history";

    public SondeHistoryDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new SondeHistory();
    }

    @Override
    protected Object build(ResultSet rs) {
        SondeHistory sondeHistory = new SondeHistory();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "igraid":
                        if (rs.getObject("igraid") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("igraid"));

                            sondeHistory.setStation(station);
                        }
                        break;
                    case "id":
                        sondeHistory.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
                        break;
                    case "fullrow":
                        sondeHistory.setFullrow(rs.getObject("fullrow") != null ? rs.getString("fullrow") : null);
                        break;
                    case "begyear":
                        sondeHistory.setBegyear(rs.getObject("begyear") != null ? rs.getInt("begyear") : null);
                        break;
                    case "begmonth":
                        sondeHistory.setBegmonth(rs.getObject("begmonth") != null ? rs.getInt("begmonth") : null);
                        break;
                    case "begday":
                        sondeHistory.setBegday(rs.getObject("begday") != null ? rs.getInt("begday") : null);
                        break;
                    case "beghour":
                        sondeHistory.setBeghour(rs.getObject("beghour") != null ? rs.getInt("beghour") : null);
                        break;
                    case "endyear":
                        sondeHistory.setEndyear(rs.getObject("endyear") != null ? rs.getInt("endyear") : null);
                        break;
                    case "endmonth":
                        sondeHistory.setEndmonth(rs.getObject("endmonth") != null ? rs.getInt("endmonth") : null);
                        break;
                    case "endday":
                        sondeHistory.setEndday(rs.getObject("endday") != null ? rs.getInt("endday") : null);
                        break;
                    case "endhour":
                        sondeHistory.setEndhour(rs.getObject("endhour") != null ? rs.getInt("endhour") : null);
                        break;
                    case "code":
                        sondeHistory.setCode(rs.getObject("code") != null ? rs.getInt("code") : null);
                        break;
                    case "begin":
                        sondeHistory.setBegin(rs.getObject("begin") != null ? rs.getTimestamp("begin").toInstant() : null);
                        break;
                    case "enddate":
                        sondeHistory.setEnddate(rs.getObject("enddate") != null ? rs.getTimestamp("enddate").toInstant() : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return sondeHistory;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "igraid":
                case "fullrow":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "begday":
                case "beghour":
                case "begmonth":
                case "begyear":
                case "code":
                case "endday":
                case "endhour":
                case "endmonth":
                case "endyear":
                case "id":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "begin":
                case "enddate":
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
        SondeHistory sondeHistory = (SondeHistory) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (sondeHistory.getId() != null) params.put("id", sondeHistory.getId());
        if (sondeHistory.getFullrow() != null) params.put("fullrow", sondeHistory.getFullrow());
        if (sondeHistory.getStation() != null && sondeHistory.getStation().getIdStation() != null)
            params.put("igraid", sondeHistory.getStation().getIdStation());
        if (sondeHistory.getBegyear() != null) params.put("begyear", sondeHistory.getBegyear());
        if (sondeHistory.getBegmonth() != null) params.put("begmonth", sondeHistory.getBegmonth());
        if (sondeHistory.getBegday() != null) params.put("begday", sondeHistory.getBegday());
        if (sondeHistory.getBeghour() != null) params.put("beghour", sondeHistory.getBeghour());
        if (sondeHistory.getEndyear() != null) params.put("endyear", sondeHistory.getEndyear());
        if (sondeHistory.getEndmonth() != null) params.put("endmonth", sondeHistory.getEndmonth());
        if (sondeHistory.getEndday() != null) params.put("endday", sondeHistory.getEndday());
        if (sondeHistory.getEndhour() != null) params.put("endhour", sondeHistory.getEndhour());
        if (sondeHistory.getCode() != null) params.put("code", sondeHistory.getCode());
        if (sondeHistory.getBegin() != null) params.put("begin", sondeHistory.getBegin());
        if (sondeHistory.getEnddate() != null) params.put("enddate", sondeHistory.getEnddate());

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
        SondeHistory sondeHistory = (SondeHistory) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();
        if (sondeHistory.getId() != null) updateSetStatementList.add("id = ?");
        if (sondeHistory.getFullrow() != null) updateSetStatementList.add("fullrow = ?");
        if (sondeHistory.getStation() != null && sondeHistory.getStation().getIdStation() != null)
            updateSetStatementList.add("igraid = ?");
        if (sondeHistory.getBegyear() != null) updateSetStatementList.add("begyear = ?");
        if (sondeHistory.getBegmonth() != null) updateSetStatementList.add("begmonth = ?");
        if (sondeHistory.getBegday() != null) updateSetStatementList.add("begday = ?");
        if (sondeHistory.getBeghour() != null) updateSetStatementList.add("beghour = ?");
        if (sondeHistory.getEndyear() != null) updateSetStatementList.add("endyear = ?");
        if (sondeHistory.getEndmonth() != null) updateSetStatementList.add("endmonth = ?");
        if (sondeHistory.getEndday() != null) updateSetStatementList.add("endday = ?");
        if (sondeHistory.getEndhour() != null) updateSetStatementList.add("endhour = ?");
        if (sondeHistory.getCode() != null) updateSetStatementList.add("code = ?");
        if (sondeHistory.getBegin() != null) updateSetStatementList.add("begin = ?");
        if (sondeHistory.getEnddate() != null) updateSetStatementList.add("enddate = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
