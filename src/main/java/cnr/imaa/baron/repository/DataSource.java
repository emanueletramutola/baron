package cnr.imaa.baron.repository;

import cnr.imaa.baron.utils.FileResourceUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private HikariConfig config;
    private HikariDataSource ds;

    public DataSource(FileResourceUtils fileResourceUtils) {
        if (fileResourceUtils != null) {
            final String jdbcUrl = fileResourceUtils.getConfig().get("jdbcUrl");
            final String username = fileResourceUtils.getConfig().get("username");
            final String password = fileResourceUtils.getConfig().get("password");

            config = new HikariConfig();

            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);

            config.setMaximumPoolSize(Integer.parseInt(fileResourceUtils.getConfig().get("maxPoolSize")));

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            ds = new HikariDataSource(config);
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void close() {
        ds.close();
    }
}
