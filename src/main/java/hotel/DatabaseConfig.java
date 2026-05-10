package hotel;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private final HikariDataSource dataSource;

    public DatabaseConfig(String configFilePath) {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(configFilePath)) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Config file not found: " + configFilePath, e);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        if (url == null || user == null || password == null) {
            throw new RuntimeException("Missing required db.url, db.user or db.password in config.");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);

        this.dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() { return dataSource; }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}