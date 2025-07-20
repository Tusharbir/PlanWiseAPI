package com.planwise.service;

import com.planwise.model.Plan;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanService {
    private List<Plan> allPlans = new ArrayList<>();

    @PostConstruct
    public void loadPlansFromCsv() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("merged-csv.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] fields = parseCsvLine(line);
                if (fields.length < 8) continue; // handle according to your CSV

                Plan plan = new Plan(
                        fields[0].trim(), // site/company
                        "",               // provider unused
                        fields[1].trim(), // planName
                        fields[2].trim(), // dataLimit
                        fields[3].trim(), // price
                        fields[4].trim(), // downloadSpeed
                        fields[5].trim(), // uploadSpeed
                        fields[6].trim(), // technology
                        fields[7].trim()  // features
                );
                allPlans.add(plan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simple CSV parser, handles quoted commas
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else field.append(c);
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }

    public List<Plan> getAllPlans() {
        return allPlans;
    }

    public Set<String> getAllSites() {
        return allPlans.stream().map(Plan::getSite).collect(Collectors.toSet());
    }

    public List<Plan> getPlansBySite(String site) {
        return allPlans.stream()
                .filter(plan -> plan.getSite().equalsIgnoreCase(site))
                .collect(Collectors.toList());
    }
}