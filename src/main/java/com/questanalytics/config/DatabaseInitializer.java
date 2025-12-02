package com.questanalytics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final Environment environment;

    public DatabaseInitializer(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        try {
            String datasourceUrl = environment.getProperty("spring.datasource.url");
            String username = environment.getProperty("spring.datasource.username", "postgres");
            String password = environment.getProperty("spring.datasource.password", "postgres");
            
            if (datasourceUrl == null || !datasourceUrl.contains("postgresql")) {
                logger.debug("Skipping database initialization - not using PostgreSQL");
                return;
            }
            
            String databaseName = extractDatabaseName(datasourceUrl);
            String baseUrl = datasourceUrl.substring(0, datasourceUrl.lastIndexOf('/'));
            
            logger.info("Checking if database '{}' exists...", databaseName);
            
            String postgresUrl = baseUrl + "/postgres";
            try (Connection connection = DriverManager.getConnection(postgresUrl, username, password);
                 Statement statement = connection.createStatement()) {
                
                String checkDbQuery = "SELECT 1 FROM pg_database WHERE datname = '" + databaseName + "'";
                ResultSet resultSet = statement.executeQuery(checkDbQuery);
                
                if (!resultSet.next()) {
                    logger.info("Database '{}' does not exist. Creating it...", databaseName);
                    statement.executeUpdate("CREATE DATABASE " + databaseName);
                    logger.info("✓ Database '{}' created successfully!", databaseName);
                } else {
                    logger.info("✓ Database '{}' already exists.", databaseName);
                }
            }
        } catch (Exception e) {
            logger.warn("⚠ Could not auto-create database. This is OK if the database already exists.");
            logger.warn("   If you see connection errors, please create the database manually:");
            logger.warn("   CREATE DATABASE quest_analytics_db;");
            logger.debug("Error details: {}", e.getMessage());
        }
    }

    private String extractDatabaseName(String url) {
        int lastSlash = url.lastIndexOf('/');
        int questionMark = url.indexOf('?', lastSlash);
        
        if (questionMark != -1) {
            return url.substring(lastSlash + 1, questionMark);
        }
        return url.substring(lastSlash + 1);
    }
}

