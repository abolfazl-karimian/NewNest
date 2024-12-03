package newnest.scraper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import newnest.filters.ApartmentFilter;
import newnest.property.DivarApartment;
import newnest.utils.HTTPRequest;
import newnest.utils.KeyManager;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class DivarAPICall {
    HTTPRequest request;
    KeyManager km;
    private final String endpoint = "https://api.divar.ir/v2/open-platform/finder/post";
    int maxRetries;
    int retryCount = 0;
    String payload;
    ObjectMapper mapper;


    public DivarAPICall(KeyManager km) {
        this.request = new HTTPRequest();
        this.km = km;
        this.maxRetries = km.getKMSize();
        mapper = new ObjectMapper();

    }

    public List<DivarApartment.Post> getApartments(ApartmentFilter filter) throws JsonProcessingException {
        this.payload = getPayload(filter);
        String jsonResponse = this.call(payload);

        DivarApartment response = mapper.readValue(jsonResponse, DivarApartment.class);

        return response.getPosts();

    }

    public String call(String jsonPayload) {
        HttpResponse response = null;
        String responseBody = null;

        while (retryCount < maxRetries) {
            try {
                List<Map.Entry<String, String>> headers = getHeaders(km.getKey());
                response = request.post(endpoint, jsonPayload, headers);
                System.out.println("Request have been sent.");

//                System.out.println(jsonPayload);
//                System.out.println(endpoint);

                if (response.statusCode() != 200) {
                    retryCount++;
                    System.out.println(response.statusCode() + " - Retrying... (" + retryCount + ")");
                    if (response.statusCode() == 429) {
                        km.moveToNextKey();
                        System.out.println("Testing next key...");
                    }

                } else {
                    responseBody = response.body().toString();
                    break;
                }
            } catch (Exception e) {
                System.err.println("Error occurred At Divar: " + e.getMessage());
                retryCount++;
                km.moveToNextKey();
            }
        }

        if (responseBody == null) {
            throw new RuntimeException("Failed to get a successful response after " + maxRetries + " retries.");
        }

        return responseBody;
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
                filter.isParking());
        return jsonPayload;
    }

    private String districtToString(List<String> districts) {
        return "[\"" + String.join("\", \"", districts) + "\"]";
    }


}
