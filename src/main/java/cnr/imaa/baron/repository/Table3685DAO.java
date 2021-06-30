package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.Table3685;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Table3685DAO extends BaseObjDAO {
    private final String FIELDS = "id,tac_code,bufr_code,date_from,date_to,description";
    private final String TABLE_NAME = "table3685";

    public Table3685DAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new Table3685();
    }

    @Override
    protected Object build(ResultSet rs) {
        Table3685 table3685 = new Table3685();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "id":
                        table3685.setId(rs.getObject("id") != null ? rs.getInt("id") : null);
                        break;
                    case "tac_code":
                        table3685.setTac_code(rs.getObject("tac_code") != null ? rs.getInt("tac_code") : null);
                        break;
                    case "bufr_code":
                        table3685.setBufr_code(rs.getObject("bufr_code") != null ? rs.getInt("bufr_code") : null);
                        break;
                    case "date_from":
                        table3685.setDateFrom(rs.getObject("date_from") != null ? rs.getTimestamp("date_from").toInstant() : null);
                        break;
                    case "date_to":
                        table3685.setDateTo(rs.getObject("date_to") != null ? rs.getTimestamp("date_to").toInstant() : null);
                        break;
                    case "description":
                        table3685.setDescription(rs.getObject("description") != null ? rs.getString("description") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return table3685;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "id":
                case "tac_code":
                case "bufr_code":
                    setParameter(pstmt, indexParams, params.get(key), Types.INTEGER);
                    indexParams++;
                    break;
                case "date_from":
                case "date_to":
                    setParameter(pstmt, indexParams, params.get(key), Types.TIMESTAMP_WITH_TIMEZONE);
                    indexParams++;
                    break;
                case "description":
                    setParameter(pstmt, indexParams, params.get(key), Types.VARCHAR);
                    indexParams++;
                    break;
                default:
                    throw new RuntimeException("Unexpected column name: '" + key + "'");
            }
        }
    }

    @Override
    protected LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand) {
        Table3685 table3685 = (Table3685) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (table3685.getId() != null) params.put("id", table3685.getId());
        if (table3685.getTac_code() != null) params.put("tac_code", table3685.getTac_code());
        if (table3685.getBufr_code() != null) params.put("bufr_code", table3685.getBufr_code());
        if (table3685.getDateFrom() != null) params.put("date_from", table3685.getDateFrom());
        if (table3685.getDateTo() != null) params.put("date_to", table3685.getDateTo());
        if (table3685.getDescription() != null) params.put("description", table3685.getDescription());

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
        Table3685 table3685 = (Table3685) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();
        if (table3685.getId() != null) updateSetStatementList.add("id = ?");
        if (table3685.getTac_code() != null) updateSetStatementList.add("tac_code = ?");
        if (table3685.getBufr_code() != null) updateSetStatementList.add("bufr_code = ?");
        if (table3685.getDateFrom() != null) updateSetStatementList.add("date_from = ?");
        if (table3685.getDateTo() != null) updateSetStatementList.add("date_to = ?");
        if (table3685.getDescription() != null) updateSetStatementList.add("description = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
