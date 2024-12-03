package newnest.utils;

import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingUtil {
    static {
        try (InputStream configStream = LoggingUtil.class.getClassLoader().getResourceAsStream("logging.properties")) {
            if (configStream == null) {
                throw new RuntimeException("Could not find logging.properties in the classpath");
            }
            LogManager.getLogManager().readConfiguration(configStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load logging configuration", e);
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}

