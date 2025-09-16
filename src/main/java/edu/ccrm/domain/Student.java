package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.*;

public class Student extends Person {
    private String regNo;
    private StudentStatus status;
    private List<Enrollment> enrollments;
    private LocalDateTime enrollmentDate;
    private double cumulativeGPA;
    
    public enum StudentStatus {
        ACTIVE, INACTIVE, GRADUATED, SUSPENDED
    }
    
    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email); // Call parent constructor
        this.regNo = regNo;
        this.status = StudentStatus.ACTIVE;
        this.enrollments = new ArrayList<>();
        this.enrollmentDate = LocalDateTime.now();
    }
    
    @Override
    public String getRole() {
        return "Student";
    }
    
    @Override
    public String getDetails() {
        return String.format("Student[ID=%s, RegNo=%s, Name=%s, Status=%s]", 
            id, regNo, fullName, status);
    }
    
    @Override
    public String toString() {
        return getDetails();
    }
    
    // Getter and setter methods
    public String getRegNo() { return regNo; }
    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public void addEnrollment(Enrollment enrollment) { enrollments.add(enrollment); }
    public double getCumulativeGPA() { return cumulativeGPA; }
    public void setCumulativeGPA(double gpa) { this.cumulativeGPA = gpa; }
}