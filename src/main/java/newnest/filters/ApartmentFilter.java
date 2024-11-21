package newnest.filters;

import java.util.List;

public class ApartmentFilter {
    private List<String> districts;
    private int minCredit;
    private int maxCredit;
    private int minRent;
    private int maxRent;
    private int minSize;
    private int maxSize;
    private int minAge;
    private int maxAge;
    private String rooms;
    private boolean parking;

    // Constructor
    public ApartmentFilter(List<String> districts, int minCredit, int maxCredit, int minRent, int maxRent,
                           int minSize, int maxSize, int minAge, int maxAge, int rooms, boolean parking) {
        this.districts = districts;
        this.minCredit = minCredit;
        this.maxCredit = maxCredit;
        this.minRent = minRent;
        this.maxRent = maxRent;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.rooms = convertToPersian(rooms);
        this.parking = parking;
    }

    // Getters and setters (or use Lombok for brevity)
    public List<String> getDistricts() {
        return districts;
    }

    public int getMinCredit() {
        return minCredit;
    }

    public int getMaxCredit() {
        return maxCredit;
    }

    public int getMinRent() {
        return minRent;
    }

    public int getMaxRent() {
        return maxRent;
    }

    public int getMinSize() {
        return minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public String getRooms() {
        return rooms;
    }

    public String hasParking() {
        return parking ? "true" : "false";
    }

    private static String convertToPersian(int digit) {
        String str = switch (digit) {
            case 1 -> "یک";
            case 2 -> "دو";
            case 3 -> "سه";
            case 4 -> "چهار";
            default -> "";
        };
        return str;
    }
}
