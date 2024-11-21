package newnest.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class HTTPRequest {
    HttpClient client;
//    HttpResponse<String> response;

    public HTTPRequest() {
        client = HttpClient.newHttpClient();
        HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
//        System.setProperty("jdk.httpclient.HttpClient.log", "all");

    }

    public HttpResponse<String> post(String url, List<Map.Entry<String, String>> headers, String jsonPayload) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload));

        addHeader(headers, requestBuilder);

        HttpRequest request = requestBuilder.build();

        return sendRequest(request);
    }


    public String get(String url, List<Map.Entry<String, String>> headers) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        addHeader(headers, requestBuilder);

        HttpRequest request = requestBuilder.build();

        return sendRequest(request).body();


    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private static void addHeader(List<Map.Entry<String, String>> headers, HttpRequest.Builder requestBuilder) {
        for (Map.Entry<String, String> header : headers) {
            requestBuilder.header(header.getKey(), header.getValue());
        }
    }
}
