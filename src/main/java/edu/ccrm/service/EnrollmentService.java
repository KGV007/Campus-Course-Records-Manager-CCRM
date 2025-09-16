package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentService {
    private static final int MAX_CREDITS_PER_SEMESTER = 24;
    private List<Enrollment> enrollments = new ArrayList<>();
    
    public void enroll(Student student, Course course) throws MaxCreditLimitExceededException {
        // Check if student would exceed credit limit
        int currentCredits = getCurrentCredits(student, course.getSemester());
        if (currentCredits + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException(
                "Student cannot exceed " + MAX_CREDITS_PER_SEMESTER + " credits per semester");
        }
        
        // Verify student isn't already enrolled in this course
        boolean alreadyEnrolled = enrollments.stream()
            .anyMatch(e -> e.getStudent().equals(student) && e.getCourse().equals(course));
        
        if (alreadyEnrolled) {
            throw new DuplicateEnrollmentException("Student is already enrolled in this course");
        }
        
        Enrollment newEnrollment = new Enrollment(student, course);
        enrollments.add(newEnrollment);
        student.addEnrollment(newEnrollment);
    }
    
    public void unenroll(Student student, Course course) {
        enrollments.removeIf(e -> e.getStudent().equals(student) && 
                                  e.getCourse().equals(course));
    }
    
    public void recordGrade(Student student, Course course, double marks) {
        Enrollment enrollment = findEnrollment(student, course);
        if (enrollment != null) {
            enrollment.recordMarks(marks);
        }
    }
    
    private Enrollment findEnrollment(Student student, Course course) {
        return enrollments.stream()
            .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
            .findFirst()
            .orElse(null);
    }
    
    private int getCurrentCredits(Student student, Semester semester) {
        return enrollments.stream()
            .filter(e -> e.getStudent().equals(student) && 
                        e.getCourse().getSemester() == semester)
            .mapToInt(e -> e.getCourse().getCredits())
            .sum();
    }
    
    public double calculateGPA(Student student) {
        List<Enrollment> gradedEnrollments = enrollments.stream()
            .filter(e -> e.getStudent().equals(student) && e.getGrade() != null)
            .collect(Collectors.toList());
        
        if (gradedEnrollments.isEmpty()) return 0.0;
        
        double totalGradePoints = 0;
        int totalCredits = 0;
        
        for (Enrollment e : gradedEnrollments) {
            totalGradePoints += e.getGrade().getGradePoint() * e.getCourse().getCredits();
            totalCredits += e.getCourse().getCredits();
        }
        
        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }
    
    public List<Enrollment> getStudentEnrollments(Student student) {
        return enrollments.stream()
            .filter(e -> e.getStudent().equals(student))
            .collect(Collectors.toList());
    }
}