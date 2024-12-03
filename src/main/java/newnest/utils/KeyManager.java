package newnest.utils;

import newnest.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class KeyManager {
    private static final Logger logger = LoggingUtil.getLogger(Main.class);
    private final List<String> apiKeys;
    private int currentIndex;

    public KeyManager(Map<String, String> keyConf) throws IOException {
        this.apiKeys = new ArrayList<>(keyConf.values());
        logger.info("Loaded " + apiKeys.size() + " API keys");
        this.currentIndex = 0;
    }

    // Get the current key
    public String getKey() {
        return apiKeys.get(currentIndex);
    }

    // Move to the next key (cyclically)
    public void moveToNextKey() {
        currentIndex = (currentIndex + 1) % apiKeys.size();
    }

    public int getKMSize() {
        return apiKeys.size();
    }
}
