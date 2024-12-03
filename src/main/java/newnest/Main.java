package newnest;

import newnest.filters.ApartmentFilter;
import newnest.notification.TelegramAPI;
import newnest.property.DivarApartment;
import newnest.scraper.DivarApartmentScraper;
import newnest.store.StoreProcessor;
import newnest.utils.ConfLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        StoreProcessor storeProcessor = new StoreProcessor();

        Map<String, String> telegramConf = new ConfLoader("telegram").getConfs();
        TelegramAPI telegramAPI = new TelegramAPI(telegramConf);

        Map<String, String> cl = new ConfLoader("filters").getConfs();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                Set<String> storedIds = storeProcessor.loadStoredIds();

                ApartmentFilter filter = new ApartmentFilter(Arrays.asList(cl.get("districts").split(",")),
                        Integer.parseInt(cl.get("minCredit")), Integer.parseInt(cl.get("maxCredit")),
                        Integer.parseInt(cl.get("minRent")), Integer.parseInt(cl.get("maxRent")),
                        Integer.parseInt(cl.get("minSize")), Integer.parseInt(cl.get("maxSize")),
                        Integer.parseInt(cl.get("minYear")), Integer.parseInt(cl.get("rooms")),
                        Boolean.parseBoolean(cl.get("parking")), Boolean.parseBoolean(cl.get("elevator")));

                List<DivarApartment.Post> apartments = new DivarApartmentScraper().Scrape(filter, "api");

                System.out.println(apartments.size() + " filtered in.");

                List<DivarApartment.Post> newApartments = storeProcessor.filterNewApartments(apartments, storedIds);
                System.out.println(apartments.size() + " apartments out of " + newApartments.size() + " were new.");


                for (DivarApartment.Post apartment : newApartments)
                    if (telegramAPI.sendMessage(apartment)) {
                        storeProcessor.addApartmentToStore(apartment);
                        System.out.println("Posted New Apartment");
                    }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 60, TimeUnit.SECONDS);

    }
}
