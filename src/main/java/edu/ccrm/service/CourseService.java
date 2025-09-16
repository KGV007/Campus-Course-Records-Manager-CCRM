package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class CourseService implements ServiceOperations<Course> {
    private Map<String, Course> courses = new HashMap<>();
    
    @Override
    public void add(Course course) {
        validateNotNull(course);
        courses.put(course.getCode().getCode(), course);
    }
    
    @Override
    public void update(Course course) {
        validateNotNull(course);
        courses.put(course.getCode().getCode(), course);
    }
    
    @Override
    public Course findById(String code) {
        return courses.get(code);
    }
    
    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }
    
    @Override
    public void deactivate(String code) {
        Course course = findById(code);
        if (course != null) {
            course.setStatus(Course.CourseStatus.INACTIVE);
        }
    }
    
    // Specialized filtering methods using Stream API
    public List<Course> filterByDepartment(String department) {
        return courses.values().stream()
            .filter(c -> c.getDepartment().equalsIgnoreCase(department))
            .collect(Collectors.toList());
    }
    
    public List<Course> filterBySemester(Semester semester) {
        return courses.values().stream()
            .filter(c -> c.getSemester() == semester)
            .collect(Collectors.toList());
    }
    
    public List<Course> filterByInstructor(String instructorName) {
        return courses.values().stream()
            .filter(c -> c.getInstructor() != null && 
                        c.getInstructor().getFullName().contains(instructorName))
            .collect(Collectors.toList());
    }
}