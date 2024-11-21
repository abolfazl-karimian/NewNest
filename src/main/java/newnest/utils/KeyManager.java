package newnest.utils;

import java.util.ArrayList;
import java.util.List;

public class KeyManager {
    private final List<String> apiKeys;
    private int currentIndex;

    public KeyManager() {
        this.apiKeys = new ArrayList<>();
        apiKeys.add("key1");
        apiKeys.add("key2");
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

    public int getKMSize(){
        return apiKeys.size();
    }
}
