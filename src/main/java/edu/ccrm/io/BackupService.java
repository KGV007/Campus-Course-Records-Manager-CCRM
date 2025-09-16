package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class BackupService {
    
    public void createBackup() throws IOException {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String backupTimestamp = timestamp.format(formatter);
        
        Path sourceDirectory = AppConfig.getInstance().getDataPath();
        Path backupDirectory = AppConfig.getInstance().getBackupPath().resolve("backup_" + backupTimestamp);
        
        Files.createDirectories(backupDirectory);
        
        // Recursively copy all files using NIO.2 Files.walk
        Files.walk(sourceDirectory)
            .forEach(sourcePath -> {
                try {
                    Path targetPath = backupDirectory.resolve(sourceDirectory.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException ex) {
                    System.err.println("Failed to copy file: " + ex.getMessage());
                }
            });
        
        System.out.println("Backup successfully created at: " + backupDirectory);
    }
    
    // Recursive method for calculating total directory size
    public long calculateDirectorySize(Path directory) throws IOException {
        if (Files.isRegularFile(directory)) {
            return Files.size(directory);
        }
        
        // Use try-with-resources for proper resource management
        try (Stream<Path> pathStream = Files.walk(directory)) {
            return pathStream
                .filter(Files::isRegularFile)
                .mapToLong(path -> {
                    try {
                        return Files.size(path);
                    } catch (IOException ex) {
                        return 0L;
                    }
                })
                .sum();
        }
    }
    
    // Recursive directory listing with indentation based on depth
    public void listFilesByDepth(Path directory, int currentDepth) throws IOException {
        String indentation = "  ".repeat(currentDepth);
        
        Files.list(directory).forEach(path -> {
            System.out.println(indentation + path.getFileName());
            if (Files.isDirectory(path)) {
                try {
                    listFilesByDepth(path, currentDepth + 1); // Recursive call with increased depth
                } catch (IOException ex) {
                    System.err.println("Error listing directory contents: " + ex.getMessage());
                }
            }
        });
    }
}