package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportExportService {
    private static final String CSV_DELIMITER = ",";
    
    // Import student data from CSV file using modern Java NIO.2 and Streams
    public List<Student> importStudents(String fileName) throws IOException {
        Path filePath = AppConfig.getInstance().getDataPath().resolve(fileName);
        
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Import file not found: " + filePath);
        }
        
        // Use try-with-resources and Stream API for efficient file processing
        try (Stream<String> fileLines = Files.lines(filePath)) {
            return fileLines
                .skip(1) // Skip the header row
                .map(this::parseStudentFromCSV)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
    }
    
    private Student parseStudentFromCSV(String csvLine) {
        String[] fields = csvLine.split(CSV_DELIMITER);
        if (fields.length >= 4) {
            return new Student(fields[0], fields[1], fields[2], fields[3]);
        }
        return null;
    }
    
    // Export student data to CSV format
    public void exportStudents(List<Student> students, String fileName) throws IOException {
        Path outputPath = AppConfig.getInstance().getDataPath().resolve(fileName);
        Files.createDirectories(outputPath.getParent());
        
        List<String> csvLines = new ArrayList<>();
        csvLines.add("ID,RegNo,Name,Email,Status");
        
        for (Student s : students) {
            csvLines.add(String.format("%s,%s,%s,%s,%s",
                s.getId(), s.getRegNo(), s.getFullName(), s.getEmail(), s.getStatus()));
        }
        
        Files.write(outputPath, csvLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    // Export course data to CSV format
    public void exportCourses(List<Course> courses, String fileName) throws IOException {
        Path outputPath = AppConfig.getInstance().getDataPath().resolve(fileName);
        Files.createDirectories(outputPath.getParent());
        
        List<String> csvLines = new ArrayList<>();
        csvLines.add("Code,Title,Credits,Department,Semester");
        
        for (Course c : courses) {
            csvLines.add(String.format("%s,%s,%d,%s,%s",
                c.getCode().getCode(), c.getTitle(), c.getCredits(), 
                c.getDepartment(), c.getSemester()));
        }
        
        Files.write(outputPath, csvLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}