package edu.ccrm.domain;

import java.time.LocalDate;

// Abstract class - cannot be instantiated directly
public abstract class Person {
    protected String id;
    protected String fullName;
    protected String email;
    protected LocalDate dateOfBirth;
    
    public Person(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }
    
    // Abstract methods - subclasses must implement these
    public abstract String getRole();
    public abstract String getDetails();
    
    // Common getter/setter methods for all Person types
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}