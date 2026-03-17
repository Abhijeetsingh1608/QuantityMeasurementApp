package com.quantityMeasurementApp.repository;

import com.quantityMeasurementApp.util.ApplicationConfig;
import com.quantityMeasurementApp.util.ConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionPoolTest {

    @AfterEach
    void tearDown() {
        System.clearProperty("db.url");
        System.clearProperty("db.pool.maxSize");
        System.clearProperty("db.pool.minIdle");
        System.clearProperty("db.pool.connectionTimeoutMs");
    }

    @Test
    void testConnectionPool_Initialization() {
        System.setProperty("db.url", "jdbc:h2:mem:qm_pool_init;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        System.setProperty("db.pool.maxSize", "3");
        System.setProperty("db.pool.minIdle", "1");

        ConnectionPool pool = new ConnectionPool(ApplicationConfig.getInstance());
        try {
            var stats = pool.getPoolStatistics();
            assertTrue(stats.containsKey("totalConnections"));
            assertTrue(stats.get("totalConnections") >= 0);
        } finally {
            pool.close();
        }
    }

    @Test
    void testConnectionPool_Acquire_Release() throws SQLException {
        System.setProperty("db.url", "jdbc:h2:mem:qm_pool_acquire;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        System.setProperty("db.pool.maxSize", "2");

        ConnectionPool pool = new ConnectionPool(ApplicationConfig.getInstance());
        try {
            Connection connection = pool.getConnection();
            assertNotNull(connection);
            connection.close();

            var stats = pool.getPoolStatistics();
            assertTrue(stats.get("activeConnections") >= 0);
            assertTrue(stats.get("idleConnections") >= 0);
        } finally {
            pool.close();
        }
    }

    @Test
    void testConnectionPool_AllConnectionsExhausted() throws SQLException {
        System.setProperty("db.url", "jdbc:h2:mem:qm_pool_exhaust;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        System.setProperty("db.pool.maxSize", "1");
        System.setProperty("db.pool.minIdle", "1");
        System.setProperty("db.pool.connectionTimeoutMs", "300");

        ConnectionPool pool = new ConnectionPool(ApplicationConfig.getInstance());
        try (Connection connection1 = pool.getConnection()) {
            assertThrows(SQLException.class, pool::getConnection);
        } finally {
            pool.close();
        }
    }
}