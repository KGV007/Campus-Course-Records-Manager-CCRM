package edu.ccrm.domain;

public enum Grade {
    S("S", 10.0),
    A("A", 9.0),
    B("B", 8.0),
    C("C", 7.0),
    D("D", 6.0),
    E("E", 5.0),
    F("F", 0.0);
    
    private final String letter;
    private final double gradePoint;
    
    // Enum constructor
    Grade(String letter, double gradePoint) {
        this.letter = letter;
        this.gradePoint = gradePoint;
    }
    
    public String getLetter() { return letter; }
    public double getGradePoint() { return gradePoint; }
    
    // Convert numerical marks to letter grade
    public static Grade fromMarks(double marks) {
        if (marks >= 90) return S;
        else if (marks >= 80) return A;
        else if (marks >= 70) return B;
        else if (marks >= 60) return C;
        else if (marks >= 50) return D;
        else if (marks >= 40) return E;
        else return F;
    }
}