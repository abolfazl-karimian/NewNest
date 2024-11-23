package newnest.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfLoader {
    private static final Logger logger = Logger.getLogger(ConfLoader.class.getName());
    private Map<String, String> options = new HashMap<>();

    public ConfLoader(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(getFile(path));
        setConfs(properties);
    }


    private InputStream getFile(String path) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(path);
        if (input == null) {
            logger.severe("Unable to find " + path);
            throw new IOException("Configuration file not found: " + path);
        }
        return input;
    }



    private void setConfs(Properties properties) {
        properties.forEach((key, value) -> {
            options.put(key.toString(), value.toString());
        });
    }

    public Map<String, String> getConfs() {
        return this.options;
    }
}


