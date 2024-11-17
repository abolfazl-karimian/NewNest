package newnest.scraper;

import newnest.property.Apartment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.rmi.server.LogStream.log;

public class DivarApartmentScraper extends Scraper{
    private final String website = "Divar";
    private final String endpoint = "https://divar.ir/s/tehran/rent-apartment/ararat?";
    private String district = "districts=301%2C68%2C943%2C85%2C360%2C91%2C67%2C934%2C86%2C1028%2C1025%2C940%2C71%2C81%2C84%2C75%2C95%2C72%2C300%2C64%2C941%2C70%2C65%2C944%2C96%2C74%2C939%2C315%2C90%2C127&";
    private String searchUrl;


    public DivarApartmentScraper(int minCredit, int maxCredit, int minRent,
                                 int maxRent, int minSize, int maxSize, int minAge, int maxAge,
                                 int bedrooms, boolean parking) {
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
                getRooms(bedrooms),
                minAge, maxAge,
                parking ? "true" : "false" );
        System.out.println(searchUrl);
    }

    private static String getRooms(int bedrooms) {
        String rooms="";
        switch(bedrooms) {
            case 1:
                rooms = "یک";
                break;
            case 2:
                rooms = "دو";
                break;
            case 3:
                rooms = "سه";
                break;
            case 4:
                rooms = "چهار";
                break;
        }
        return rooms;
    }

    private String getSearchUrl() {
        return searchUrl;
    }
    public List<Apartment> scrape() throws IOException {
//        long startTime = System.nanoTime();
        List<String> apartmentUrls = getApartmentUrls(getSearchUrl());
//        long endTime = System.nanoTime();
//        long duration = (endTime - startTime)/1000000;
//        System.out.println("process time to get ad urls: " + duration);

        List<Apartment> apartments = apartmentUrls.stream()
                .map(DivarApartmentScraper::getApartment) // Convert URL to Apartment
                .collect(Collectors.toList());

//        System.out.println(doc.title());
//        startTime = System.nanoTime();
//        Elements divs = doc.select("a.kt-post-card__action");
//        endTime = System.nanoTime();
//        duration = (endTime - startTime)/1000000;
//        System.out.println("process time step 1" + duration );

        System.out.println("Hrefs: " + apartments);



        return List.of();
    }

    private List<String> getApartmentUrls(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        List<String> apartmentUrls = doc.getElementsByClass("kt-post-card__action")
                .stream()
                .map(element -> "https://divar.ir" + element.attr("href"))
                .collect(Collectors.toList());
        return apartmentUrls;
    }
    
    private Apartment getApartment(String apartmentUrl) {
        return new Apartment(getYear(),getFloor(),getCredit(),getRent(),getSize(),getBedroomgetElevator(),getParking(),getDistrict());
    }
}
