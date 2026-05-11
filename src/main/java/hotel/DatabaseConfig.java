package hotel;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private final EntityManagerFactory entityManagerFactory;

    public DatabaseConfig(String configFilePath) {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(configFilePath)) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Config file not found: " + configFilePath, e);
        }

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Apartment.class);

        configuration.setProperty("hibernate.connection.url", props.getProperty("db.url"));
        configuration.setProperty("hibernate.connection.username", props.getProperty("db.user"));
        configuration.setProperty("hibernate.connection.password", props.getProperty("db.password"));
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "false");

        this.entityManagerFactory = configuration.buildSessionFactory();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}