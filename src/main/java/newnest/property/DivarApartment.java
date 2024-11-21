package newnest.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.List;
import java.util.Map;

public class DivarApartment extends Apartment {
    private List<Post> posts;
/*
    public DivarApartment(int year, int floor, int credit, int rent, int size, int bedroom, boolean elevator, boolean parking, String district) {
        super(year, floor, credit, rent, size, bedroom, elevator, parking, district);
    }*/


    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Post {
        @JsonProperty("business_type")
        private String businessType;
        @JsonProperty("city")
        private String city;
        @JsonProperty("last_modified")
        private String lastModified;
        @JsonProperty("token")
        private String token;
        @JsonProperty("post_data")
        private PostData postData;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PostData {
            @JsonProperty("category")
            private String category;
            @JsonProperty("description")
            private String description;
            @JsonProperty("district")
            private String district;
            @JsonProperty("parking")
            private boolean parking;
            @JsonProperty("elevator")
            private boolean elevator;
            @JsonProperty("rent_credit_transform")
            private boolean rent_credit_transform;
            @JsonProperty("floor")
            private int floor;
            @JsonProperty("production-year")
            private int productionYear;
            @JsonProperty("size")
            private int size;
            @JsonProperty("title")
            private String title;
            @JsonProperty("rent")
            private Rent rent;
            @JsonProperty("credit")
            private Credit credit;

            @Override
            public String toString() {
                return "PostData{" +
                        "category='" + category + '\'' +
                        ", description='" + description + '\'' +
                        ", district='" + district + '\'' +
                        ", parking=" + parking +
                        ", elevator=" + elevator +
                        ", rent_credit_transform=" + rent_credit_transform +
                        ", floor=" + floor +
                        ", productionYear=" + productionYear +
                        ", size=" + size +
                        ", title='" + title + '\'' +
                        ", rent=" + rent.toString() +
                        ", credit=" + credit.toString() +
                        '}';
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Rent {
                private String mode;
                private int value;

                // Getters and setters...
                public String getMode() {
                    return mode;
                }

                public void setMode(String mode) {
                    this.mode = mode;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                @Override
                public String toString() {
                    return "" + value;
                }
            }


            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Credit {
                private String mode;
                private int value;

                // Getters and setters...
                public String getMode() {
                    return mode;
                }

                public void setMode(String mode) {
                    this.mode = mode;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                @Override
                public String toString() {
                    return "" + value;
                }
            }
        }

        @Override
        public String toString() {
            return "Post{" +
                    "businessType='" + businessType + '\'' +
                    ", city='" + city + '\'' +
                    ", lastModified='" + lastModified + '\'' +
                    ", token='" + token + '\'' +
                    ", postData=" + postData +
                    '}';
        }
    }
}

