package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.Station;
import cnr.imaa.baron.model.WndeqHistory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WndeqHistoryDAO extends BaseObjDAO {
    private final String FIELDS = "begday,beghour,begin,begmonth,begyear,code,enddate,endday,endhour,endmonth,endyear,fullrow,igraid,id";
    private final String TABLE_NAME = "wndeq_history";

    public WndeqHistoryDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new WndeqHistory();
    }

    @Override
    protected Object build(ResultSet rs) {
        WndeqHistory wndeqHistory = new WndeqHistory();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "begday":
                        wndeqHistory.setBegday(rs.getObject("begday") != null ? rs.getInt("begday") : null);
                    case "beghour":
                        wndeqHistory.setBeghour(rs.getObject("beghour") != null ? rs.getInt("beghour") : null);
                    case "begin":
                        wndeqHistory.setBegin(rs.getObject("begin") != null ? rs.getTimestamp("begin").toInstant() : null);
                    case "begmonth":
                        wndeqHistory.setBegmonth(rs.getObject("begmonth") != null ? rs.getInt("begmonth") : null);
                    case "begyear":
                        wndeqHistory.setBegyear(rs.getObject("begyear") != null ? rs.getInt("begyear") : null);
                    case "code":
                        wndeqHistory.setCode(rs.getObject("code") != null ? rs.getInt("code") : null);
                    case "enddate":
                        wndeqHistory.setEnddate(rs.getObject("enddate") != null ? rs.getTimestamp("enddate").toInstant() : null);
                    case "endday":
                        wndeqHistory.setEndday(rs.getObject("endday") != null ? rs.getInt("endday") : null);
                    case "endhour":
                        wndeqHistory.setEndhour(rs.getObject("endhour") != null ? rs.getInt("endhour") : null);
                    case "endmonth":
                        wndeqHistory.setEndmonth(rs.getObject("endmonth") != null ? rs.getInt("endmonth") : null);
                    case "endyear":
                        wndeqHistory.setEndyear(rs.getObject("endyear") != null ? rs.getInt("endyear") : null);
                    case "fullrow":
                        wndeqHistory.setFullrow(rs.getObject("fullrow") != null ? rs.getString("fullrow") : null);
                    case "igraid":
                        if (rs.getObject("igraid") != null) {
                            Station station = new Station();
                            station.setIdStation(rs.getString("igraid"));

                            wndeqHistory.setStation(station);
                        }
                        break;
                    case "id":
                        wndeqHistory.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
                        break;

                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return wndeqHistory;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
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
                case "fullrow":
                case "igraid":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                default:
            }
            throw new RuntimeException("Unexpected column name: '" + key + "'");
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        WndeqHistory wndeqHistory = (WndeqHistory) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (wndeqHistory.getBegday() != null) params.put("begday", wndeqHistory.getBegday());
        if (wndeqHistory.getBeghour() != null) params.put("beghour", wndeqHistory.getBeghour());
        if (wndeqHistory.getBegin() != null) params.put("begin", wndeqHistory.getBegin());
        if (wndeqHistory.getBegmonth() != null) params.put("begmonth", wndeqHistory.getBegmonth());
        if (wndeqHistory.getBegyear() != null) params.put("begyear", wndeqHistory.getBegyear());
        if (wndeqHistory.getCode() != null) params.put("code", wndeqHistory.getCode());
        if (wndeqHistory.getEnddate() != null) params.put("enddate", wndeqHistory.getEnddate());
        if (wndeqHistory.getEndday() != null) params.put("endday", wndeqHistory.getEndday());
        if (wndeqHistory.getEndhour() != null) params.put("endhour", wndeqHistory.getEndhour());
        if (wndeqHistory.getEndmonth() != null) params.put("endmonth", wndeqHistory.getEndmonth());
        if (wndeqHistory.getEndyear() != null) params.put("endyear", wndeqHistory.getEndyear());
        if (wndeqHistory.getFullrow() != null) params.put("fullrow", wndeqHistory.getFullrow());
        if (wndeqHistory.getStation() != null && wndeqHistory.getStation().getIdStation() != null)
            params.put("igraid", wndeqHistory.getStation().getIdStation());
        if (wndeqHistory.getId() != null) params.put("id", wndeqHistory.getId());

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
        WndeqHistory wndeqHistory = (WndeqHistory) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();

        if (wndeqHistory.getBegday() != null) updateSetStatementList.add("begday = ?");
        if (wndeqHistory.getBeghour() != null) updateSetStatementList.add("beghour = ?");
        if (wndeqHistory.getBegin() != null) updateSetStatementList.add("begin = ?");
        if (wndeqHistory.getBegmonth() != null) updateSetStatementList.add("begmonth = ?");
        if (wndeqHistory.getBegyear() != null) updateSetStatementList.add("begyear = ?");
        if (wndeqHistory.getCode() != null) updateSetStatementList.add("code = ?");
        if (wndeqHistory.getEnddate() != null) updateSetStatementList.add("enddate = ?");
        if (wndeqHistory.getEndday() != null) updateSetStatementList.add("endday = ?");
        if (wndeqHistory.getEndhour() != null) updateSetStatementList.add("endhour = ?");
        if (wndeqHistory.getEndmonth() != null) updateSetStatementList.add("endmonth = ?");
        if (wndeqHistory.getEndyear() != null) updateSetStatementList.add("endyear = ?");
        if (wndeqHistory.getFullrow() != null) updateSetStatementList.add("fullrow = ?");
        if (wndeqHistory.getStation() != null && wndeqHistory.getStation().getIdStation() != null)
            updateSetStatementList.add("igraid = ?");
        if (wndeqHistory.getId() != null) updateSetStatementList.add("id = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
