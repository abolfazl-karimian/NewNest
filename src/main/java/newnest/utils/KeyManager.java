package newnest.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyManager {
    private final List<String> apiKeys;
    private int currentIndex;

    public KeyManager(Map<String, String> keyConf) throws IOException {
        this.apiKeys = new ArrayList<>(keyConf.values());
        System.out.println("Loaded " + apiKeys.size() + " API keys");
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
