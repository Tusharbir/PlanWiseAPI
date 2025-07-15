package com.planwise.controller;

import com.planwise.model.Plan;
import com.planwise.service.DataLoaderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    private final DataLoaderService dataLoader;

    public SearchController(DataLoaderService dataLoader) {
        this.dataLoader = dataLoader;
    }

    @GetMapping("/plans")
    public List<Plan> getPlans(@RequestParam String site) {
        System.out.println("Received request for site: " + site);
        List<Plan> plans = dataLoader.getPlansBySite(site);
        System.out.println("Returning " + plans.size() + " plans for site: " + site);
        return plans;
    }

    @GetMapping("/sites")
    public Set<String> getAvailableSites() {
        Set<String> sites = dataLoader.getAllSites();
        System.out.println("Available sites endpoint called, returning: " + sites);
        return sites;
    }

    // Add debug endpoint to help troubleshoot
    @GetMapping("/debug")
    public String debug() {
        Set<String> sites = dataLoader.getAllSites();
        StringBuilder sb = new StringBuilder();
        sb.append("Available sites: ").append(sites).append("\n");

        for (String site : sites) {
            List<Plan> plans = dataLoader.getPlansBySite(site);
            sb.append("Site: ").append(site).append(" has ").append(plans.size()).append(" plans\n");
            for (Plan plan : plans) {
                sb.append("  - ").append(plan.getPlanName()).append("\n");
            }
        }

        return sb.toString();
    }
}