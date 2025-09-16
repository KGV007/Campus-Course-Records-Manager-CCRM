package edu.ccrm.util;

import edu.ccrm.domain.*;
import java.util.*;

public class TranscriptGenerator {
    
    // Inner class for handling transcript formatting logic
    private class TranscriptFormatter {
        private String formatTranscriptHeader(Student student) {
            return String.format(
                "\n======== OFFICIAL TRANSCRIPT ========\n" +
                "Student Name: %s\n" +
                "Registration No: %s\n" +
                "Email: %s\n" +
                "=====================================\n",
                student.getFullName(), student.getRegNo(), student.getEmail()
            );
        }
        
        private String formatEnrollmentLine(Enrollment enrollment) {
            return String.format("%-10s %-30s %2d    %s    %.1f",
                enrollment.getCourse().getCode().getCode(),
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getCredits(),
                enrollment.getGrade() != null ? enrollment.getGrade().getLetter() : "N/A",
                enrollment.getGrade() != null ? enrollment.getGrade().getGradePoint() : 0.0
            );
        }
    }
    
    public String generateTranscript(Student student, List<Enrollment> enrollments, double gpa) {
        TranscriptFormatter formatter = new TranscriptFormatter();
        StringBuilder transcriptBuilder = new StringBuilder();
        
        transcriptBuilder.append(formatter.formatTranscriptHeader(student));
        transcriptBuilder.append("Course     Title                          Credits Grade Points\n");
        transcriptBuilder.append("----------------------------------------------------------------\n");
        
        for (Enrollment enrollment : enrollments) {
            transcriptBuilder.append(formatter.formatEnrollmentLine(enrollment)).append("\n");
        }
        
        transcriptBuilder.append("----------------------------------------------------------------\n");
        transcriptBuilder.append(String.format("Cumulative GPA: %.2f\n", gpa));
        
        return transcriptBuilder.toString();
    }
}