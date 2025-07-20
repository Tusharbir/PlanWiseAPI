package com.planwise.service;

import com.planwise.model.SearchFrequency;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class SearchFrequencyService {

    private final ConcurrentHashMap<String, SearchFrequency> freqMap = new ConcurrentHashMap<>();

    private final Path csvPath = Paths.get("data/search_frequency.csv");

    // Load CSV file into freqMap on app start
    @PostConstruct
    public void loadFromCsv() {
        if (!Files.exists(csvPath)) {
            System.out.println("Search frequency CSV not found: " + csvPath.toAbsolutePath());
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(csvPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String keyword = parts[0].trim().toLowerCase();
                    int count = Integer.parseInt(parts[1].trim());
                    freqMap.put(keyword, new SearchFrequency(keyword, count));
                }
            }
            System.out.println("Loaded search frequencies from CSV, entries: " + freqMap.size());
        } catch (IOException e) {
            System.err.println("Error loading search frequency CSV: " + e.getMessage());
        }
    }

    // Save freqMap to CSV file (overwrite)
    private synchronized void saveToCsv() {
        try {
            Files.createDirectories(csvPath.getParent()); // Ensure parent dir exists
        } catch (IOException e) {
            System.err.println("Failed to create directory for search frequency CSV: " + e.getMessage());
        }

        try (BufferedWriter bw = Files.newBufferedWriter(csvPath)) {
            for (Map.Entry<String, SearchFrequency> entry : freqMap.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue().getCount());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving search frequency CSV: " + e.getMessage());
        }
    }

    public void incrementFrequency(String keyword) {
        String key = keyword.toLowerCase();
        freqMap.compute(key, (k, v) -> {
            if (v == null) {
                v = new SearchFrequency(k, 1);
            } else {
                v.setCount(v.getCount() + 1);
            }
            saveToCsv();  // Save on every update (could be optimized with batching)
            return v;
        });
    }

    public int getFrequency(String keyword) {
        SearchFrequency freq = freqMap.get(keyword.toLowerCase());
        return freq == null ? 0 : freq.getCount();
    }
}