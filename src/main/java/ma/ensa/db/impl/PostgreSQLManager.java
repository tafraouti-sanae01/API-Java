package ma.ensa.db.impl;

import lombok.Getter;
import ma.ensa.db.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PostgreSQLManager implements DatabaseManager {
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public PostgreSQLManager(String url, String username, String password) {
        this.url = "jdbc:postgresql://" + url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void connect() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new Exception("PostgreSQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("PostgreSQL Connection Error: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() throws Exception {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new Exception("PostgreSQL Disconnection Error: " + e.getMessage());
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
            throw new Exception("PostgreSQL Query Execution Error: " + e.getMessage());
        }

        return resultList;
    }

    @Override
    public int executeUpdate(String query) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new Exception("PostgreSQL Update Execution Error: " + e.getMessage());
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

    public List<String> getDatabaseList() throws Exception {
        List<String> databases = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT datname FROM pg_database WHERE datistemplate = false")) {

            while (rs.next()) {
                databases.add(rs.getString("datname"));
            }
        } catch (SQLException e) {
            throw new Exception("Error fetching PostgreSQL databases: " + e.getMessage());
        }
        return databases;
    }
}