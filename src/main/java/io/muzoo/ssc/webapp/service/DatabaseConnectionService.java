package io.muzoo.ssc.webapp.service;

import io.muzoo.ssc.webapp.config.ConfigurationLoader;
import io.muzoo.ssc.webapp.config.ConfigurationProperties;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionService {

    private final HikariDataSource ds;

    /*
     * database connection pool using hikari library
     * the secret and variables are loaded from disk
     * the file config,properties is not committed to git repository
     */
    public DatabaseConnectionService() {
        ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ConfigurationProperties configurationProperties = ConfigurationLoader.load();
        ds.setDriverClassName(configurationProperties.getDatabaseDriverClassName());
        ds.setJdbcUrl(configurationProperties.getDatabaseConnectionUrl());
        ds.addDataSourceProperty("user", configurationProperties.getDatabaseUsername());
        ds.addDataSourceProperty("password", configurationProperties.getDatabasePassword());
        ds.setAutoCommit(false);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
