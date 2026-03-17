package com.quantityMeasurementApp.repository;

import com.quantityMeasurementApp.unit.LengthUnit;
import com.quantityMeasurementApp.exception.DatabaseException;
import com.quantityMeasurementApp.model.QuantityMeasurementEntity;
import com.quantityMeasurementApp.model.QuantityModel;
import com.quantityMeasurementApp.util.ApplicationConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementDatabaseRepositoryTest {

    private QuantityMeasurementDatabaseRepository repository;

    @BeforeEach
    void setUp() {
        System.setProperty("db.url", "jdbc:h2:mem:qm_repo_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        System.setProperty("db.driverClassName", "org.h2.Driver");
        System.setProperty("db.username", "sa");
        System.setProperty("db.password", "");
        ApplicationConfig.reset();

        repository = new QuantityMeasurementDatabaseRepository();
        repository.deleteAllMeasurements();
    }

    @AfterEach
    void tearDown() {
        if (repository != null) {
            repository.releaseResources();
            repository = null;
        }

        System.clearProperty("db.url");
        System.clearProperty("db.driverClassName");
        System.clearProperty("db.username");
        System.clearProperty("db.password");
        ApplicationConfig.reset();
    }

    @Test
    void shouldSaveAndFetchMeasurements() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "COMPARE",
                "Equal"
        );

        repository.save(entity);

        assertEquals(1, repository.getMeasurementCount());
        assertEquals(1, repository.getAllMeasurements().size());
    }

    @Test
    void shouldFilterByOperationAndMeasurementType() {
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "ADD",
                new QuantityModel<>(2.0, LengthUnit.FEET)
        ));
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "COMPARE",
                "Equal"
        ));

        assertEquals(1, repository.getMeasurementsByOperation("ADD").size());
        assertEquals(2, repository.getMeasurementsByMeasurementType("LengthUnit").size());
    }

    @Test
    void shouldDeleteAllMeasurements() {
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "COMPARE",
                "Equal"
        ));

        repository.deleteAllMeasurements();

        assertEquals(0, repository.getMeasurementCount());
        assertTrue(repository.getAllMeasurements().isEmpty());
    }

    @Test
    void testRepositoryMethods_AliasCompatibility() {
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "ADD",
                new QuantityModel<>(2.0, LengthUnit.FEET)
        ));

        assertEquals(1, repository.getMeasurementsByMeasurementType("LengthUnit").size());
        assertEquals(1, repository.getMeasurementCount());
        repository.deleteAllMeasurements();
        assertEquals(0, repository.getMeasurementCount());
    }

    @Test
    void testH2TestDatabase_IsolationBetweenTests() {
        assertEquals(0, repository.getMeasurementCount());
    }

    @Test
    void testSQLInjectionPrevention() {
        String injectedValue = "ADD' OR '1'='1";
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                injectedValue,
                new QuantityModel<>(2.0, LengthUnit.FEET)
        ));
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "COMPARE",
                "Equal"
        ));

        List<QuantityMeasurementEntity> matches = repository.getMeasurementsByOperation(injectedValue);
        assertEquals(1, matches.size());
        assertEquals(injectedValue, matches.get(0).getOperation());
    }

    @Test
    void testTransactionRollback_OnError() {
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "COMPARE",
                "Equal"
        ));

        QuantityMeasurementEntity invalid = new QuantityMeasurementEntity();
        invalid.setThisValue(1.0);
        invalid.setThisUnit(null);
        invalid.setThisMeasurementType("LengthUnit");
        invalid.setOperation("ADD");

        assertThrows(DatabaseException.class, () -> repository.save(invalid));
        assertEquals(1, repository.getMeasurementCount());
    }

    @Test
    void testDatabaseRepositoryPoolStatistics() {
        var stats = repository.getPoolStatistics();
        assertTrue(stats.containsKey("activeConnections"));
        assertTrue(stats.containsKey("idleConnections"));
        assertTrue(stats.containsKey("totalConnections"));
        assertEquals(0, stats.get("activeConnections"));
    }

    @Test
    void testParameterizedQuery_DateTimeHandling() {
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "COMPARE",
                "Equal"
        ));

        QuantityMeasurementEntity stored = repository.getAllMeasurements().get(0);
        assertNotNull(stored.getCreatedAt());
    }

    @Test
    void testDatabaseRepository_LargeDataSet() {
        for (int index = 0; index < 1000; index++) {
            repository.save(new QuantityMeasurementEntity(
                    new QuantityModel<>(index + 1.0, LengthUnit.FEET),
                    new QuantityModel<>(12.0, LengthUnit.INCHES),
                    "ADD",
                    new QuantityModel<>(index + 2.0, LengthUnit.FEET)
            ));
        }

        assertEquals(1000, repository.getMeasurementCount());
        assertEquals(1000, repository.getMeasurementsByOperation("ADD").size());
    }

    @Test
    void testDatabaseRepository_ConcurrentAccess() throws InterruptedException {
        int workers = 8;
        int perWorker = 20;
        CountDownLatch latch = new CountDownLatch(workers);
        ExecutorService executorService = Executors.newFixedThreadPool(workers);

        for (int worker = 0; worker < workers; worker++) {
            executorService.submit(() -> {
                try {
                    for (int i = 0; i < perWorker; i++) {
                        repository.save(new QuantityMeasurementEntity(
                                new QuantityModel<>(1.0, LengthUnit.FEET),
                                new QuantityModel<>(12.0, LengthUnit.INCHES),
                                "COMPARE",
                                "Equal"
                        ));
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(30, TimeUnit.SECONDS));
        assertEquals(workers * perWorker, repository.getMeasurementCount());
    }

    @Test
    void testDataPersistence_AcrossRepositoryRestart() throws Exception {
        if (repository != null) {
            repository.releaseResources();
            repository = null;
        }

        System.clearProperty("db.url");
        System.clearProperty("db.driverClassName");
        System.clearProperty("db.username");
        System.clearProperty("db.password");

        String uniqueDbName = "qm_repo_persist_test_" + System.nanoTime();
        String dbPath = Path.of("target", uniqueDbName).toAbsolutePath().toString().replace("\\", "/");

        String fileUrl = "jdbc:h2:file:" + dbPath
                + ";MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_ON_EXIT=FALSE";

        System.setProperty("db.url", fileUrl);
        System.setProperty("db.driverClassName", "org.h2.Driver");
        System.setProperty("db.username", "sa");
        System.setProperty("db.password", "");
        ApplicationConfig.reset();

        QuantityMeasurementDatabaseRepository repository1 = new QuantityMeasurementDatabaseRepository();
        repository1.deleteAllMeasurements();

        repository1.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "COMPARE",
                "Equal"
        ));

        assertEquals(1, repository1.getMeasurementCount());
        repository1.releaseResources();

        ApplicationConfig.reset();

        QuantityMeasurementDatabaseRepository repository2 = new QuantityMeasurementDatabaseRepository();
        System.out.println(repository2.getMeasurementCount());
        assertEquals(1, repository2.getMeasurementCount());
        assertEquals(1, repository2.getAllMeasurements().size());

        repository2.deleteAllMeasurements();
        repository2.releaseResources();

        cleanupH2Files(dbPath);
    }

    @Test
    void testDatabaseSchema_TablesCreated() throws SQLException {
        repository.save(new QuantityMeasurementEntity(
                new QuantityModel<>(1.0, LengthUnit.FEET),
                new QuantityModel<>(12.0, LengthUnit.INCHES),
                "COMPARE",
                "Equal"
        ));

        String url = System.getProperty("db.url");
        ApplicationConfig.reset();
        ApplicationConfig config = ApplicationConfig.getInstance();

        try (var connection = java.sql.DriverManager.getConnection(url, config.getDbUsername(), config.getDbPassword());
             var statement = connection.createStatement()) {
            var tables = new ArrayList<String>();
            try (var rs = statement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'")) {
                while (rs.next()) {
                    tables.add(rs.getString(1));
                }
            }

            assertTrue(tables.contains("QUANTITY_MEASUREMENT_ENTITY") || tables.contains("quantity_measurement_entity"));
            assertTrue(tables.contains("QUANTITY_MEASUREMENT_HISTORY") || tables.contains("quantity_measurement_history"));
        }
    }

    private void cleanupH2Files(String dbPath) throws IOException {
        Files.deleteIfExists(Path.of(dbPath + ".mv.db"));
        Files.deleteIfExists(Path.of(dbPath + ".trace.db"));
    }
}