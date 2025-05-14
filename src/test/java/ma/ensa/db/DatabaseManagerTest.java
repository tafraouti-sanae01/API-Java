package ma.ensa.db;

import ma.ensa.db.impl.MySQLManager;
import ma.ensa.util.DBConfigLoader;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseManagerTest {
    private static DatabaseManager dbManager;

    @BeforeAll
    static void setup() throws Exception {
        Properties props = DBConfigLoader.loadProperties("db.properties");
        dbManager = new MySQLManager(
                props.getProperty("mysql.url") + "/" + props.getProperty("mysql.database"),
                props.getProperty("mysql.username"),
                props.getProperty("mysql.password")
        );
        dbManager.connect();
    }

    @AfterAll
    static void cleanup() throws Exception {
        if (dbManager != null && dbManager.isConnected()) {
            dbManager.disconnect();
        }
    }

    @Test
    @Order(1)
    void testConnection() {
        assertTrue(dbManager.isConnected());
    }

    @Test
    @Order(2)
    void testQueryExecution() throws Exception {
        List<Map<String, Object>> results = dbManager.executeQuery("SELECT * FROM employees");
        assertFalse(results.isEmpty());
        assertEquals(3, results.size());
    }

    @Test
    @Order(3)
    void testInsertAndDelete() throws Exception {
        // Insertion
        int inserted = dbManager.executeUpdate(
                "INSERT INTO employees VALUES (4, 'Test', 'User', 'test@email.com', 3000.00)");
        assertEquals(1, inserted);

        // Vérification
        List<Map<String, Object>> results = dbManager.executeQuery(
                "SELECT * FROM employees WHERE id = 4");
        assertEquals(1, results.size());
        assertEquals("Test", results.get(0).get("first_name"));

        // Nettoyage
        int deleted = dbManager.executeUpdate("DELETE FROM employees WHERE id = 4");
        assertEquals(1, deleted);
    }

    @Test
    @Order(4)
    void testInvalidQuery() {
        Exception exception = assertThrows(Exception.class, () -> {
            dbManager.executeQuery("SELECT * FROM non_existent_table");
        });
        assertTrue(exception.getMessage().contains("Error"));
    }
}