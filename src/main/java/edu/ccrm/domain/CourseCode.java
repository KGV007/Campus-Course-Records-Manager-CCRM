package edu.ccrm.domain;

import java.util.*;

public final class CourseCode {
    private final String code;
    
    public CourseCode(String code) {
        this.code = Objects.requireNonNull(code, "Course code cannot be null!");
    }
    
    public String getCode() {
        return code;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CourseCode)) return false;
        CourseCode other = (CourseCode) obj;
        return code.equals(other.code);
    }
    
    @Override
    public int hashCode() {
        return code.hashCode();
    }
}