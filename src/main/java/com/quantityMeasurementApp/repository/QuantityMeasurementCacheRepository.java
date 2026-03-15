package com.quantityMeasurementApp.repository;

import com.quantityMeasurementApp.model.QuantityMeasurementEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static final String FILE_NAME = "quantity_measurement_repo.ser";

    List<QuantityMeasurementEntity> quantityMeasurementEntityCache;

    private static QuantityMeasurementCacheRepository instance;

    private QuantityMeasurementCacheRepository() {
        quantityMeasurementEntityCache = new ArrayList<>();
        loadFromDisk();
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

    private void saveToDisk(QuantityMeasurementEntity entity) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
             AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos)) {
            oos.writeObject(entity);
        } catch (IOException e) {
            System.err.println("Error saving to disk: " + e.getMessage());
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
            System.err.println("Error loading from disk: " + e.getMessage());
        }
    }

    public void clearCache() {
        quantityMeasurementEntityCache.clear();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void main(String[] args) {
        QuantityMeasurementCacheRepository repo = QuantityMeasurementCacheRepository.getInstance();
        System.out.println("Repository initialized with " + repo.getAllMeasurements().size() + " entries");
    }
}