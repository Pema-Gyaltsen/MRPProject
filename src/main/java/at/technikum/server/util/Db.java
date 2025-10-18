package at.technikum.server.util;

import java.sql.*; //brings in JDBC types (Connection, DriverManager, SQLException, etc.).
//JDBC: java Database Connectivity

public final class Db { //final: no class can extend it

    //need to implement null exception here
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASSWORD");
//system: built-in java class java.lang.System
    //getenv(): static method on System: reads, returns environment variable's value

    static {
        try { Class.forName("org.postgresql.Driver"); } //org.postgresql.Driver is inside PostgreSQL JBDC driver JAR, dependency in pom.xml; class becomes available at runtime
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    } //find a class with that fully qualified name, load its bytecode, link it, initialize it

    //forces loading PostgreSQL JDBC driver

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    } //Connection: predefined interface from java.sql; return type: open database session to run SQL
    //DriverManager: predefined class in java.sql
    //getConnection: predefined static method: tries drivers in order to create a Connection for the given JDBC URL.


    private Db(){} //private constructor prevents instantiation; enforces "static-only utility" pattern
}
