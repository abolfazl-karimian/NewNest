package newnest.filters;

import lombok.Getter;

import java.util.List;

public class ApartmentFilter {
    // Getters and setters (or use Lombok for brevity)
    @Getter
    private List<String> districts;
    @Getter
    private int minCredit;
    @Getter
    private int maxCredit;
    @Getter
    private int minRent;
    @Getter
    private int maxRent;
    @Getter
    private int minSize;
    @Getter
    private int maxSize;
    @Getter
    private int minYear;
    @Getter
    private int rooms;
    @Getter
    private boolean parking;
    @Getter
    private boolean elevator;

    // Constructor
    public ApartmentFilter(List<String> districts, int minCredit, int maxCredit, int minRent, int maxRent,
                           int minSize, int maxSize, int minAge, int rooms, boolean parking, boolean elevator) {
        this.districts = districts;
        this.minCredit = minCredit;
        this.maxCredit = maxCredit;
        this.minRent = minRent;
        this.maxRent = maxRent;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.minYear = minAge;
        this.rooms = rooms;
        this.parking = parking;
        this.elevator = elevator;
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
