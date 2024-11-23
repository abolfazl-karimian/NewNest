package newnest;

import newnest.filters.ApartmentFilter;
import newnest.property.DivarApartment;
import newnest.scraper.DivarApartmentScraper;
import newnest.store.StoreProcessor;
import newnest.utils.ConfLoader;
import newnest.utils.KeyManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        KeyManager keys = new KeyManager("keys");
        Map<String, String> cl = new ConfLoader("filters").getConfs();
        List<String> districts = Arrays.asList(cl.get("districts").split(","));


        // Schedule the task
        scheduler.scheduleAtFixedRate(() -> {
            try {
                StoreProcessor storeProcessor = new StoreProcessor();
                Set<String> storedIds = storeProcessor.loadStoredIds();

                ApartmentFilter filter = new ApartmentFilter(districts,
                        Integer.parseInt(cl.get("minCredit")), Integer.parseInt(cl.get("maxCredit")),
                        Integer.parseInt(cl.get("minRent")), Integer.parseInt(cl.get("maxRent")),
                        Integer.parseInt(cl.get("minSize")), Integer.parseInt(cl.get("maxSize")),
                        Integer.parseInt(cl.get("minAge")), Integer.parseInt(cl.get("maxAge")),
                        Integer.parseInt(cl.get("rooms")), Boolean.parseBoolean(cl.get("parking")));
                List<DivarApartment.Post> apartments = new DivarApartmentScraper().Scrape(filter, keys);

                System.out.println("Scraped apartments: " + apartments.size());

                List<DivarApartment.Post> newApartments = storeProcessor.filterNewApartments(apartments, storedIds);
                System.out.println("New scraped apartments: " + newApartments.size());

                List<DivarApartment.Post> acceptedApartments = storeProcessor.sendToApi(newApartments);

                System.out.println("Accepted apartments: " + acceptedApartments.size());

                storeProcessor.updateStoredApartments(acceptedApartments);


            } catch (Exception e) {
                // Handle exceptions to avoid crashing the scheduler
                e.printStackTrace();
            }
        }, 0, 60, TimeUnit.SECONDS);

    }
}
