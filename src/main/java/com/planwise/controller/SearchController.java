//package com.planwise.controller;
//
//import com.planwise.model.Plan;
//import com.planwise.service.DataLoaderService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Set;
//
//@RestController
//@RequestMapping("/api")
////@CrossOrigin(origins = "http://localhost:3000")
//public class SearchController {
//
//    private final DataLoaderService dataLoader;
//
//    public SearchController(DataLoaderService dataLoader) {
//        this.dataLoader = dataLoader;
//    }
//
//    @GetMapping("/plans")
//    public List<Plan> getPlans(@RequestParam String site) {
//        System.out.println("Received request for site: " + site);
//        List<Plan> plans = dataLoader.getPlansBySite(site);
//        System.out.println("Returning " + plans.size() + " plans for site: " + site);
//        return plans;
//    }
//
//    @GetMapping("/sites")
//    public Set<String> getAvailableSites() {
//        Set<String> sites = dataLoader.getAllSites();
//        System.out.println("Available sites endpoint called, returning: " + sites);
//        return sites;
//    }
//
//    // Add debug endpoint to help troubleshoot
//    @GetMapping("/debug")
//    public String debug() {
//        Set<String> sites = dataLoader.getAllSites();
//        StringBuilder sb = new StringBuilder();
//        sb.append("Available sites: ").append(sites).append("\n");
//
//        for (String site : sites) {
//            List<Plan> plans = dataLoader.getPlansBySite(site);
//            sb.append("Site: ").append(site).append(" has ").append(plans.size()).append(" plans\n");
//            for (Plan plan : plans) {
//                sb.append("  - ").append(plan.getPlanName()).append("\n");
//            }
//        }
//
//        return sb.toString();
//    }
//}


package com.planwise.controller;

import com.planwise.model.Plan;
import com.planwise.service.DataLoaderService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SearchController {

    private final DataLoaderService dataLoaderService;

    public SearchController(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    /**
     * GET /api/search?q=...
     *
     * - If q is null/blank or contains no letters/digits, returns [] immediately.
     * - If q matches exactly a site name, returns that site's plans.
     * - Otherwise, does free-text search across all Plan fields.
     */
    @GetMapping("/search")
    public List<Plan> search(@RequestParam("q") String q) {
        if (q == null) {
            return Collections.emptyList();
        }

        String raw   = q.trim();
        // Edge case: empty string
        if (raw.isEmpty()) {
            return Collections.emptyList();
        }
        // Edge case: no alphanumeric chars
        if (!raw.matches(".*[A-Za-z0-9].*")) {
            return Collections.emptyList();
        }

        String lower = raw.toLowerCase();

        // 1) Exact site match?
        Set<String> sites = dataLoaderService.getAllSites();
        if (sites.contains(lower)) {
            return dataLoaderService.getPlansBySite(lower);
        }

        // 2) Free-text search otherwise
        return dataLoaderService.getAllPlans().stream()
                .filter(p ->
                        contains(p.getSite(), lower)          ||
                                contains(p.getPlanName(), lower)      ||
                                contains(p.getTechnology(), lower)    ||
                                contains(p.getDownloadSpeed(), lower) ||
                                contains(p.getUploadSpeed(), lower)   ||
                                contains(p.getPrice(), lower)         ||
                                contains(p.getDataLimit(), lower)     ||
                                contains(p.getPlanURL(), lower) ||
                                contains(p.getFeatures(), lower) ||
                                contains(p.getDescription(), lower) ||
                                contains(p.getPros(), lower)||
                                contains(p.getModemLinks(), lower)

                )
                .collect(Collectors.toList());
    }

    private boolean contains(String field, String query) {
        return field != null && field.toLowerCase().contains(query);
    }
}