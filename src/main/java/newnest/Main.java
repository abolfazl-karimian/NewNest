package newnest;

import newnest.scraper.DivarApartmentScraper;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DivarApartmentScraper DAS = new DivarApartmentScraper(50000000,350000000,2000000,
                20000000,64,180,5,50,2,true);
        DAS.scrape();
    }
}
