package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.HarmonizationBreak;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HarmonizationBreakDAO extends BaseObjDAO {
    private final String FIELDS = "idstation,fabio,ecvref,zenref,year,month,day,hour,press";
    private final String TABLE_NAME = "harmonization_break";

    public HarmonizationBreakDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new HarmonizationBreak();
    }

    @Override
    protected Object build(ResultSet rs) {
        HarmonizationBreak harmonizationBreak = new HarmonizationBreak();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        harmonizationBreak.setId(rs.getObject("id") != null ? rs.getLong("id") : null);
                        break;
                    case "idstation":
                        harmonizationBreak.setIdStation(rs.getObject("idstation") != null ? rs.getString("idstation") : null);
                        break;
                    case "fabio":
                        harmonizationBreak.setFabio(rs.getObject("fabio") != null ? rs.getDouble("fabio") : null);
                        break;
                    case "ecvref":
                        harmonizationBreak.setEcvref(rs.getObject("ecvref") != null ? rs.getInt("ecvref") : null);
                        break;
                    case "zenref":
                        harmonizationBreak.setZenref(rs.getObject("zenref") != null ? rs.getInt("zenref") : null);
                        break;
                    case "year":
                        harmonizationBreak.setYear(rs.getObject("year") != null ? rs.getInt("year") : null);
                        break;
                    case "month":
                        harmonizationBreak.setMonth(rs.getObject("month") != null ? rs.getInt("month") : null);
                        break;
                    case "day":
                        harmonizationBreak.setDay(rs.getObject("day") != null ? rs.getInt("day") : null);
                        break;
                    case "hour":
                        harmonizationBreak.setHour(rs.getObject("hour") != null ? rs.getInt("hour") : null);
                        break;
                    case "press":
                        harmonizationBreak.setPress(rs.getObject("press") != null ? rs.getDouble("press") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return harmonizationBreak;
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
                case "idstation":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                case "fabio":
                case "press":
                    setParameter(pstmt, indexParams, params.get(key), Types.DOUBLE);
                    indexParams++;
                    break;
                case "ecvref":
                case "zenref":
                case "year":
                case "month":
                case "day":
                case "hour":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        HarmonizationBreak harmonizationBreak = (HarmonizationBreak) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (harmonizationBreak.getId() != null) params.put("id", harmonizationBreak.getId());
        if (harmonizationBreak.getIdStation() != null) params.put("idstation", harmonizationBreak.getIdStation());
        if (harmonizationBreak.getFabio() != null) params.put("fabio", harmonizationBreak.getFabio());
        if (harmonizationBreak.getEcvref() != null) params.put("ecvref", harmonizationBreak.getEcvref());
        if (harmonizationBreak.getZenref() != null) params.put("zenref", harmonizationBreak.getZenref());
        if (harmonizationBreak.getYear() != null) params.put("year", harmonizationBreak.getYear());
        if (harmonizationBreak.getMonth() != null) params.put("month", harmonizationBreak.getMonth());
        if (harmonizationBreak.getDay() != null) params.put("day", harmonizationBreak.getDay());
        if (harmonizationBreak.getHour() != null) params.put("hour", harmonizationBreak.getHour());
        if (harmonizationBreak.getPress() != null) params.put("press", harmonizationBreak.getPress());

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
        HarmonizationBreak harmonizationBreak = (HarmonizationBreak) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();

        if (harmonizationBreak.getId() != null) updateSetStatementList.add("id = ?");
        if (harmonizationBreak.getIdStation() != null) updateSetStatementList.add("idstation = ?");
        if (harmonizationBreak.getFabio() != null) updateSetStatementList.add("fabio = ?");
        if (harmonizationBreak.getEcvref() != null) updateSetStatementList.add("ecvref = ?");
        if (harmonizationBreak.getZenref() != null) updateSetStatementList.add("zenref = ?");
        if (harmonizationBreak.getYear() != null) updateSetStatementList.add("year = ?");
        if (harmonizationBreak.getMonth() != null) updateSetStatementList.add("month = ?");
        if (harmonizationBreak.getDay() != null) updateSetStatementList.add("day = ?");
        if (harmonizationBreak.getHour() != null) updateSetStatementList.add("hour = ?");
        if (harmonizationBreak.getPress() != null) updateSetStatementList.add("press = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
