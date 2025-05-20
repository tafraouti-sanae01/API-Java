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
                    props.getProperty("sqlserver.password")
            );
            testDatabase(sqlServerManager, "employees");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testDatabase(DatabaseManager manager, String tableName)
            throws Exception {
        try {
            // Connexion
            manager.connect();
            System.out.println("Statut de la connexion : " + manager.isConnected());

            // Execution d'une requete SELECT
            System.out.println("\n--- Avant l'insertion et suppression ---");
            List<Map<String, Object>> results1 = manager.executeQuery
                    ("SELECT * FROM " + tableName);

            // Affichage des resultats
            System.out.println("\nResultats de la requete :");
            for (Map<String, Object> row : results1) {
                System.out.println(row);
            }

            // Exemple d'insertion
            int affectedRows1 = manager.executeUpdate(
                    "INSERT INTO " + tableName + " VALUES (4, 'Test1','User1','test1.user1@email.com', 4700.00)");
            System.out.println("Lignes insérées (4, 'Test1', 'User1', 'test1.user1@email.com', 4700.00) : " + affectedRows1);


            int affectedRows2 = manager.executeUpdate(
                    "INSERT INTO " + tableName + " VALUES (5, 'Test2','User2','test2.user2@email.com', 3000.00)");
            System.out.println("Lignes insérées (5, 'Test2', 'User2', 'test2.user2@email.com', 3000.00) : " + affectedRows2);

            // Nettoyage
            manager.executeUpdate("DELETE FROM " + tableName + " WHERE id = 4");
            System.out.println("\nSuppression de l'enregistrement : (4, 'Test1', 'User1', 'test1.user1@email.com', 4700.00) ");
            manager.executeUpdate("DELETE FROM " + tableName + " WHERE id = 4");
            manager.executeUpdate("DELETE FROM " + tableName + " WHERE id = 5");


            System.out.println("\n\n--- Apres l'insertion et suppression ---");
            List<Map<String, Object>> results2 = manager.executeQuery("SELECT * FROM " + tableName);

            System.out.println("Nombre d'enregistrements : " + results2.size());
            for (Map<String, Object> row : results2) {
                System.out.println(row);
            }

        } finally {
            // Fermeture propre
            manager.disconnect();
            System.out.println("\nConnexion fermee");
        }
    }
}