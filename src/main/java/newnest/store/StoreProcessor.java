package newnest.store;

import newnest.property.DivarApartment;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class StoreProcessor {
    private final String filePath = "/opt/nest/store/store.csv";

    // Load apartment IDs from the file
    public Set<String> loadStoredIds() throws IOException {
        File file = new File(filePath);

        // Create the file if it doesn't exist
        if (!file.exists()) {
            file.createNewFile();
            return new HashSet<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.toSet());
        }
    }

    // Save new apartment IDs to the file
    public void updateStoredIDs(Set<String> newIds) throws IOException, URISyntaxException {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (String id : newIds) {
                writer.write(id);
                writer.newLine();
            }
        }
    }


    // Save new apartment IDs to the file
    public void updateStoredApartments(List<DivarApartment.Post> acceptedApartments) throws IOException, URISyntaxException {
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (DivarApartment.Post ap : acceptedApartments) {
                writer.write(ap.getToken());
                writer.newLine();
            }
        }
    }

    public void addApartmentToStore(DivarApartment.Post apartment) throws IOException, URISyntaxException {
        File file = new File(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(apartment.getToken());
                writer.newLine();
        }
    }


    // Filter new apartments that aren't stored in the file
    public List<DivarApartment.Post> filterNewApartments(List<DivarApartment.Post> apartments, Set<String> storedIds) {
        return apartments.stream()
                .filter(apartment -> !storedIds.contains(apartment.getToken()))
                .collect(Collectors.toList());
    }

    // Simulate sending apartments to the API and return the accepted ones
    public List<DivarApartment.Post> sendToApi(List<DivarApartment.Post> apartments) {
        // Simulated API logic
        List<DivarApartment.Post> acceptedApartments = new ArrayList<>();
        for (DivarApartment.Post apartment : apartments) {
            if (mockApiCall(apartment)) { // Replace this with real API call
                acceptedApartments.add(apartment);
            }
        }
        return acceptedApartments;
    }

    // Mock API call for simulation
    public boolean mockApiCall(DivarApartment.Post apartment) {
        // Simulate an API accepting every other apartment
        return apartment.getToken().hashCode() % 2 == 0;
    }
}
