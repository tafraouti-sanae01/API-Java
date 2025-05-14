package ma.ensa.db;

import java.util.List;
import java.util.Map;

public interface DatabaseManager {
    void connect() throws Exception;
    void disconnect() throws Exception;
    List<Map<String, Object>> executeQuery(String query) throws Exception;
    int executeUpdate(String query) throws Exception;
    boolean isConnected();
}