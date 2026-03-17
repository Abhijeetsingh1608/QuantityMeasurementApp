package com.quantityMeasurementApp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public final class ApplicationConfig {

    private static final String CONFIG_FILE = "application.properties";
    private static ApplicationConfig INSTANCE = new ApplicationConfig();

    private final Properties properties = new Properties();

    private ApplicationConfig() {
        loadProperties();
    }

    public static ApplicationConfig getInstance() {
        return INSTANCE;
    }

    public static void reset() {
        INSTANCE = new ApplicationConfig();
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ignored) {
        }
    }

    public String getRepositoryType() {
        return get("app.repository.type", "cache").toLowerCase(Locale.ROOT);
    }

    public String getDbUrl() {
        return get("db.url", "jdbc:h2:mem:quantity_measurement;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
    }

    public String getDbUsername() {
        return get("db.username", "sa");
    }

    public String getDbPassword() {
        return get("db.password", "");
    }

    public String getDbDriverClassName() {
        String explicit = System.getProperty("db.driverClassName");
        if (explicit != null && !explicit.isBlank()) {
            return explicit;
        }

        String configured = properties.getProperty("db.driverClassName");
        String dbUrl = getDbUrl();

        if (dbUrl.startsWith("jdbc:h2:")) {
            return "org.h2.Driver";
        }
        if (dbUrl.startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        }
        if (dbUrl.startsWith("jdbc:mysql:")) {
            return "com.mysql.cj.jdbc.Driver";
        }

        return configured != null && !configured.isBlank() ? configured : "org.h2.Driver";
    }

    public int getDbPoolMaxSize() {
        return getInt("db.pool.maxSize", 10);
    }

    public int getDbPoolMinIdle() {
        return getInt("db.pool.minIdle", 2);
    }

    public long getDbPoolConnectionTimeoutMs() {
        return getLong("db.pool.connectionTimeoutMs", 30000L);
    }

    public long getDbPoolIdleTimeoutMs() {
        return getLong("db.pool.idleTimeoutMs", 600000L);
    }

    public long getDbPoolMaxLifetimeMs() {
        return getLong("db.pool.maxLifetimeMs", 1800000L);
    }

    private String get(String key, String defaultValue) {
        String fromSystem = System.getProperty(key);
        if (fromSystem != null && !fromSystem.isBlank()) {
            return fromSystem;
        }
        return properties.getProperty(key, defaultValue);
    }

    private int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)).trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private long getLong(String key, long defaultValue) {
        try {
            return Long.parseLong(get(key, String.valueOf(defaultValue)).trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}