package edu.ccrm.util;

import edu.ccrm.domain.*;
import java.util.*;

public class ComparatorUtils {
    // Lambda expression-based comparators for different sorting needs
    public static final Comparator<Student> BY_NAME = 
        (student1, student2) -> student1.getFullName().compareTo(student2.getFullName());
    
    public static final Comparator<Student> BY_REG_NO = 
        (student1, student2) -> student1.getRegNo().compareTo(student2.getRegNo());
    
    public static final Comparator<Course> BY_CREDITS = 
        (course1, course2) -> Integer.compare(course1.getCredits(), course2.getCredits());
    
    // Method reference for cleaner code
    public static final Comparator<Course> BY_TITLE = 
        Comparator.comparing(Course::getTitle);
}