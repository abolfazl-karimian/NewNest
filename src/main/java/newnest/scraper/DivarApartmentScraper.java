package newnest.scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import newnest.filters.ApartmentFilter;
import newnest.property.DivarApartment;
import newnest.property.Apartment;
import newnest.utils.HTTPRequest;
import newnest.utils.KeyManager;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DivarApartmentScraper extends Scraper {
//    https://api.divar.ir/v1/open-platform/finder/post/
//    Token : eyJhbGciOiJSUzI1NiIsImtpZCI6InByaXZhdGVfa2V5XzIiLCJ0eXAiOiJKV1QifQ.eyJhcHBfc2x1ZyI6InZpb2xldC1wZWF0LXN0b3JrIiwiYXVkIjoic2VydmljZXByb3ZpZGVycyIsImV4cCI6MTczNzI5MjI3NSwianRpIjoiZWMzYzljM2YtYTc0MC0xMWVmLTgxNDktYjJkOGE5MWQzMTFhIiwiaWF0IjoxNzMyMTA4Mjc1LCJpc3MiOiJkaXZhciIsInN1YiI6ImFwaWtleSJ9.vdEAGBkaqOgCoonEGHmUD7rx3H03qYRCJRoxhdc7i3wkkykqgwzE0FCOlgZC2OEyYVjsbMYDUzvcUBVamFMdTu0NUfgqUOZN9Xa0eJeYyJKcXBZvKLgc2YwaxzNUbGp_sNf6tCXFTu4qo4DiUS4KqPEYDVLJCKWNkMxWvV_dQbFcbCHmANDQapWzR8raroykpdNHk1jCCi3RxlJfipALo2XzOQOC0PrYPBU4wEx_T2Y9K5hMGtaWXPzYqE3DatpIFEkfuT62ma-dh5xnDpNyesT4p4eh3Dpt-ea88-t9vuuZqYo-YV3MXoyl_vml9jD14hhIzSEQ-YrR2Bnp0sVU2A
//    {
//    "city": "tehran",
//    "category": "light",
//    "districts": ["abshar", "nazi-abad"],
//    "query": {
//        "brand_model": {
//            "value": ["Pride 111 EX", "MVM 110"]
//        },
//        "production-year": {
//            "min": 1380,
//            "max": 1398
//        },
//        "usage": {
//            "max": 200000
//        }
//    }
//}
//
//    https://github.com/divar-ir/kenar-docs/?tab=readme-ov-file
//    https://github.com/divar-ir/kenar-docs/blob/master/finder/search_post.md
//    https://github.com/divar-ir/kenar-docs/blob/master/finder/get_post.md

    private final String website = "Divar";
    //    private final String endpoint = "https://divar.ir/s/tehran/rent-apartment/ararat?";
    private final String endpoint = "https://api.divar.ir/v1/open-platform/finder/post";
    //    private String district = "districts=301%2C68%2C943%2C85%2C360%2C91%2C67%2C934%2C86%2C1028%2C1025%2C940%2C71%2C81%2C84%2C75%2C95%2C72%2C300%2C64%2C941%2C70%2C65%2C944%2C96%2C74%2C939%2C315%2C90%2C127&";
//    private String district = "districts=301%2C68%2C943%2C85%2C360%2C91%2C67%2C934%2C86%2C1028%2C1025%2C940%2C71%2C81%2C84%2C75%2C95%2C72%2C300%2C64%2C941%2C70%2C65%2C944%2C96%2C74%2C939%2C315%2C90%2C127&";
    private String searchUrl;
    KeyManager km;

    public DivarApartmentScraper() {
    }

    public DivarApartmentScraper(ApartmentFilter filter) throws IOException, InterruptedException {
//        searchUrl = endpoint;
        KeyManager km = new KeyManager();
        String jsonResponse = ApiCall(filter, km);
//        searchUrl = endpoint + "wZ9MqS7W";
//        String response = request.get(searchUrl, headers);
        ObjectMapper mapper = new ObjectMapper();
        DivarApartment response = mapper.readValue(jsonResponse, DivarApartment.class);
        List<DivarApartment.Post> posts = response.getPosts();
        List<DivarApartment.Post> customFilteredPosts = response.getPosts();

//        System.out.println(posts.toString());
        System.out.println(posts.size());
        /*
        {
    "city": "tehran",
    "category": "apartment-rent",
    "districts": ["abshar", "nazi-abad","ajoodanieh"],
    "query": {
    "parking": true,
    "size":{
        "min": 60
    },
    "rent": {
        "min": 1000000,
        "max": 20000000
    },
    "credit": {
        "min": 1000000,
        "max": 250000000
    }
       }
}
         */
        /*
        searchUrl = endpoint + district +
                String.format(
                        "credit=%d-%d&" +
                                "rent=%d-%d&" +
                                "size=%d-%d&" +
                                "rooms=%s&" +
                                "building-age=%d-%d&" +
                                "parking=%s",
                        minCredit, maxCredit,
                        minRent, maxRent,
                        minSize, maxSize,
                        extractRooms(bedrooms),
                        minAge, maxAge,
                        parking ? "true" : "");
        System.out.println(searchUrl);*/
    }

    private String ApiCall(ApartmentFilter filter, KeyManager km) throws IOException, InterruptedException {
        HTTPRequest request = new HTTPRequest();
        int maxRetries = km.getKMSize();
        int retryCount = 0;
        HttpResponse response = null;
        String responseBody = null;

        String jsonPayload = getPayload(filter);

        while (retryCount < maxRetries) {
            try {
                List<Map.Entry<String, String>> headers = getHeaders(km.getKey());
                System.out.println(km.getKey());
                response = request.post(endpoint, headers, jsonPayload);
                System.out.println("Status Code: " + response.statusCode());

                if (response.statusCode() == 429) {
                    retryCount++;
                    System.out.println("Retrying... (" + retryCount + ")");
                    km.moveToNextKey(); // Cycle to the next key
                } else {
                    responseBody = response.body().toString();
                    break;
                }
            } catch (Exception e) {
                System.err.println("Error occurred: " + e.getMessage());
                retryCount++;
                km.moveToNextKey(); // Try with the next key on exception
            }
        }

        if (responseBody == null) {
            throw new RuntimeException("Failed to get a successful response after " + maxRetries + " retries.");
        }
/*
        HttpResponse response = request.post(endpoint, headers, jsonPayload);
        System.out.println(response.statusCode());*/
        return response.body().toString();
    }

    private static List<Map.Entry<String, String>> getHeaders(String key) {
        List<Map.Entry<String, String>> headers = List.of(
                Map.entry("x-api-key", key),
                Map.entry("Content-Type", "application/json")
        );
        return headers;
    }

    private String getPayload(ApartmentFilter filter) {
        String jsonPayload = String.format("""
                        {
                            "city": "tehran",
                            "category": "apartment-rent",
                            "districts": %s,
                            "query": {
                            "rooms": {
                                "value": ["%s"]
                              },
                            "size":{
                                "min": %d,
                                "max": %d
                            },
                            "rent": {
                                "min": %d,
                                "max": %d
                            },
                            "credit": {
                                "min": %d,
                                "max": %d
                            },
                           "parking": %s
                               }
                        }""", districtToString(filter.getDistricts()), filter.getRooms(),
                filter.getMinSize(), filter.getMaxSize(),
                filter.getMinRent(), filter.getMaxRent(),
                filter.getMinCredit(), filter.getMaxCredit(),
                filter.hasParking());
        return jsonPayload;
    }


    private String districtToString(List<String> districts) {
        return "[\"" + String.join("\", \"", districts) + "\"]";
    }

    private String extractSearchUrl() {
        return searchUrl;
    }

    public List<Apartment> scrape() throws IOException {
//        long startTime = System.nanoTime();
        List<String> apartmentUrls = extractApartmentUrls(extractSearchUrl());
//        long endTime = System.nanoTime();
//        long duration = (endTime - startTime)/1000000;
//        System.out.println("process time to extract ad urls: " + duration);


        /*
        DivarApartmentScraper scraper = new DivarApartmentScraper();
        List<Apartment> apartments = apartmentUrls.stream()
                .map(scraper::extractApartment) // Convert URL to Apartment
                .collect(Collectors.toList());*/


//        System.out.println(doc.title());
//        startTime = System.nanoTime();
//        Elements divs = doc.select("a.kt-post-card__action");
//        endTime = System.nanoTime();
//        duration = (endTime - startTime)/1000000;
//        System.out.println("process time step 1" + duration );

//        System.out.println("Hrefs: " + apartments);
        Apartment ap = extractApartment("https://divar.ir/v/%D9%81%D8%B1%D8%B4%D8%AA%D9%87-%DB%B2%DB%B8%DB%B0%D9%85%D8%AA%D8%B1-%DA%A9%D9%84%DB%8C%D8%AF%D9%86%D8%AE%D9%88%D8%B1%D8%AF%D9%87/wZ9MKGnz");
        System.out.println(ap);
        return List.of();
    }

    private List<String> extractApartmentUrls(String url) throws IOException {
        doc = Jsoup.connect(url).get();
        List<String> apartmentUrls = doc.getElementsByClass("kt-post-card__action")
                .stream()
                .map(element -> "https://divar.ir" + element.attr("href"))
                .collect(Collectors.toList());
        return apartmentUrls;
    }

    private Apartment extractApartment(String apartmentUrl) throws IOException {
        System.out.println(apartmentUrl);
        doc = Jsoup.connect(apartmentUrl).get();
        System.out.println(doc);
        extractSpecs();
        /*
        return new Apartment(extractYear(), extractFloor(), extractCredit(), extractRent(), extractSize(),
                extractBedroom(), extractElevator(), extractParking(), extractDistrict());*/
        return new Apartment();
//        return new Apartment();
    }

    private String extractTitle() {
        return this.doc.getElementsByClass("kt-page-title__title kt-page-title__title--responsive-sized").text();
    }

    private String extractDistrict() {
        return this.doc.getElementsByClass("kt-page-title__subtitle kt-page-title__subtitle--responsive-sized").text();
    }

    private Elements extractFeatures() {
        return this.doc.getElementsByClass("kt-group-row-item kt-group-row-item__value kt-body kt-body--stable");
    }

    private Boolean extractElevator() {
        return this.extractFeatures().get(0).text().equals("آسانسور");
    }

    private Boolean extractParking() {
        return this.extractFeatures().get(1).text().equals("پارکینگ");
    }


    private Elements extractSpecs() {
//        System.out.println(this.doc);
//        System.out.println(this.doc.getElementsByClass("kt-page-title__title kt-page-title__title--responsive-sized").text());
        return this.doc.getElementsByClass("kt-group-row-item kt-group-row-item__value kt-group-row-item--info-row");
    }

    private int extractSize() {
        return Integer.parseInt(extractSpecs().get(0).text());
    }

    private int extractYear() {
//        return Integer.parseInt(extractSpecs().get(1).text());
        return 3;
    }

    private int extractBedroom() {
        return Integer.parseInt(extractSpecs().get(2).text());
    }

    private Elements extractConds() {
        return this.doc.getElementsByClass("kt-unexpandable-row__value");
    }


    private int extractCredit() {
        return getEnglishDigits(extractConds().get(0).text().split(" ")[0].replace("٬", ""));
    }

    private int extractRent() {
        return getEnglishDigits(extractConds().get(1).text().split(" ")[0].replace("٬", ""));

    }

    private int isInterchangeable() {
        return getEnglishDigits(extractConds().get(2).text().split(" ")[0].replace("٬", ""));

    }

    private int extractFloor() {
        return getEnglishDigits(extractConds().get(3).text());

    }

    private int getEnglishDigits(String persianDigits) {
        StringBuilder englishDigits = new StringBuilder();
        for (char c : persianDigits.toCharArray()) {
            if (Character.isDigit(c))
                englishDigits.append(Character.getNumericValue(c));
        }
        return Integer.parseInt(englishDigits.toString());

    }


}
