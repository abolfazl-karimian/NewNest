package newnest;

import newnest.filters.ApartmentFilter;
import newnest.notification.TelegramAPI;
import newnest.property.DivarApartment;
import newnest.scraper.DivarApartmentScraper;
import newnest.store.StoreProcessor;
import newnest.utils.ConfLoader;
import newnest.utils.LoggingUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class Main {
    private static final Logger logger = LoggingUtil.getLogger(Main.class);
    private static int period = 60;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tehran"));
    }

    public static void main(String[] args) throws IOException {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        StoreProcessor storeProcessor = new StoreProcessor();

        Map<String, String> telegramConf = new ConfLoader("telegram").getConfs();
        TelegramAPI telegramAPI = new TelegramAPI(telegramConf);

        Map<String, String> cl = new ConfLoader("filters").getConfs();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                logger.info("New run started.");

                Set<String> storedIds = storeProcessor.loadStoredIds();

                ApartmentFilter filter = new ApartmentFilter(Arrays.asList(cl.get("districts").split(",")),
                        Integer.parseInt(cl.get("minCredit")), Integer.parseInt(cl.get("maxCredit")),
                        Integer.parseInt(cl.get("minRent")), Integer.parseInt(cl.get("maxRent")),
                        Integer.parseInt(cl.get("minSize")), Integer.parseInt(cl.get("maxSize")),
                        Integer.parseInt(cl.get("minYear")), Integer.parseInt(cl.get("rooms")),
                        Boolean.parseBoolean(cl.get("parking")), Boolean.parseBoolean(cl.get("elevator")));

                List<DivarApartment.Post> apartments = new DivarApartmentScraper().Scrape(filter, "api");
                logger.info(apartments.size() + " apartments are here to check.");

                List<DivarApartment.Post> newApartments = storeProcessor.filterNewApartments(apartments, storedIds);
                logger.info(newApartments.size() + " apartments out of " + apartments.size() + " were new");


                for (DivarApartment.Post apartment : newApartments)
                    if (telegramAPI.sendMessage(apartment)) {
                        storeProcessor.addApartmentToStore(apartment);
                        logger.info("Posted New Apartment");
                    }


            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("Waiting " + period + " seconds...");
        }, 0, period, TimeUnit.SECONDS);

    }
}
