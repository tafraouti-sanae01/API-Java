package ma.ensa.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfigLoader {
    public static Properties loadProperties(String filename) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = DBConfigLoader.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new IOException("Unable to find " + filename);
            }
            properties.load(input);
        }
        return properties;
    }
}