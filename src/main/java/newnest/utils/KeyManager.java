package newnest.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeyManager {
    private final List<String> apiKeys;
    private int currentIndex;

    public KeyManager(String path) throws IOException {
        ConfLoader keys = new ConfLoader(path);
        this.apiKeys = new ArrayList<>(keys.getConfs().values());
        System.out.println("Loaded " + apiKeys.size() + " API keys");
        apiKeys.add("key3");
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
