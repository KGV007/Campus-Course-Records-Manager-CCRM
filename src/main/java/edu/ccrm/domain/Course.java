package edu.ccrm.domain;

public class Course {
    private CourseCode code;
    private String title;
    private int credits;
    private Instructor instructor;
    private Semester semester;
    private String department;
    private CourseStatus status;
    
    public enum CourseStatus {
        ACTIVE, INACTIVE, ARCHIVED
    }
    
    // Private constructor - only accessible through Builder
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
        this.status = builder.status;
    }
    
    // Builder pattern implementation
    public static class Builder {
        private CourseCode code;
        private String title;
        private int credits;
        private Instructor instructor;
        private Semester semester;
        private String department;
        private CourseStatus status = CourseStatus.ACTIVE; // default value
        
        public Builder(CourseCode code, String title) {
            this.code = code;
            this.title = title;
        }
        
        public Builder credits(int credits) {
            this.credits = credits;
            return this;
        }
        
        public Builder instructor(Instructor instructor) {
            this.instructor = instructor;
            return this;
        }
        
        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }
        
        public Builder department(String department) {
            this.department = department;
            return this;
        }
        
        public Builder status(CourseStatus status) {
            this.status = status;
            return this;
        }
        
        public Course build() {
            return new Course(this);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Course[%s: %s, Credits=%d, Semester=%s]", 
            code.getCode(), title, credits, semester);
    }
    
    // Public getter methods
    public CourseCode getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public Instructor getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
}