package edu.ccrm.domain;

import java.time.LocalDateTime;

public class Enrollment {
    private Student student;
    private Course course;
    private LocalDateTime enrollmentDate;
    private double marks;
    private Grade grade;
    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
    }
    
    public void recordMarks(double marks) {
        this.marks = marks;
        this.grade = Grade.fromMarks(marks);
    }
    
    // Accessor methods
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public double getMarks() { return marks; }
    public Grade getGrade() { return grade; }
}