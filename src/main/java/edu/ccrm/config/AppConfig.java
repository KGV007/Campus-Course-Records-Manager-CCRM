package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

// Singleton pattern for application configuration
public class AppConfig {
    private static AppConfig instance;
    private Path dataPath;
    private Path backupPath;
    
    private AppConfig() {
        // Private constructor for Singleton
        this.dataPath = Paths.get("data");
        this.backupPath = Paths.get("backup");
    }
    
    // Thread-safe singleton implementation
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    public Path getDataPath() { return dataPath; }
    public Path getBackupPath() { return backupPath; }
    
    public void setDataPath(String path) {
        this.dataPath = Paths.get(path);
    }
    
    public void setBackupPath(String path) {
        this.backupPath = Paths.get(path);
    }
}