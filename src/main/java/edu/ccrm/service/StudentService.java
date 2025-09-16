package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import java.util.*;
import java.util.stream.Collectors;

// Interface defining common operations for all service classes
interface ServiceOperations<T> {
    void add(T item);
    void update(T item);
    T findById(String id);
    List<T> findAll();
    void deactivate(String id);
    
    // Default method - helps avoid code duplication across implementations
    default void validateNotNull(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
    }
}

// Functional interface for search operations
@FunctionalInterface
interface Searchable<T> {
    List<T> search(String criteria);
}

public class StudentService implements ServiceOperations<Student>, Searchable<Student> {
    private Map<String, Student> students = new HashMap<>();
    
    @Override
    public void add(Student student) {
        validateNotNull(student);
        if (students.containsKey(student.getId())) {
            throw new DuplicateStudentException("Student with this ID already exists: " + student.getId());
        }
        students.put(student.getId(), student);
    }
    
    @Override
    public void update(Student student) {
        validateNotNull(student);
        students.put(student.getId(), student);
    }
    
    @Override
    public Student findById(String id) {
        return students.get(id);
    }
    
    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }
    
    @Override
    public void deactivate(String id) {
        Student student = findById(id);
        if (student != null) {
            student.setStatus(Student.StudentStatus.INACTIVE);
        }
    }
    
    @Override
    public List<Student> search(String criteria) {
        // Using lambda expression for filtering students
        return students.values().stream()
            .filter(s -> s.getFullName().toLowerCase().contains(criteria.toLowerCase()) ||
                        s.getRegNo().contains(criteria))
            .collect(Collectors.toList());
    }
    
    // Overloaded method for finding students by status
    public List<Student> findByStatus(Student.StudentStatus status) {
        return students.values().stream()
            .filter(s -> s.getStatus() == status)
            .collect(Collectors.toList());
    }
}