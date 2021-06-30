package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;
import cnr.imaa.baron.model.GuanDataHeaderSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GuanDataHeaderSourceDAO extends BaseObjDAO {
    private final String FIELDS = "code,value";
    private final String TABLE_NAME = "guan_data_header_source";

    public GuanDataHeaderSourceDAO(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected BaseObj getBaseObjFilter() {
        return new GuanDataHeaderSource();
    }

    @Override
    protected Object build(ResultSet rs) {
        GuanDataHeaderSource guanDataHeaderSource = new GuanDataHeaderSource();

        try {
            List<String> columnNames = getColumnNames(rs);

            for (String columnName : columnNames) {
                switch (columnName) {
                    case "code":
                        guanDataHeaderSource.setCode(rs.getObject("code") != null ? rs.getString("code") : null);
                        break;
                    case "value":
                        guanDataHeaderSource.setValue(rs.getObject("value") != null ? rs.getString("value") : null);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return guanDataHeaderSource;
    }

    @Override
    protected void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params) {
        int indexParams = 1;

        for (String key : params.keySet()) {
            switch (key) {
                case "code":
                case "value":
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
        GuanDataHeaderSource guanDataHeaderSource = (GuanDataHeaderSource) obj;

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        if (guanDataHeaderSource.getCode() != null) params.put("code", guanDataHeaderSource.getCode());
        if (guanDataHeaderSource.getValue() != null) params.put("value", guanDataHeaderSource.getValue());

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
        GuanDataHeaderSource guanDataHeaderSource = (GuanDataHeaderSource) obj;
        StringBuilder updateSetStatement = new StringBuilder();

        List<String> updateSetStatementList = new ArrayList<>();
        if (guanDataHeaderSource.getCode() != null) updateSetStatementList.add("code = ?");
        if (guanDataHeaderSource.getValue() != null) updateSetStatementList.add("value = ?");

        for (int i = 0; i < updateSetStatementList.size(); i++) {
            if (i > 0)
                updateSetStatement.append(",");

            updateSetStatement.append(updateSetStatementList.get(i));
        }

        return updateSetStatement.toString();
    }
}
