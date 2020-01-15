import java.util.Properties;

public class Config {

    private final Properties properties = new Properties();

    public String getString(String key) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return null;
    }

    public boolean hasKey(String key) {
        return properties.containsKey(key);
    }

    public int getInteger(String key, int defaultValue) {
        if (hasKey(key)) {
            return Integer.parseInt(getString(key));
        } else {
            return defaultValue;
        }
    }
}
