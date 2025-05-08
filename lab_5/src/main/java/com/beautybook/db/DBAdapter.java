package com.beautybook.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAdapter {
    private static final String DB_URL = "jdbc:sqlite:beautybook.db";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    public static void initializeDB() {
        String sqlMasters = "CREATE TABLE IF NOT EXISTS masters ("
                + "id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "specialization TEXT NOT NULL)";
        
        String sqlAppointments = "CREATE TABLE IF NOT EXISTS appointments ("
                + "id INTEGER PRIMARY KEY,"
                + "clientName TEXT NOT NULL,"
                + "clientPhone TEXT NOT NULL,"
                + "dateTime TEXT NOT NULL,"
                + "masterId INTEGER NOT NULL)";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlMasters);
            stmt.execute(sqlAppointments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}