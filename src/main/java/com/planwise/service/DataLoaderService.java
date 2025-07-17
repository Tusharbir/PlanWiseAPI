package com.planwise.service;

import com.planwise.model.Plan;
import org.springframework.stereotype.Service;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DataLoaderService {

    private final Map<String, List<Plan>> sitePlanMap = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void loadCsvOnStart() {
        String filePath = "src/main/resources/crawled_data/merged-csv.csv";

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String headerLine = br.readLine(); // Read and skip header
            System.out.println("CSV Header: " + headerLine);

            String line;
            int lineCount = 0;
            int successCount = 0;

            while ((line = br.readLine()) != null) {
                lineCount++;

                try {
                    // Parse CSV line properly handling quoted fields
                    String[] fields = parseCSVLine(line);

                    if (fields.length < 12) {
                        System.out.println("Skipping line " + lineCount + " - insufficient fields: " + fields.length);
                        continue;
                    }

                    // Map CSV columns to fields based on your CSV structure:
                    // Company,Plan Name,Data Limited,Price,Download Speed,Upload Speed,Technology,Features,Description,Pros,Compatible Modems Link,Plan Link
                    String company = fields[0].trim();
                    String planName = fields[1].trim();
                    String dataLimit = fields[2].trim();
                    String price = fields[3].trim();
                    String downloadSpeed = fields[4].trim();
                    String uploadSpeed = fields[5].trim();
                    String technology = fields[6].trim();
                    String features = fields[7].trim();
                    String description = fields[8].trim();
                    String pros = fields[9].trim();
                    String modemLink = fields[10].trim();
                    String planLink = fields[11].trim();

                    // Skip if company or plan name is empty
                    if (company.isEmpty() || planName.isEmpty()) {
                        System.out.println("Skipping line " + lineCount + " - empty company or plan name");
                        continue;
                    }

                    // Create Plan object
                    Plan plan = new Plan(
                            company,        // site
                            "",            // provider (empty as noted in your code)
                            planName,      // planName
                            technology,    // technology
                            downloadSpeed, // downloadSpeed
                            uploadSpeed,   // uploadSpeed
                            price,         // price
                            dataLimit,     // dataLimit
                            "",            // totalMonthly (empty as noted)
                            planLink       // planURL
                    );

                    // Add to map with lowercase key for case-insensitive lookup
                    sitePlanMap.computeIfAbsent(company.toLowerCase(), k -> new ArrayList<>()).add(plan);
                    successCount++;

                    System.out.println("Added plan: " + planName + " for company: " + company);

                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineCount + ": " + e.getMessage());
                    System.err.println("Line content: " + line);
                }
            }

            System.out.println("Plans loaded successfully from merged-csv.csv");
            System.out.println("Total lines processed: " + lineCount);
            System.out.println("Successfully parsed: " + successCount);
            System.out.println("Total companies: " + sitePlanMap.size());
            System.out.println("Companies: " + sitePlanMap.keySet());

        } catch (IOException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parse a CSV line handling quoted fields properly
     * This is a simple CSV parser that handles quoted fields containing commas
     */
    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Handle escaped quotes
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++; // Skip the next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        // Add the last field
        fields.add(currentField.toString());

        return fields.toArray(new String[0]);
    }

    public List<Plan> getPlansBySite(String siteName) {
        if (siteName == null || siteName.trim().isEmpty()) {
            System.out.println("Empty site name provided");
            return new ArrayList<>();
        }

        List<Plan> plans = sitePlanMap.getOrDefault(siteName.toLowerCase(), new ArrayList<>());
        System.out.println("Getting plans for site: '" + siteName + "' (found " + plans.size() + " plans)");

        // Also try exact match if lowercase doesn't work
        if (plans.isEmpty()) {
            plans = sitePlanMap.getOrDefault(siteName, new ArrayList<>());
            System.out.println("Tried exact match for site: '" + siteName + "' (found " + plans.size() + " plans)");
        }

        return plans;
    }

    public Set<String> getAllSites() {
        Set<String> sites = sitePlanMap.keySet();
        System.out.println("Available sites: " + sites);
        return sites;
    }
    public List<Plan> getAllPlans() {
        return sitePlanMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}