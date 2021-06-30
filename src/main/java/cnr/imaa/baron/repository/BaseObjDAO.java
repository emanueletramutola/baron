package cnr.imaa.baron.repository;

import cnr.imaa.baron.model.BaseObj;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class BaseObjDAO {
    final private DataSource dataSource;

    public enum SQL_COMMAND {
        SELECT,
        INSERT,
        UPDATE,
        DELETE
    }

    private final String TABLE_NAME_CHARS_TO_REPLACE = "$$TABLE_NAME$$";
    private final String FIELDS_CHARS_TO_REPLACE = "$$FIELDS$$";
    private final String PARAMS_CHARS_TO_REPLACE = "$$PARAMS$$";
    private final String FILTER_CHARS_TO_REPLACE = "$$FILTER$$";
    private final String UPDATE_SET_STATEMENT_TO_REPLACE = "$$UPDATE_SET$$";

    private final String SQLInsert = "INSERT INTO " + TABLE_NAME_CHARS_TO_REPLACE
            + "(" + FIELDS_CHARS_TO_REPLACE + ") " +
            "VALUES (" + PARAMS_CHARS_TO_REPLACE + ")";

    private final String SQLUpdate = "UPDATE " + TABLE_NAME_CHARS_TO_REPLACE
            + " SET " + UPDATE_SET_STATEMENT_TO_REPLACE
            + " WHERE " + FILTER_CHARS_TO_REPLACE;

    private final String SQLSelect = "SELECT " + FIELDS_CHARS_TO_REPLACE
            + " FROM " + TABLE_NAME_CHARS_TO_REPLACE
            + " WHERE " + FILTER_CHARS_TO_REPLACE;

    public BaseObjDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Object getAll() {
        return get(getParams(getBaseObjFilter(), SQL_COMMAND.SELECT));
    }

    public Object getById(BaseObj obj) {
        BaseObj baseObjFilter = getBaseObjFilter();
        baseObjFilter.setIdPK(obj.getIdPK());

        return get(getParams(baseObjFilter, SQL_COMMAND.SELECT));
    }

    public void save_OLD_VERSION(Object obj, SQL_COMMAND sqlCommand) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        Exception exception = null;

        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);

            LinkedHashMap<String, Object> params = getParams(obj, sqlCommand);

            String sql;
            switch (sqlCommand) {
                case INSERT:
                    sql = getSQLInsert();
                    break;
                case UPDATE:
                    sql = getSQLUpdate(obj, getFilterByPK((BaseObj) obj, params));
                    break;
                default:
                    throw new RuntimeException("Unexpected command in save function:" + sqlCommand.name());
            }

            pstmt = conn.prepareStatement(sql);

            setParameters(pstmt, params);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows <= 0) {
                conn.rollback();
            } else {
                conn.commit();
            }
        } catch (Exception ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException exx) {
                    throw new RuntimeException(exx);
                }
            }

            exception = ex;
        } finally {
            DataSource.close(pstmt);
            DataSource.close(conn);
        }

        if (exception != null)
            throw new RuntimeException(exception);
    }

    public void save(Object obj, SQL_COMMAND sqlCommand) {
        Connection conn = null;
        LinkedHashMap<String, Object> params = null;

        Exception exception = null;

        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);

            if (!(obj instanceof List)) {
                params = getParams(obj, sqlCommand);
            }

            String sql;
            switch (sqlCommand) {
                case INSERT:
                    sql = getSQLInsert();
                    break;
                case UPDATE:
                    sql = getSQLUpdate(obj, getFilterByPK((BaseObj) obj, params));
                    break;
                default:
                    throw new RuntimeException("Unexpected command in save function:" + sqlCommand.name());
            }

            final PreparedStatement pstmt = conn.prepareStatement(sql);

            if (obj instanceof List) {
                for (Object baseObj : (List) obj) {
                    setParameters(pstmt, getParams(baseObj, SQL_COMMAND.INSERT));

                    pstmt.addBatch();
                }
            } else {
                setParameters(pstmt, params);

                pstmt.addBatch();
            }

            pstmt.executeBatch();

            conn.commit();

            DataSource.close(pstmt);
        } catch (Exception ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException exx) {
                    throw new RuntimeException(exx);
                }
            }

            exception = ex;
        } finally {
            DataSource.close(conn);
        }

        if (exception != null)
            throw new RuntimeException(exception);
    }

    protected abstract BaseObj getBaseObjFilter();

    protected abstract Object build(ResultSet rs);

    protected abstract void setParameters(PreparedStatement pstmt, LinkedHashMap<String, Object> params);

    protected abstract LinkedHashMap<String, Object> getParams(Object obj, SQL_COMMAND sqlCommand);

    protected abstract String getFields();

    protected abstract String getTableName();

    protected abstract String getUpdateSetStatement(Object obj);

    protected String getParamsChars() {
        StringBuilder params = new StringBuilder("?");

        for (int i = 1; i < getFields().split(",").length; i++) {
            params.append(",?");
        }

        return params.toString();
    }

    protected String getSQLInsert() {
        return SQLInsert
                .replace(TABLE_NAME_CHARS_TO_REPLACE, getTableName())
                .replace(FIELDS_CHARS_TO_REPLACE, getFields())
                .replace(PARAMS_CHARS_TO_REPLACE, getParamsChars());
    }

    protected String getSQLUpdate(Object obj, String sqlFilter) {
        return SQLUpdate
                .replace(TABLE_NAME_CHARS_TO_REPLACE, getTableName())
                .replace(UPDATE_SET_STATEMENT_TO_REPLACE, getUpdateSetStatement(obj))
                .replace(FILTER_CHARS_TO_REPLACE, sqlFilter);
    }

    protected String getSQLSelect(String sqlFilter) {
        return SQLSelect
                .replace(TABLE_NAME_CHARS_TO_REPLACE, getTableName())
                .replace(FIELDS_CHARS_TO_REPLACE, getFields())
                .replace(FILTER_CHARS_TO_REPLACE, sqlFilter);
    }

    protected List<String> getColumnNames(ResultSet rs) {
        List<String> columnNames = new ArrayList<>();

        try {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int numColumns = resultSetMetaData.getColumnCount();

            for (int i = 1; i <= numColumns; i++) {
                columnNames.add(resultSetMetaData.getColumnName(i));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return columnNames;
    }

    protected void setParameter(PreparedStatement pstmt, int indexParams, Object param, int sqlType) {
        try {
            if (param == null) pstmt.setNull(indexParams, sqlType);
            else {
                switch (sqlType) {
                    case Types.DOUBLE:
                        pstmt.setDouble(indexParams, (Double) param);
                        break;
                    case Types.REAL:
                    case Types.FLOAT:
                        pstmt.setFloat(indexParams, (Float) param);
                        break;
                    case Types.TIMESTAMP_WITH_TIMEZONE:
                        pstmt.setTimestamp(indexParams, Timestamp.from((Instant) param));
                        break;
                    case Types.CHAR:
                        pstmt.setString(indexParams, ((Character) param).toString());
                        break;
                    case Types.VARCHAR:
                        pstmt.setString(indexParams, (String) param);
                        break;
                    case Types.BIGINT:
                        pstmt.setLong(indexParams, (Long) param);
                        break;
                    case Types.INTEGER:
                        pstmt.setInt(indexParams, (Integer) param);
                        break;
                    case Types.ARRAY:
                        pstmt.setArray(indexParams, pstmt.getConnection().createArrayOf("integer", (Integer[]) param));
                        break;
                    default:
                        throw new RuntimeException("Unexpected SQL data type");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected Object get(LinkedHashMap<String, Object> params) {
        List<Object> list = new ArrayList<>();

        try {
            Connection conn = this.dataSource.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(getSQLSelect(getFilter(params)));

            setParameters(pstmt, params);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                list.add(build(resultSet));
            }

            DataSource.close(resultSet);
            DataSource.close(pstmt);
            DataSource.close(conn);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return list;
    }

    private String getFilter(HashMap<String, Object> params) {
        StringBuilder filter = new StringBuilder(" 1 = 1 ");
        for (String key : params.keySet()) {
            filter.append(" AND ").append(key).append(" = ? ");
        }

        return filter.toString();
    }

    private String getFilterByPK(BaseObj obj, LinkedHashMap<String, Object> params) {
        params.put(obj.getIdPKField(), obj.getIdPK());

        return obj.getIdPKField() + " = ?";
    }

    protected DataSource getDataSource() {
        return dataSource;
    }
}
