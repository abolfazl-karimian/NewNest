package newnest;

import newnest.filters.ApartmentFilter;
import newnest.scraper.DivarApartmentScraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> districts = new ArrayList<>();
        districts.add("abshar");
        districts.add("nazi-abad");
        districts.add("ajoodanieh");
        ApartmentFilter filter = new ApartmentFilter(districts,10000000,250000000,1000000,
                20000000,60,500,5,50,1,true);
        DivarApartmentScraper DAS = new DivarApartmentScraper(filter);
//        DAS.scrape();
    }
}
