# Campus Course & Records Manager (CCRM)

## Evolution of Java
- 1991: Oak language created by James Gosling at Sun Microsystems
- 1995: Renamed to Java, first public release (Java 1.0)
- 1998: Java 2 (J2SE 1.2) with Swing, Collections Framework
- 2004: Java 5 with Generics, Annotations, Enums
- 2014: Java 8 with Lambda expressions, Stream API
- 2017: Java 9 with Module System
- 2021: Java 17 LTS with Pattern Matching, Records
- 2023: Java 21 LTS with Virtual Threads

## Java Editions Comparison

| Feature | Java ME | Java SE | Java EE |
|---------|---------|---------|---------|
| Target | Mobile/Embedded devices | Desktop applications | Enterprise applications |
| Memory | Limited (KB-MB) | Moderate (MB-GB) | Large (GB+) |
| APIs | Subset of SE | Core Java APIs | Extended APIs (Servlets, JSP, EJB) |
| Use Cases | IoT, Smart cards | Desktop apps, Android | Web servers, Microservices |

## Java Architecture

### JDK (Java Development Kit)
- Complete development environment
- Includes JRE + development tools (javac, javadoc, jar)
- Required for compiling Java code

### JRE (Java Runtime Environment)
- Runtime libraries and JVM
- Required for running Java applications
- Cannot compile code

### JVM (Java Virtual Machine)
- Executes bytecode
- Platform-specific implementation
- Provides memory management, security, portability

### How They Interact:
JDK → contains → JRE → contains → JVM
Source Code → javac (JDK) → Bytecode → JVM → Machine Code

## Installation on Windows
1. Download JDK from Oracle/OpenJDK website
![Download JDK](./screenshots/JDKDownload.png)
2. Run installer as Administrator
3. Set JAVA_HOME environment variable to JDK path
![Download JDK](./screenshots/SettingJAVAHOMEEnvironmentVariable.png)
4. Add %JAVA_HOME%\bin to PATH
5. Verify: `java -version` and `javac -version`
![Download JDK](./screenshots/JDKInstallationVerification.png)

## Eclipse IDE Setup
1. Download Eclipse IDE for Java Developers
![Download Eclipse1](./screenshots/EclipseInstallation1.png)
![Download Eclipse2](./screenshots/EclipseInstallation2.png)
![Download Eclipse3](./screenshots/EclipseInstallation3.png)
![Download Eclipse4](./screenshots/EclipseInstallation4.png)
![Download Eclipse5](./screenshots/EclipseInstallation5.png)
3. File → New → Java Project
![SetupProject1](./screenshots/ProjectSetup1.png)
![SetupProject2](./screenshots/ProjectSetup2.png)
![SetupProject3](./screenshots/ProjectSetup3.png)
5. Configure Build Path for libraries
![ConfigureBuildPath1](./screenshots/ProjectSetup4.png)
![ConfigureBuildPath2](./screenshots/ProjectSetup5.png)
![ConfigureBuildPath3](./screenshots/ProjectSetup6.png)
![ConfigureBuildPath4](./screenshots/ProjectSetup7.png)
![ConfigureBuildPath5](./screenshots/ProjectSetup8.png)
7. Run → Run Configurations → Java Application
8. Select Main class and Run

## Errors vs Exceptions
- **Errors**: Serious problems that applications shouldn't catch (OutOfMemoryError, StackOverflowError)
- **Exceptions**: Conditions that applications might want to catch
  - Checked: Must be handled (IOException)
  - Unchecked: Runtime exceptions (NullPointerException)

## Enabling Assertions
Run with: `java -ea MainClass` or `-enableassertions`

### Author : Kevin George Varghese
### Reg No: 24BCE10489
