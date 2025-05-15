package ma.ensa;

import ma.ensa.db.DatabaseManager;
import ma.ensa.db.impl.MySQLManager;
import ma.ensa.db.impl.PostgreSQLManager;
import ma.ensa.db.impl.SQLServerManager;
import ma.ensa.util.DBConfigLoader;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            Properties props = DBConfigLoader.loadProperties("db.properties");

            // Test MySQL
            System.out.println("\n=== MySQL (XAMPP) ===");
            DatabaseManager mysqlManager = new MySQLManager(
                    props.getProperty("mysql.url") + "/" + props.getProperty("mysql.database"),
                    props.getProperty("mysql.username"),
                    props.getProperty("mysql.password")
            );
            testDatabase(mysqlManager, "employees");

            // Test PostgreSQL
            System.out.println("\n=== PostgreSQL (pgAdmin) ===");
            DatabaseManager postgresManager = new PostgreSQLManager(
                    props.getProperty("postgresql.url") + "/" + props.getProperty("postgresql.database"),
                    props.getProperty("postgresql.username"),
                    props.getProperty("postgresql.password")
            );
            testDatabase(postgresManager, "employees");

            // Test SQL Server
            System.out.println("\n=== SQL Server ===");
            DatabaseManager sqlServerManager = new SQLServerManager(
                    props.getProperty("sqlserver.url"),
                    props.getProperty("sqlserver.username"),
                    ""
            );
            testDatabase(sqlServerManager, "employees");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testDatabase(DatabaseManager manager, String tableName) throws Exception {
        try {
            manager.connect();
            System.out.println("Connecté : " + manager.isConnected());

            List<Map<String, Object>> results = manager.executeQuery("SELECT * FROM " + tableName);
            System.out.println("Nombre d'enregistrements : " + results.size());

            for (Map<String, Object> row : results) {
                System.out.println(row);
            }

            int affectedRows = manager.executeUpdate(
                    "INSERT INTO " + tableName + " VALUES (4, 'Test', 'User', 'test.user@email.com', 3000.00)");
            System.out.println("Lignes insérées : " + affectedRows);

            manager.executeUpdate("DELETE FROM " + tableName + " WHERE id = 4");
        } finally {
            manager.disconnect();
            System.out.println("Déconnecté\n");
        }
    }
}