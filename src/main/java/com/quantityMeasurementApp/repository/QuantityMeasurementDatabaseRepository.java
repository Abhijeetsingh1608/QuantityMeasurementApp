package com.quantityMeasurementApp.repository;

import com.quantityMeasurementApp.exception.DatabaseException;
import com.quantityMeasurementApp.model.QuantityMeasurementEntity;
import com.quantityMeasurementApp.util.ApplicationConfig;
import com.quantityMeasurementApp.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);

    private static final String INSERT_SQL = """
            INSERT INTO quantity_measurement_entity (
                this_value, this_unit, this_measurement_type,
                that_value, that_unit, that_measurement_type,
                operation, result_value, result_unit, result_measurement_type,
                result_string, is_error, error_message
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        private static final String SELECT_BASE_SQL = """
            SELECT id, this_value, this_unit, this_measurement_type,
                   that_value, that_unit, that_measurement_type,
                   operation, result_value, result_unit, result_measurement_type,
                   result_string, is_error, error_message, created_at
            FROM quantity_measurement_entity
            """;
        private static final String SELECT_ALL_SQL = SELECT_BASE_SQL + " ORDER BY id";

        private static final String SELECT_BY_OPERATION_SQL = SELECT_BASE_SQL + " WHERE operation = ? ORDER BY id";
        private static final String SELECT_BY_MEASUREMENT_TYPE_SQL = SELECT_BASE_SQL + " WHERE this_measurement_type = ? ORDER BY id";
    private static final String COUNT_SQL = "SELECT COUNT(*) FROM quantity_measurement_entity";
    private static final String DELETE_ALL_HISTORY_SQL = "DELETE FROM quantity_measurement_history";
    private static final String DELETE_ALL_SQL = "DELETE FROM quantity_measurement_entity";

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository() {
        this(new ConnectionPool(ApplicationConfig.getInstance()));
    }

    public QuantityMeasurementDatabaseRepository(ConnectionPool connectionPool) {
        this.connectionPool = Objects.requireNonNull(connectionPool, "connectionPool cannot be null");
        initializeSchema();
        logger.info("Database repository initialized");
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setDouble(1, entity.getThisValue());
            statement.setString(2, entity.getThisUnit());
            statement.setString(3, entity.getThisMeasurementType());

            if (entity.getThatUnit() == null) {
                statement.setNull(4, java.sql.Types.DOUBLE);
                statement.setNull(5, java.sql.Types.VARCHAR);
                statement.setNull(6, java.sql.Types.VARCHAR);
            } else {
                statement.setDouble(4, entity.getThatValue());
                statement.setString(5, entity.getThatUnit());
                statement.setString(6, entity.getThatMeasurementType());
            }

            statement.setString(7, entity.getOperation());

            if (entity.getResultUnit() == null && entity.getResultString() == null) {
                statement.setNull(8, java.sql.Types.DOUBLE);
                statement.setNull(9, java.sql.Types.VARCHAR);
                statement.setNull(10, java.sql.Types.VARCHAR);
            } else {
                statement.setDouble(8, entity.getResultValue());
                statement.setString(9, entity.getResultUnit());
                statement.setString(10, entity.getResultMeasurementType());
            }

            statement.setString(11, entity.getResultString());
            statement.setBoolean(12, entity.isError());
            statement.setString(13, entity.getErrorMessage());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save measurement", e);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return executeQuery(SELECT_ALL_SQL, null);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return executeQuery(SELECT_BY_OPERATION_SQL, statement -> statement.setString(1, operation));
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType) {
        return executeQuery(SELECT_BY_MEASUREMENT_TYPE_SQL, statement -> statement.setString(1, measurementType));
    }

    @Override
    public long getMeasurementCount() {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count measurements", e);
        }
    }

    @Override
    public void deleteAllMeasurements() {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteHistory = connection.prepareStatement(DELETE_ALL_HISTORY_SQL);
                 PreparedStatement deleteAll = connection.prepareStatement(DELETE_ALL_SQL)) {
                deleteHistory.executeUpdate();
                deleteAll.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete measurements", e);
        }
    }

    @Override
    public Map<String, Integer> getPoolStatistics() {
        return connectionPool.getPoolStatistics();
    }

    @Override
    public void releaseResources() {
        connectionPool.close();
    }

    private interface StatementBinder {
        void bind(PreparedStatement statement) throws SQLException;
    }

    private List<QuantityMeasurementEntity> executeQuery(String sql, StatementBinder binder) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (binder != null) {
                binder.bind(statement);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<QuantityMeasurementEntity> results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(mapEntity(resultSet));
                }
                return results;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database query failed", e);
        }
    }

    private QuantityMeasurementEntity mapEntity(ResultSet resultSet) throws SQLException {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setThisValue(resultSet.getDouble("this_value"));
        entity.setThisUnit(resultSet.getString("this_unit"));
        entity.setThisMeasurementType(resultSet.getString("this_measurement_type"));

        double thatValue = resultSet.getDouble("that_value");
        if (!resultSet.wasNull()) {
            entity.setThatValue(thatValue);
        }
        entity.setThatUnit(resultSet.getString("that_unit"));
        entity.setThatMeasurementType(resultSet.getString("that_measurement_type"));

        entity.setOperation(resultSet.getString("operation"));

        double resultValue = resultSet.getDouble("result_value");
        if (!resultSet.wasNull()) {
            entity.setResultValue(resultValue);
        }
        entity.setResultUnit(resultSet.getString("result_unit"));
        entity.setResultMeasurementType(resultSet.getString("result_measurement_type"));
        entity.setResultString(resultSet.getString("result_string"));
        entity.setError(resultSet.getBoolean("is_error"));
        entity.setErrorMessage(resultSet.getString("error_message"));

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            entity.setCreatedAt(createdAt.toLocalDateTime());
        } else {
            entity.setCreatedAt(LocalDateTime.now());
        }

        return entity;
    }

    private void initializeSchema() {
        String schemaFile = resolveSchemaFile();
        String schema = readSchemaScript(schemaFile);
        List<String> statements = splitStatements(schema);

        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to initialize database schema", e);
        }
    }

    private String resolveSchemaFile() {
        String dbUrl = ApplicationConfig.getInstance().getDbUrl();
        if (dbUrl.startsWith("jdbc:postgresql:")) {
            return "db/schema-postgresql.sql";
        }
        return "db/schema.sql";
    }

    private String readSchemaScript(String resourcePath) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new DatabaseException("Schema file not found: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new DatabaseException("Failed to read schema file", e);
        }
    }

    private List<String> splitStatements(String sqlContent) {
        String cleaned = sqlContent.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .filter(line -> !line.startsWith("--"))
                .collect(Collectors.joining("\n"));

        return List.of(cleaned.split(";"))
                .stream()
                .map(String::trim)
                .filter(statement -> !statement.isBlank())
                .toList();
    }
}