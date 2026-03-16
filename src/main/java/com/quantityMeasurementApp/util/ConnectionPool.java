package com.quantityMeasurementApp.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConnectionPool {

    private final HikariDataSource dataSource;

    public ConnectionPool(ApplicationConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getDbUrl());
        hikariConfig.setUsername(config.getDbUsername());
        hikariConfig.setPassword(config.getDbPassword());
        hikariConfig.setDriverClassName(config.getDbDriverClassName());
        hikariConfig.setMaximumPoolSize(config.getDbPoolMaxSize());
        hikariConfig.setMinimumIdle(config.getDbPoolMinIdle());
        hikariConfig.setConnectionTimeout(config.getDbPoolConnectionTimeoutMs());
        hikariConfig.setIdleTimeout(config.getDbPoolIdleTimeoutMs());
        hikariConfig.setMaxLifetime(config.getDbPoolMaxLifetimeMs());
        hikariConfig.setPoolName("QuantityMeasurementPool");
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Map<String, Integer> getPoolStatistics() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        HikariPoolMXBean bean = dataSource.getHikariPoolMXBean();
        if (bean == null) {
            stats.put("activeConnections", 0);
            stats.put("idleConnections", 0);
            stats.put("totalConnections", 0);
            stats.put("threadsAwaitingConnection", 0);
            return stats;
        }

        stats.put("activeConnections", bean.getActiveConnections());
        stats.put("idleConnections", bean.getIdleConnections());
        stats.put("totalConnections", bean.getTotalConnections());
        stats.put("threadsAwaitingConnection", bean.getThreadsAwaitingConnection());
        return stats;
    }

    public void close() {
        dataSource.close();
    }
}