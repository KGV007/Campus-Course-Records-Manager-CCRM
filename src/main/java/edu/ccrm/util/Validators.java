package edu.ccrm.util;

import edu.ccrm.domain.*;

public class Validators {
    // Regular expression for email validation
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    // Registration number format validation (e.g., CS202401)
    public static boolean isValidRegNo(String regNo) {
        return regNo != null && regNo.matches("^[A-Z]{2}\\d{6}$");
    }
    
    // Using assertions for runtime validation during development/testing
    public static void validateStudent(Student student) {
        assert student != null : "Student object cannot be null";
        assert student.getId() != null : "Student ID is required";
        assert student.getRegNo() != null : "Registration number is required";
    }
}