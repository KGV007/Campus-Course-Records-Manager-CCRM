package edu.ccrm.domain;

import java.util.*;

public class Instructor extends Person {
    private String department;
    private List<Course> courses;
    
    public Instructor(String id, String fullName, String email, String department) {
        super(id, fullName, email);
        this.department = department;
        this.courses = new ArrayList<>();
    }
    
    @Override
    public String getRole() {
        return "Instructor";
    }
    
    @Override
    public String getDetails() {
        return String.format("Instructor[ID=%s, Name=%s, Dept=%s]", 
            id, fullName, department);
    }
    
    public String getDepartment() { return department; }
    public List<Course> getCourses() { return courses; }
    public void addCourse(Course course) { courses.add(course); }
}