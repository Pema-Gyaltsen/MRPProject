package at.technikum.util;

import java.sql.*;

public final class Db {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASSWORD");

    static {
        try { Class.forName("org.postgresql.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    private Db(){}
}
