package newnest.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivarApartment extends Apartment {
    private List<Post> posts;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Post {
        @JsonProperty("category")
        private String category;

        @JsonProperty("business_type")
        private String businessType;

        @JsonProperty("city")
        private String city;

        @JsonProperty("last_modified_at")
        private String lastModified;

        @JsonProperty("token")
        private String token;

        @JsonProperty("title")
        private String title;

        @JsonProperty("real_estate_fields")
        private RealEstateFields    realEstateFields;

        public String getUrl() {
            return "https://divar.ir/v/" + getToken();
        }

        @Data
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RealEstateFields {

            @JsonProperty("has_parking")
            private boolean parking;

            @JsonProperty("has_elevator")
            private boolean elevator;

            @JsonProperty("floor")
            private int floor;

            @JsonProperty("year")
            private int year;

            @JsonProperty("size")
            private int size;

            @JsonProperty("rooms")
            private String rooms;

            public int getRoomsAsInt() {
                if (rooms == null) {
                    return 0; // or throw an exception based on your requirement
                }
                switch (rooms.trim()) {
                    case "یک":
                        return 1;
                    case "دو":
                        return 2;
                    case "سه":
                        return 3;
                    case "چهار":
                        return 4;
                    case "پنج":
                        return 5;
                    // Add more cases as needed
                    default:
                        try {
                            return Integer.parseInt(rooms);
                        } catch (NumberFormatException e) {
                            // Log the exception or handle it as per your need
                            return 0;
                        }
                }
            }

            @JsonProperty("rent")
            private Rent rent;

            @JsonProperty("credit")
            private Credit credit;

            @Data
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Rent {
                @JsonProperty("mode")
                private String mode;

                @JsonProperty("value")
                private Long value; // Nullable

                @Override
                public String toString() {
                    return value != null ? value.toString() : "null";
                }
            }

            @Data
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Credit {
                @JsonProperty("mode")
                private String mode;

                @JsonProperty("value")
                private Long value; // Nullable

                @Override
                public String toString() {
                    return value != null ? value.toString() : "null";
                }
            }
        }
    }
}
