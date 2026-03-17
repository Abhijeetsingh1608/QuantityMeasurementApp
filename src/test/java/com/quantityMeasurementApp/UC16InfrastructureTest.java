package com.quantityMeasurementApp;

import com.quantityMeasurementApp.repository.QuantityMeasurementCacheRepository;
import com.quantityMeasurementApp.repository.QuantityMeasurementDatabaseRepository;
import com.quantityMeasurementApp.util.ApplicationConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class UC16InfrastructureTest {

    @Test
    void testPackageStructure_AllLayersPresent() {
        Path base = Path.of("src/main/java/com/quantityMeasurementApp");
        assertTrue(Files.isDirectory(base.resolve("controller")));
        assertTrue(Files.isDirectory(base.resolve("service")));
        assertTrue(Files.isDirectory(base.resolve("repository")));
        assertTrue(Files.isDirectory(base.resolve("model")));
        assertTrue(Files.isDirectory(base.resolve("dto")));
        assertTrue(Files.isDirectory(base.resolve("exception")));
        assertTrue(Files.isDirectory(base.resolve("util")));
    }

    @Test
    void testPomDependencies_JDBCDriversIncluded() throws IOException {
        String pomContent = Files.readString(Path.of("pom.xml"));
        assertTrue(pomContent.contains("<artifactId>h2</artifactId>"));
        assertTrue(pomContent.contains("<artifactId>HikariCP</artifactId>"));
        assertTrue(pomContent.contains("<artifactId>mockito-core</artifactId>"));
        assertTrue(pomContent.contains("<artifactId>junit-jupiter</artifactId>"));
    }

    @Test
    void testPomPlugin_Configuration() throws IOException {
        String pomContent = Files.readString(Path.of("pom.xml"));
        assertTrue(pomContent.contains("<artifactId>maven-surefire-plugin</artifactId>"));
        assertTrue(pomContent.contains("<artifactId>exec-maven-plugin</artifactId>"));
        assertTrue(pomContent.contains("<maven.compiler.source>21</maven.compiler.source>"));
        assertTrue(pomContent.contains("<maven.compiler.target>21</maven.compiler.target>"));
    }

    @Test
    void testDatabaseConfiguration_LoadedFromProperties() {
        ApplicationConfig config = ApplicationConfig.getInstance();
        assertNotNull(config.getDbUrl());
        assertNotNull(config.getDbDriverClassName());
        assertTrue(config.getDbPoolMaxSize() > 0);
    }

    @Test
    void testPropertiesConfiguration_EnvironmentOverride() {
        String previous = System.getProperty("app.repository.type");
        String previousUrl = System.getProperty("db.url");
        String previousDriver = System.getProperty("db.driverClassName");
        String previousUser = System.getProperty("db.username");
        String previousPassword = System.getProperty("db.password");
        try {
            System.setProperty("db.url", "jdbc:h2:mem:qm_repo_override_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
            System.setProperty("db.driverClassName", "org.h2.Driver");
            System.setProperty("db.username", "sa");
            System.setProperty("db.password", "");

            System.setProperty("app.repository.type", "database");
            assertTrue(QuantityMeasurementApp.createRepository() instanceof QuantityMeasurementDatabaseRepository);

            System.setProperty("app.repository.type", "cache");
            assertTrue(QuantityMeasurementApp.createRepository() instanceof QuantityMeasurementCacheRepository);
        } finally {
            if (previous == null) {
                System.clearProperty("app.repository.type");
            } else {
                System.setProperty("app.repository.type", previous);
            }

            if (previousUrl == null) {
                System.clearProperty("db.url");
            } else {
                System.setProperty("db.url", previousUrl);
            }

            if (previousDriver == null) {
                System.clearProperty("db.driverClassName");
            } else {
                System.setProperty("db.driverClassName", previousDriver);
            }

            if (previousUser == null) {
                System.clearProperty("db.username");
            } else {
                System.setProperty("db.username", previousUser);
            }

            if (previousPassword == null) {
                System.clearProperty("db.password");
            } else {
                System.setProperty("db.password", previousPassword);
            }
        }
    }

    @Test
    @Disabled("Manual verification test")
    @EnabledIfSystemProperty(named = "run.maven.command.tests", matches = "true")
    void testMavenBuild_Success() throws IOException, InterruptedException {
        Process process = new ProcessBuilder("mvn", "clean", "compile").redirectErrorStream(true).start();
        int exitCode = process.waitFor();
        assertEquals(0, exitCode);
    }

    @Test
    @Disabled("Manual verification test")
    @EnabledIfSystemProperty(named = "run.maven.command.tests", matches = "true")
    void testMavenTest_AllTestsPass() throws IOException, InterruptedException {
        Process process = new ProcessBuilder("mvn", "clean", "test").redirectErrorStream(true).start();
        int exitCode = process.waitFor();
        assertEquals(0, exitCode);
    }

    @Test
    @Disabled("Manual verification test")
    @EnabledIfSystemProperty(named = "run.maven.command.tests", matches = "true")
    void testMavenClean_RemovesTargetDirectory() throws IOException, InterruptedException {
        new ProcessBuilder("mvn", "clean", "compile").redirectErrorStream(true).start().waitFor();
        assertTrue(Files.exists(Path.of("target")));

        Process cleanProcess = new ProcessBuilder("mvn", "clean").redirectErrorStream(true).start();
        int cleanExitCode = cleanProcess.waitFor();
        assertEquals(0, cleanExitCode);
        assertFalse(Files.exists(Path.of("target")));
    }

    @Test
    @EnabledIfSystemProperty(named = "run.maven.command.tests", matches = "true")
    void testMavenPackage_JarCreated() throws IOException, InterruptedException {
        Process process = new ProcessBuilder("mvn", "clean", "package", "-DskipTests").redirectErrorStream(true).start();
        int exitCode = process.waitFor();
        assertEquals(0, exitCode);
        assertTrue(Files.exists(Path.of("target")));
    }
}