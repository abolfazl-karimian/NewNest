package newnest.utils;

import java.util.ArrayList;
import java.util.List;

public class KeyManager {
    private final List<String> apiKeys;
    private int currentIndex;

    public KeyManager() {
        this.apiKeys = new ArrayList<>();
        apiKeys.add("eyJhbGciOiJSUzI1NiIsImtpZCI6InByaXZhdGVfa2V5XzIiLCJ0eXAiOiJKV1QifQ.eyJhcHBfc2x1ZyI6ImdyZWVuLXRhYmJ5LW1vdGgiLCJhdWQiOiJzZXJ2aWNlcHJvdmlkZXJzIiwiZXhwIjoxNzM3MzAyNzI2LCJqdGkiOiI0MTgxOGZhMC1hNzU5LTExZWYtYTM4ZC0wZTdhZDNlNjM2NGQiLCJpYXQiOjE3MzIxMTg3MjYsImlzcyI6ImRpdmFyIiwic3ViIjoiYXBpa2V5In0.YUxGq-wmVnPsM-c_Ma3LdwrIyQ7pQJMSE9uxrWnjeLGHhKwtloen0XG_fC-tBpuf2G5RphEpkUiaKbkqolL86t13XMmAwRF5L_htWKvUkJeN1sdcbtQefqktG0OKQdNMgLCi7RUtJ8CmwWs0WCNiw0Rh3hDrGG9JZwtpE_xotYirZwB-pRS96TbOdKOnJo43_VQyAt01yuSLiVi984UVXqu1sz6sM7Al_KnH06p5Zva1ifcb2jU-8q3xXql6QtmeY1huGST2nABb-jZW2bDjZK95x1MtAiOu7chbkroFd1996-0FS6j4W2dcP3oJoQUGQYBLlKzLIdyu9WpXjNeU2A");
        apiKeys.add("eyJhbGciOiJSUzI1NiIsImtpZCI6InByaXZhdGVfa2V5XzIiLCJ0eXAiOiJKV1QifQ.eyJhcHBfc2x1ZyI6InZpb2xldC1wZWF0LXN0b3JrIiwiYXVkIjoic2VydmljZXByb3ZpZGVycyIsImV4cCI6MTczNzM2Mzk0NCwianRpIjoiY2EyZWE1NzMtYTdlNy0xMWVmLWEzOGQtMGU3YWQzZTYzNjRkIiwiaWF0IjoxNzMyMTc5OTQ0LCJpc3MiOiJkaXZhciIsInN1YiI6ImFwaWtleSJ9.uwrlXwAAsjBBtntDKDtyx30Yh2DaP1-PqGPSb-cu-Os2rWX7OkT664I03JqUm28ATlxE-1nZo-Lyq7PKKITYFxCcB1irKbhWFmEbnJ2LeXkNEYgWTLVYKCtou4lMtDVArbtquCx1obToHVX0nn8qoOV3loGONzP9WvJU2m28377ZZrc9XmbMB6MA1ho_MOlpucKGKIVz6uI4JzInUa_oJSr64XxAC_jv_MPAVamOLC9P6Be1ymTBBpo_DLt6BMhmM9B5kE6ifPGvQpxjnHHmK27rDG68N3xyjQEdQuVISWIjAk-kbCilnuVBVB8Pb2hJWLuujraFBwiucqFmuTiMxA");
        apiKeys.add("ajoodanieh");
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
