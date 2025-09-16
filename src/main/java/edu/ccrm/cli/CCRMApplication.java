package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.exception.*;
import edu.ccrm.io.*;
import edu.ccrm.service.*;
import edu.ccrm.util.*;
import java.io.IOException;
import java.util.*;

public class CCRMApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static StudentService studentService = new StudentService();
    private static CourseService courseService = new CourseService();
    private static EnrollmentService enrollmentService = new EnrollmentService();
    private static ImportExportService ioService = new ImportExportService();
    private static BackupService backupService = new BackupService();
    
    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();
        // Welcome message - keeping it friendly for users
        System.out.println("Campus Course and Records Manager(CCRM)");
        System.out.println("Built with Java SE");
        
        // Let's get some sample data loaded first
        loadInitialData();
        
        boolean keepRunning = true;
        
        // Main program loop - keep going until user wants to exit
        while (keepRunning) {
            showMainMenu();
            int userChoice = getUserInputAsInt();
            
            // Handle user's menu choice
            if (userChoice == 1) {
                handleStudentManagement();
            } else if (userChoice == 2) {
                handleCourseManagement();
            } else if (userChoice == 3) {
                handleEnrollmentManagement();
            } else if (userChoice == 4) {
                handleGradeManagement();
            } else if (userChoice == 5) {
                handleImportExportOperations();
            } else if (userChoice == 6) {
                handleBackupOperations();
            } else if (userChoice == 7) {
                handleReportGeneration();
            } else if (userChoice == 8) {
                System.out.println("Thanks for using CCRM. Goodbye!");
                keepRunning = false;
            } else {
                System.out.println("That's not a valid option. Please try again.");
            }
            
            // Skip iteration if input was invalid
            if (userChoice == 0) {
                continue; 
            }
        }
        
        scanner.close();
    }
    
    private static void showMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Grade Management");
        System.out.println("5. Data Import/Export");
        System.out.println("6. Backup & Restore");
        System.out.println("7. Reports & Analytics");
        System.out.println("8. Exit Application");
        System.out.print("Please select an option: ");
    }
    
    private static void handleStudentManagement() {
        boolean stayInMenu = true;
        
        while (stayInMenu) {
            System.out.println("\n--- Student Management Portal ---");
            System.out.println("1. Register New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Edit Student Information");
            System.out.println("4. Find Student");
            System.out.println("5. Deactivate Student");
            System.out.println("6. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = getUserInputAsInt();
            
            switch (choice) {
                case 1:
                    registerNewStudent();
                    break;
                case 2:
                    showAllStudents();
                    break;
                case 3:
                    editStudentInfo();
                    break;
                case 4:
                    findStudent();
                    break;
                case 5:
                    deactivateStudent();
                    break;
                case 6:
                    stayInMenu = false;
                    break;
                default:
                    System.out.println("Invalid selection!");
            }
        }
    }
    
    private static void registerNewStudent() {
        System.out.println("\n--- New Student Registration ---");
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Registration Number (format: CS202401): ");
        String regNumber = scanner.nextLine();
        System.out.print("Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Email Address: ");
        String emailAddr = scanner.nextLine();
        
        // Validate email format first
        if (!Validators.isValidEmail(emailAddr)) {
            System.out.println("Please enter a valid email address!");
            return;
        }
        
        // Then validate registration number
        if (!Validators.isValidRegNo(regNumber)) {
            System.out.println("Registration number format is incorrect!");
            return;
        }
        
        try {
            Student newStudent = new Student(studentId, regNumber, name, emailAddr);
            studentService.add(newStudent);
            System.out.println("Student registered successfully!");
        } catch (DuplicateStudentException ex) {
            System.err.println("Registration failed: " + ex.getMessage());
        }
    }
    
    private static void showAllStudents() {
        List<Student> allStudents = studentService.findAll();
        
        if (allStudents.size() == 0) {
            System.out.println("No students are currently registered.");
            return;
        }
        
        // Sort students by name for better readability
        allStudents.sort(ComparatorUtils.BY_NAME);
        
        System.out.println("\n--- Student Directory ---");
        for (Student s : allStudents) {
            System.out.println(s.toString());
        }
    
        // Show some basic statistics
        long activeStudentCount = allStudents.stream()
            .filter(student -> student.getStatus() == Student.StudentStatus.ACTIVE)
            .count();
        System.out.println("\nSummary: " + allStudents.size() + " total students, " + 
                          activeStudentCount + " currently active");
    }
    
    private static void editStudentInfo() {
        System.out.print("Enter the Student ID you want to update: ");
        String studentId = scanner.nextLine();
        
        Student existingStudent = studentService.findById(studentId);
        if (existingStudent == null) {
            System.out.println("Could not find a student with that ID!");
            return;
        }
        
        System.out.println("Current info: " + existingStudent);
        System.out.print("New email address (or press Enter to keep current): ");
        String updatedEmail = scanner.nextLine();
        
        if (updatedEmail.trim().length() > 0) {
            existingStudent.setEmail(updatedEmail);
            studentService.update(existingStudent);
            System.out.println("Student information updated!");
        }
    }
    
    private static void findStudent() {
        System.out.print("Search by name or registration number: ");
        String searchTerm = scanner.nextLine();
        
        List<Student> searchResults = studentService.search(searchTerm);
        
        if (searchResults.isEmpty()) {
            System.out.println("No students found matching: " + searchTerm);
        } else {
            System.out.println("\n--- Search Results ---");
            // Using method reference here feels natural
            searchResults.forEach(System.out::println);
        }
    }
    
    private static void deactivateStudent() {
        System.out.print("Enter Student ID to deactivate: ");
        String studentId = scanner.nextLine();
        
        studentService.deactivate(studentId);
        System.out.println("Student has been deactivated.");
    }
    
    private static void handleCourseManagement() {
        System.out.println("\n--- Course Management ---");
        System.out.println("1. Add New Course");
        System.out.println("2. View All Courses");
        System.out.println("3. Filter Courses");
        System.out.println("4. Back to Main Menu");
        System.out.print("Your choice: ");
        
        int selection = getUserInputAsInt();
        
        switch (selection) {
            case 1:
                addNewCourse();
                break;
            case 2:
                displayAllCourses();
                break;
            case 3:
                filterExistingCourses();
                break;
            default:
                break;
        }
    }
    
    private static void addNewCourse() {
        System.out.println("\n--- Course Creation ---");
        System.out.print("Course Code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Course Title: ");
        String courseTitle = scanner.nextLine();
        System.out.print("Credit Hours: ");
        int creditHours = getUserInputAsInt();
        System.out.print("Department: ");
        String dept = scanner.nextLine();
        
        System.out.println("Available Semesters:");
        // Show available semester options
        Semester[] semesters = Semester.values();
        for (int i = 0; i < semesters.length; i++) {
            System.out.println((i + 1) + ". " + semesters[i].getDisplayName());
        }
        int semesterChoice = getUserInputAsInt();
        Semester selectedSemester = semesters[semesterChoice - 1];
        
        // Using builder pattern here
        Course newCourse = new Course.Builder(new CourseCode(courseCode), courseTitle)
            .credits(creditHours)
            .department(dept)
            .semester(selectedSemester)
            .build();
        
        courseService.add(newCourse);
        System.out.println("Course created successfully!");
    }
    
    private static void displayAllCourses() {
        List<Course> courseList = courseService.findAll();
        
        if (courseList.isEmpty()) {
            System.out.println("No courses available yet.");
            return;
        }
        
        // Sort by credits for easier viewing
        courseList.sort(ComparatorUtils.BY_CREDITS);
        
        System.out.println("\n--- Course Catalog ---");
        courseList.forEach(System.out::println);
        
        // Calculate some quick stats
        int totalCreditHours = courseList.stream()
            .mapToInt(Course::getCredits)
            .sum();
        System.out.println("\nCatalog contains: " + courseList.size() + 
                          " courses, totaling " + totalCreditHours + " credit hours");
    }
    
    private static void filterExistingCourses() {
        System.out.println("\n--- Course Filtering ---");
        System.out.println("1. Filter by Department");
        System.out.println("2. Filter by Semester");
        System.out.println("3. Filter by Instructor");
        System.out.print("Filter type: ");
        
        int filterType = getUserInputAsInt();
        List<Course> filteredResults = new ArrayList<>();
        
        switch (filterType) {
            case 1:
                System.out.print("Department name: ");
                String deptName = scanner.nextLine();
                filteredResults = courseService.filterByDepartment(deptName);
                break;
            case 2:
                System.out.println("Select semester:");
                Semester[] semesters = Semester.values();
                for (int i = 0; i < semesters.length; i++) {
                    System.out.println((i + 1) + ". " + semesters[i].getDisplayName());
                }
                int semChoice = getUserInputAsInt();
                filteredResults = courseService.filterBySemester(semesters[semChoice - 1]);
                break;
            case 3:
                System.out.print("Instructor name: ");
                String instructorName = scanner.nextLine();
                filteredResults = courseService.filterByInstructor(instructorName);
                break;
        }
        
        if (filteredResults.size() == 0) {
            System.out.println("No courses match your filter criteria.");
        } else {
            filteredResults.forEach(System.out::println);
        }
    }
    
    private static void handleEnrollmentManagement() {
        System.out.println("\n--- Enrollment Management ---");
        System.out.println("1. Enroll Student in Course");
        System.out.println("2. Remove Student from Course");
        System.out.println("3. View Student's Enrollments");
        System.out.print("Select option: ");
        
        int choice = getUserInputAsInt();
        
        switch (choice) {
            case 1:
                processStudentEnrollment();
                break;
            case 2:
                processStudentUnenrollment();
                break;
            case 3:
                showStudentEnrollments();
                break;
        }
    }
    
    private static void processStudentEnrollment() {
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Course Code: ");
        String courseCode = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseCode);
        
        if (student == null || course == null) {
            System.out.println("Either the student or course could not be found!");
            return;
        }
        
        try {
            enrollmentService.enroll(student, course);
            System.out.println("Enrollment completed successfully!");
        } catch (MaxCreditLimitExceededException ex) {
            System.err.println("Enrollment denied: " + ex.getMessage());
        } catch (DuplicateEnrollmentException ex) {
            System.err.println("Problem: " + ex.getMessage());
        } catch (Exception ex) {
            // Catch any other unexpected issues
            System.err.println("An unexpected error occurred: " + ex.getMessage());
        } finally {
            // Always let them know we're done processing
            System.out.println("Enrollment processing complete.");
        }
    }
    
    private static void processStudentUnenrollment() {
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Course Code: ");
        String courseCode = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseCode);
        
        if (student != null && course != null) {
            enrollmentService.unenroll(student, course);
            System.out.println("Student has been removed from the course!");
        } else {
            System.out.println("Could not find the student or course specified!");
        }
    }
    
    private static void showStudentEnrollments() {
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(student);
        
        if (enrollments.size() == 0) {
            System.out.println("This student is not currently enrolled in any courses.");
        } else {
            System.out.println("\n--- Current Enrollments ---");
            for (Enrollment enrollment : enrollments) {
                System.out.printf("%s - %s (Grade: %s)\n",
                    enrollment.getCourse().getCode().getCode(),
                    enrollment.getCourse().getTitle(),
                    enrollment.getGrade() != null ? enrollment.getGrade().getLetter() : "Not yet graded");
            }
        }
    }
    
    private static void handleGradeManagement() {
        System.out.println("\n--- Grade Management ---");
        System.out.println("1. Record Student Grades");
        System.out.println("2. Calculate GPA");
        System.out.println("3. Generate Official Transcript");
        System.out.print("Choose option: ");
        
        int choice = getUserInputAsInt();
        
        // Create a simple callback for completion notification
        Runnable completionCallback = new Runnable() {
            @Override
            public void run() {
                System.out.println("Grade operation has been completed.");
            }
        };
        
        switch (choice) {
            case 1:
                recordStudentGrades();
                completionCallback.run();
                break;
            case 2:
                calculateStudentGPA();
                completionCallback.run();
                break;
            case 3:
                generateOfficialTranscript();
                completionCallback.run();
                break;
        }
    }
    
    private static void recordStudentGrades() {
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Course Code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Enter marks (0-100): ");
        double marksEarned = scanner.nextDouble();
        scanner.nextLine(); // consume the leftover newline
        
        Student student = studentService.findById(studentId);
        Course course = courseService.findById(courseCode);
        
        if (student != null && course != null) {
            enrollmentService.recordGrade(student, course, marksEarned);
            Grade letterGrade = Grade.fromMarks(marksEarned);
            System.out.printf("Grade recorded: %.1f marks = %s letter grade\n", 
                             marksEarned, letterGrade.getLetter());
        } else {
            System.out.println("Unable to find the specified student or course!");
        }
    }
    
    private static void calculateStudentGPA() {
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        double calculatedGPA = enrollmentService.calculateGPA(student);
        student.setCumulativeGPA(calculatedGPA);
        System.out.printf("GPA for %s: %.2f\n", student.getFullName(), calculatedGPA);
    }
    
    private static void generateOfficialTranscript() {
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = studentService.findById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }
        
        List<Enrollment> studentEnrollments = enrollmentService.getStudentEnrollments(student);
        double currentGPA = enrollmentService.calculateGPA(student);
        
        TranscriptGenerator transcriptGenerator = new TranscriptGenerator();
        String officialTranscript = transcriptGenerator.generateTranscript(student, studentEnrollments, currentGPA);
        System.out.println(officialTranscript);
    }
    
    private static void handleImportExportOperations() {
        System.out.println("\n--- Data Import/Export ---");
        System.out.println("1. Import Student Data");
        System.out.println("2. Export Student Data");
        System.out.println("3. Export Course Data");
        System.out.print("Select operation: ");
        
        int choice = getUserInputAsInt();
        
        try {
            switch (choice) {
                case 1:
                    System.out.print("Import filename: ");
                    String importFilename = scanner.nextLine();
                    List<Student> importedStudents = ioService.importStudents(importFilename);
                    for (Student s : importedStudents) {
                        studentService.add(s);
                    }
                    System.out.println("Successfully imported " + importedStudents.size() + " students.");
                    break;
                case 2:
                    ioService.exportStudents(studentService.findAll(), "students_export.csv");
                    System.out.println("Student data exported to students_export.csv");
                    break;
                case 3:
                    ioService.exportCourses(courseService.findAll(), "courses_export.csv");
                    System.out.println("Course data exported to courses_export.csv");
                    break;
            }
        } catch (IOException ex) {
            System.err.println("File operation error: " + ex.getMessage());
        }
    }
    
    private static void handleBackupOperations() {
        System.out.println("\n--- Backup Management ---");
        System.out.println("1. Create New Backup");
        System.out.println("2. Check Backup Size");
        System.out.println("3. List Backup Contents");
        System.out.print("Choose action: ");
        
        int choice = getUserInputAsInt();
        
        try {
            switch (choice) {
                case 1:
                    backupService.createBackup();
                    break;
                case 2:
                    long backupSize = backupService.calculateDirectorySize(
                        AppConfig.getInstance().getBackupPath());
                    System.out.printf("Current backup size: %.2f MB\n", backupSize / (1024.0 * 1024.0));
                    break;
                case 3:
                    System.out.println("\n--- Backup Directory Contents ---");
                    backupService.listFilesByDepth(
                        AppConfig.getInstance().getBackupPath(), 0);
                    break;
            }
        } catch (IOException ex) {
            System.err.println("Backup operation failed: " + ex.getMessage());
        }
    }
    
    private static void handleReportGeneration() {
        System.out.println("\n--- Report Generation ---");
        System.out.println("1. Top Students Report");
        System.out.println("2. GPA Distribution Analysis");
        System.out.println("3. Course Statistics Report");
        System.out.print("Select report type: ");
        
        int choice = getUserInputAsInt();
        
        switch (choice) {
            case 1:
                generateTopPerformersReport();
                break;
            case 2:
                generateGPADistributionReport();
                break;
            case 3:
                generateCourseStatsReport();
                break;
        }
    }
    
    private static void generateTopPerformersReport() {
        List<Student> allStudents = studentService.findAll();
        
        // Update GPA for all students first, then sort by GPA
        for (Student s : allStudents) {
            s.setCumulativeGPA(enrollmentService.calculateGPA(s));
        }
        
        // Use stream API to get top performers
        System.out.println("\n--- Top 5 Performing Students ---");
        allStudents.stream()
            .sorted((student1, student2) -> Double.compare(student2.getCumulativeGPA(), student1.getCumulativeGPA()))
            .limit(5)
            .forEach(s -> System.out.printf("%s - GPA: %.2f\n", 
                s.getFullName(), s.getCumulativeGPA()));
    }
    
    private static void generateGPADistributionReport() {
        List<Student> allStudents = studentService.findAll();
        
        // Analyze GPA distribution using streams
        Map<String, Long> gpaDistribution = allStudents.stream()
            .map(student -> {
                double gpa = enrollmentService.calculateGPA(student);
                if (gpa >= 9.0) return "9.0-10.0 (Excellent)";
                else if (gpa >= 8.0) return "8.0-8.9 (Very Good)";
                else if (gpa >= 7.0) return "7.0-7.9 (Good)";
                else if (gpa >= 6.0) return "6.0-6.9 (Satisfactory)";
                else return "Below 6.0 (Needs Improvement)";
            })
            .collect(java.util.stream.Collectors.groupingBy(
                gpaRange -> gpaRange,
                java.util.stream.Collectors.counting()
            ));
        
        System.out.println("\n--- GPA Distribution Analysis ---");
        gpaDistribution.forEach((range, count) -> 
            System.out.printf("%s: %d students\n", range, count));
    }
    
    private static void generateCourseStatsReport() {
        List<Course> allCourses = courseService.findAll();
        
        System.out.println("\n--- Course Statistics Report ---");
        
        // Group courses by semester
        Map<Semester, Long> coursesPerSemester = allCourses.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Course::getSemester,
                java.util.stream.Collectors.counting()
            ));
        
        System.out.println("Distribution by Semester:");
        coursesPerSemester.forEach((semester, count) -> 
            System.out.printf("  %s: %d courses\n", semester.getDisplayName(), count));
        
        // Group courses by department
        Map<String, Long> coursesPerDepartment = allCourses.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Course::getDepartment,
                java.util.stream.Collectors.counting()
            ));
        
        System.out.println("\nDistribution by Department:");
        coursesPerDepartment.forEach((dept, count) -> 
            System.out.printf("  %s: %d courses\n", dept, count));
        
        // Calculate average credits
        double averageCreditHours = allCourses.stream()
            .mapToInt(Course::getCredits)
            .average()
            .orElse(0.0);
        
        System.out.printf("\nAverage Credit Hours per Course: %.1f\n", averageCreditHours);
    }
    
    private static void loadInitialData() {
        // Create some sample students
        Student student1 = new Student("S001", "CS202401", "John Doe", "john@example.com");
        Student student2 = new Student("S002", "CS202402", "Jane Smith", "jane@example.com");
        Student student3 = new Student("S003", "IT202401", "Bob Johnson", "bob@example.com");
        
        try {
            studentService.add(student1);
            studentService.add(student2);
            studentService.add(student3);
        } catch (Exception ex) {
            // If there are any issues adding sample data, just continue
        }
        
        // Create some instructors
        Instructor instructor1 = new Instructor("I001", "Prof. Komarasamy G", "komarasamye@gmail.com", "Computer Science");
        Instructor instructor2 = new Instructor("I002", "Prof. Raghavendra Mishra", "raghavendra@gmail.com", "Mathematics");
        
        // Create sample courses
        Course course1 = new Course.Builder(new CourseCode("CSE101"), "Introduction to Programming")
            .credits(4)
            .department("Computer Science")
            .semester(Semester.FALL)
            .instructor(instructor1)
            .build();
            
        Course course2 = new Course.Builder(new CourseCode("MAT201"), "Calculus II")
            .credits(3)
            .department("Mathematics")
            .semester(Semester.SPRING)
            .instructor(instructor2)
            .build();
            
        Course course3 = new Course.Builder(new CourseCode("CSE202"), "Data Structure")
            .credits(4)
            .department("Computer Science")
            .semester(Semester.FALL)
            .instructor(instructor1)
            .build();
        
        courseService.add(course1);
        courseService.add(course2);
        courseService.add(course3);
        
        System.out.println("Sample data has been loaded successfully!");
        
        // Run some demonstration code
        runBasicDemonstrations();
        runArrayDemonstrations();  
        runStringDemonstrations();
    }
    
    private static void runBasicDemonstrations() {
        // Basic arithmetic operations
        int firstNum = 10, secondNum = 3;
        int addition = firstNum + secondNum;  
        int subtraction = firstNum - secondNum; 
        int multiplication = firstNum * secondNum; 
        int division = firstNum / secondNum; 
        int remainder = firstNum % secondNum;  
        
        // Comparison operations
        boolean isLarger = firstNum > secondNum;  
        boolean areEqual = firstNum == secondNum;   
        
        // Logical operations
        boolean combinedResult = (firstNum > 5) && (secondNum < 5); 

        // Bitwise operations
        int bitwiseAnd = firstNum & secondNum;  
        int bitwiseOr = firstNum | secondNum;   
        int bitwiseXor = firstNum ^ secondNum; 

        // Operator precedence example
        int expression = 2 + 3 * 4; 
        
        // Type casting and instanceof
        Person genericPerson = new Student("TEST", "TEST123", "Test User", "test@test.com");
        if (genericPerson instanceof Student) {
            // Safe casting after instanceof check
            Student specificStudent = (Student) genericPerson;
        }
    }
    
    private static void runArrayDemonstrations() {
        // Array operations and manipulation
        String[] courseCodeArray = {"CSE101", "MAT201", "PHY101", "CSE202", "ENG101"};
        
        Arrays.sort(courseCodeArray);
        int searchIndex = Arrays.binarySearch(courseCodeArray, "CSE202");
        
        // Traditional for loop
        for (int i = 0; i < courseCodeArray.length; i++) {
            // Processing each element
        }
        
        // Enhanced for loop (for-each)
        for (String code : courseCodeArray) {
            // Process each code
        }
    }
    
    private static void runStringDemonstrations() {
        String applicationName = "Campus Course Records Manager";
        
        String upperCase = applicationName.toUpperCase();
        String lowerCase = applicationName.toLowerCase();
        String substring = applicationName.substring(7, 13); // "Course"
        String[] wordArray = applicationName.split(" ");
        String joinedWords = String.join("-", wordArray);
        boolean containsWord = applicationName.contains("Course");
        int comparisonResult = applicationName.compareTo("Another String!");
        boolean isExactMatch = applicationName.equals("Campus Course Records Manager");
        
        // String formatting
        String formattedString = String.format("Application: %s, Version: %.1f", "CCRM", 1.0);
    }
    
    private static int getUserInputAsInt() {
        try {
            int inputValue = scanner.nextInt();
            scanner.nextLine(); // consume the remaining newline
            return inputValue;
        } catch (InputMismatchException ex) {
            scanner.nextLine(); // clear the invalid input
            return -1;
        }
    }  
}