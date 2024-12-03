package newnest.scraper;

import newnest.Main;
import newnest.filters.ApartmentFilter;
import newnest.property.DivarApartment;
import newnest.property.Apartment;
import newnest.utils.ConfLoader;
import newnest.utils.KeyManager;
import newnest.utils.LoggingUtil;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DivarApartmentScraper extends Scraper {
    private static final Logger logger = LoggingUtil.getLogger(Main.class);

    private final String website = "Divar";

    public DivarApartmentScraper() {
    }

    public List<DivarApartment.Post> Scrape(ApartmentFilter filter, String type) throws IOException, InterruptedException {
        List<DivarApartment.Post> posts;
        if (type.equals("api")) {
            DivarAPICall api = new DivarAPICall(new KeyManager(new ConfLoader("keys").getConfs()));
            posts = api.getApartments(filter);
        } else {
            DivarExtractor de = new DivarExtractor();
            de.getApartments();
            posts = new ArrayList<>();

        }

        logger.info(posts.size() + " apartments were found");

        List<DivarApartment.Post> customFilteredPosts = filterBasedOnRooms(posts, filter.getRooms());
        logger.info(customFilteredPosts.size() + " - filterBasedOnRooms");

        customFilteredPosts = filterBasedOnRent(customFilteredPosts, filter.getMinRent(), filter.getMaxRent());
        logger.info(customFilteredPosts.size() + " - filterBasedOnRent");

        customFilteredPosts = filterBasedOnCredit(customFilteredPosts, filter.getMinCredit(), filter.getMaxCredit());
        logger.info(customFilteredPosts.size() + " - filterBasedOnCredit");

        customFilteredPosts = filterBasedOnSize(customFilteredPosts, filter.getMinSize(), filter.getMaxSize());
        logger.info(customFilteredPosts.size() + " - filterBasedOnSize");

        customFilteredPosts = filterBasedOnYear(customFilteredPosts, filter.getMinYear());
        if (filter.isParking())
            customFilteredPosts = filterNoParking(customFilteredPosts);
        if (filter.isElevator())
            customFilteredPosts = filterNoElevator(customFilteredPosts);

        return customFilteredPosts;
    }


    private List<DivarApartment.Post> filterBasedOnRooms(List<DivarApartment.Post> posts, int rooms) {
        Predicate<DivarApartment.Post> filter = post -> {
            DivarApartment.Post.RealEstateFields data = post.getRealEstateFields();
            return data.getRoomsAsInt() >= rooms;
        };
        return posts.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    private List<DivarApartment.Post> filterBasedOnSize(List<DivarApartment.Post> posts, int min, int max) {
        Predicate<DivarApartment.Post> filter = post -> {
            DivarApartment.Post.RealEstateFields data = post.getRealEstateFields();
            return data.getSize() >= min && data.getSize() <= max;
        };
        return posts.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    private List<DivarApartment.Post> filterBasedOnRent(List<DivarApartment.Post> posts, int min, int max) {
        return posts.stream()
                .filter(post -> Optional.ofNullable(post.getRealEstateFields())
                        .map(DivarApartment.Post.RealEstateFields::getRent)
                        .map(DivarApartment.Post.RealEstateFields.Rent::getValue)
                        .filter(rentValue -> rentValue >= min && rentValue <= max)
                        .isPresent())
                .collect(Collectors.toList());
    }

    private List<DivarApartment.Post> filterBasedOnCredit(List<DivarApartment.Post> posts, int min, int max) {
        return posts.stream()
                .filter(post -> Optional.ofNullable(post.getRealEstateFields())
                        .map(DivarApartment.Post.RealEstateFields::getCredit)
                        .map(DivarApartment.Post.RealEstateFields.Credit::getValue)
                        .filter(creditValue -> creditValue >= min && creditValue <= max)
                        .isPresent())
                .collect(Collectors.toList());
    }

    private List<DivarApartment.Post> filterBasedOnYear(List<DivarApartment.Post> posts, int min) {
        Predicate<DivarApartment.Post> filter = post -> {
            DivarApartment.Post.RealEstateFields data = post.getRealEstateFields();
            return data.getYear() >= min;
        };
        return posts.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }


    private List<DivarApartment.Post> filterNoParking(List<DivarApartment.Post> posts) {
        Predicate<DivarApartment.Post> filter = post -> {
            DivarApartment.Post.RealEstateFields data = post.getRealEstateFields();
            return data.isParking();
        };
        return posts.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    private List<DivarApartment.Post> filterNoElevator(List<DivarApartment.Post> posts) {
        Predicate<DivarApartment.Post> filter = post -> {
            DivarApartment.Post.RealEstateFields data = post.getRealEstateFields();
            return data.isElevator() || data.getFloor() < 1;
        };
        return posts.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }


    private String extractSearchUrl() {
        String searchUrl = "";
        return searchUrl;
    }

    public List<Apartment> scrape() throws IOException {
//        long startTime = System.nanoTime();
        List<String> apartmentUrls = extractApartmentUrls(extractSearchUrl());

        Apartment ap = extractApartment("https://divar.ir/v/%D9%81%D8%B1%D8%B4%D8%AA%D9%87-%DB%B2%DB%B8%DB%B0%D9%85%D8%AA%D8%B1-%DA%A9%D9%84%DB%8C%D8%AF%D9%86%D8%AE%D9%88%D8%B1%D8%AF%D9%87/wZ9MKGnz");
//        System.out.println(ap);
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

        return new Apartment();
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
        return this.doc.getElementsByClass("kt-group-row-item kt-group-row-item__value kt-group-row-item--info-row");
    }

    private int extractSize() {
        return Integer.parseInt(extractSpecs().get(0).text());
    }

    private int extractYear() {
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
