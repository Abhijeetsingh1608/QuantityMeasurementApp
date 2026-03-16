package com.quantityMeasurementApp.repository;

import com.quantityMeasurementApp.model.QuantityMeasurementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class AppendableObjectOutputStream extends ObjectOutputStream {

    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        File file = new File(QuantityMeasurementCacheRepository.FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            super.writeStreamHeader();
        } else {
            reset();
        }
    }
}

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementCacheRepository.class);

    public static final String FILE_NAME = "quantity_measurement_repo.ser";

    List<QuantityMeasurementEntity> quantityMeasurementEntityCache;

    private static QuantityMeasurementCacheRepository instance;

    private QuantityMeasurementCacheRepository() {
        quantityMeasurementEntityCache = new ArrayList<>();
        loadFromDisk();
        logger.info("Cache repository initialized with {} entries", quantityMeasurementEntityCache.size());
    }

    public static QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        quantityMeasurementEntityCache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return new ArrayList<>(quantityMeasurementEntityCache);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        if (operation == null) {
            return List.of();
        }
        return quantityMeasurementEntityCache.stream()
                .filter(entity -> operation.equalsIgnoreCase(entity.getOperation()))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType) {
        if (measurementType == null) {
            return List.of();
        }
        return quantityMeasurementEntityCache.stream()
                .filter(entity -> measurementType.equalsIgnoreCase(entity.getThisMeasurementType()))
                .collect(Collectors.toList());
    }

    @Override
    public long getMeasurementCount() {
        return quantityMeasurementEntityCache.size();
    }

    @Override
    public void deleteAllMeasurements() {
        clearCache();
    }

    @Override
    public Map<String, Integer> getPoolStatistics() {
        return Map.of();
    }

    private void saveToDisk(QuantityMeasurementEntity entity) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
             AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos)) {
            oos.writeObject(entity);
        } catch (IOException e) {
            logger.error("Error saving to disk", e);
        }
    }

    private void loadFromDisk() {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    QuantityMeasurementEntity entity = (QuantityMeasurementEntity) ois.readObject();
                    quantityMeasurementEntityCache.add(entity);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.warn("Error loading from disk: {}", e.getMessage());
        }
    }

    public void clearCache() {
        quantityMeasurementEntityCache.clear();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            if (!file.delete()) {
                logger.warn("Could not delete cache file {}", FILE_NAME);
            }
        }
    }

    @Override
    public void releaseResources() {
    }

    public static void main(String[] args) {
        QuantityMeasurementCacheRepository repo = QuantityMeasurementCacheRepository.getInstance();
        System.out.println("Repository initialized with " + repo.getAllMeasurements().size() + " entries");
    }
}