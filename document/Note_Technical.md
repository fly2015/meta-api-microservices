### Command Query Responsibility Segregation (CQRS). 
In this pattern, you separate the read operations (queries) from the write operations (commands). 
This can improve performance, scalability, and security.
-Use different databases or database schemas for read and write operations.


### CDS
CDS stands for Class Data Sharing. 
It's a feature in the Java HotSpot Virtual Machine that aims to reduce the startup time of Java applications and the footprint of the JVM process in memory.  
When a Java application starts up, the JVM needs to load and process class files. 
This involves reading the class files from disk, parsing the class data, verifying the bytecode, and possibly more, depending on the JVM and its configuration. This can take a significant amount of time, especially for large applications that use many classes.  With Class Data Sharing, the JVM can do much of this work once, save the result in a file (called a shared archive), and then reuse this data in subsequent runs of the application. This can significantly reduce the startup time of the application.  
The shared archive contains a snapshot of the class metadata (such as the bytecode, the constant pool, and various internal data structures that the JVM uses) and is memory-mapped into the JVM process. 
This allows multiple JVM processes to share the same class metadata, reducing the memory footprint when running multiple JVMs on the same machine.  
CDS was introduced in Java 5, but it has been significantly enhanced in more recent versions of Java. For example, Application Class-Data Sharing (AppCDS), introduced in Java 10, extends CDS to allow application classes to be placed in the shared archive, not just the classes of the JDK itself.