package io.muzoo.ssc.webapp.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationLoader {

    // add static method for loading configuration from disk
    // default location is 'config.properties' in the same directory
    public static ConfigurationProperties load() {
        String configFileName = "config.properties";
        try (FileInputStream fileInputStream = new FileInputStream(configFileName)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            // get the property value
            String driverClassName = properties.getProperty("database.driverClassName");
            String connectionUrl = properties.getProperty("database.connectionUrl");
            String username = properties.getProperty("database.username");
            String password = properties.getProperty("database.password");
            // create the configuration
            ConfigurationProperties configurationProperties = new ConfigurationProperties.ConfigurationPropertiesBuilder()
                    .databaseDriverClassName(driverClassName)
                    .databaseConnectionUrl(connectionUrl)
                    .databaseUsername(username)
                    .databasePassword(password)
                    .build();
            return configurationProperties;
        } catch (Exception e) {
            throw new RuntimeException("unable to read config.properties");
        }
    }

}