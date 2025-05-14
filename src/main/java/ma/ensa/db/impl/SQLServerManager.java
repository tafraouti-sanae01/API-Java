package ma.ensa.db.impl;

import lombok.Getter;
import ma.ensa.db.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SQLServerManager implements DatabaseManager {
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public SQLServerManager(String url, String username, String password) {
        // Configuration pour l'authentification Windows
        this.url = "jdbc:sqlserver://" + url + ";databaseName=TestAPI;integratedSecurity=true;encrypt=false;trustServerCertificate=true;";
        this.username = username;
        this.password = password;

        // Nécessite d'ajouter mssql-jdbc_auth.dll au classpath
        System.setProperty("java.library.path", "C:/sqljdbc_<version>/enu/auth/x64");
    }

    @Override
    public void connect() throws Exception {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            throw new Exception("SQL Server Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("SQL Server Connection Error: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() throws Exception {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new Exception("SQL Server Disconnection Error: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> executeQuery(String query) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            throw new Exception("SQL Server Query Execution Error: " + e.getMessage());
        }

        return resultList;
    }

    @Override
    public int executeUpdate(String query) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new Exception("SQL Server Update Execution Error: " + e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}