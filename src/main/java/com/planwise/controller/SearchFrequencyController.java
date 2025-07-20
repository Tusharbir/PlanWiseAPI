package com.planwise.controller;

import com.planwise.model.Plan;
import com.planwise.service.PlanService;
import com.planwise.service.SearchService;
import com.planwise.util.BoyerMoore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/freq")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchFrequencyController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private PlanService planService; // Service to access all plans

    /**
     * POST /api/freq/increment?keyword=xxx
     * Increments the search frequency for the keyword.
     */
    @PostMapping("/increment")
    public Map<String, Object> increment(@RequestParam String keyword) {
        String normalized = keyword.toLowerCase();
        searchService.incrementFrequency(normalized);
        Map<String, Object> res = new HashMap<>();
        res.put("keyword", normalized);
        res.put("status", "incremented");
        res.put("frequency", searchService.getFrequency(normalized));
        return res;
    }

    /**
     * GET /api/freq/get?keyword=xxx
     * Gets the current search frequency for the keyword.
     */
    @GetMapping("/get")
    public Map<String, Object> get(@RequestParam String keyword) {
        String normalized = keyword.toLowerCase();
        int freq = searchService.getFrequency(normalized);
        Map<String, Object> res = new HashMap<>();
        res.put("keyword", normalized);
        res.put("frequency", freq);
        return res;
    }

    /**
     * GET /api/freq/count?keyword=xxx
     * Returns how many times the keyword appears in all plan data (substring search).
     * Also returns the current search frequency for analytics.
     */
    @GetMapping("/count")
    public Map<String, Object> count(@RequestParam String keyword) {
        String normalized = keyword.toLowerCase();

        // 1. Fetch all plans
        List<Plan> allPlans = planService.getAllPlans();

        // 2. Combine all relevant fields into one string for search (NULL safe)
        StringBuilder allText = new StringBuilder();
        for (Plan plan : allPlans) {
            allText.append(Optional.ofNullable(plan.getCompany()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getPlanName()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getDataLimited()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getPrice()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getDownloadSpeed()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getUploadSpeed()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getTechnology()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getFeatures()).orElse(" ")).append(" ");
        }
        String combined = allText.toString().toLowerCase();

        // 3. Count occurrences (KMP substring search)
        int occurrences = searchService.countOccurrences(combined, normalized);

        // 4. Increment frequency for analytics
        searchService.incrementFrequency(normalized);

        // 5. Prepare JSON response
        Map<String, Object> res = new HashMap<>();
        res.put("keyword", normalized);
        res.put("occurrences", occurrences);
        res.put("frequency", searchService.getFrequency(normalized));
        return res;
    }

    @GetMapping("/rank")
    public List<Map<String, Object>> rankPlansByKeyword(@RequestParam String keyword) {
        List<Plan> allPlans = planService.getAllPlans();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Plan plan : allPlans) {
            // Combine plan fields you want to search in
            StringBuilder sb = new StringBuilder();
            sb.append(Optional.ofNullable(plan.getCompany()).orElse("")).append(" ")
            .append(Optional.ofNullable(plan.getPlanName()).orElse("")).append(" ")
            .append(Optional.ofNullable(plan.getDataLimited()).orElse("")).append(" ")
            .append(Optional.ofNullable(plan.getPrice()).orElse("")).append(" ")
            .append(Optional.ofNullable(plan.getDownloadSpeed()).orElse("")).append(" ")
            .append(Optional.ofNullable(plan.getUploadSpeed()).orElse("")).append(" ")
            .append(Optional.ofNullable(plan.getTechnology()).orElse("")).append(" ")
            .append(Optional.ofNullable(plan.getFeatures()).orElse("")).append(" ");

            String text = sb.toString();
            int score = BoyerMoore.countOccurrences(text, keyword);

            if (score > 0) { // Only include relevant plans
                Map<String, Object> map = new HashMap<>();
                map.put("plan", plan);
                map.put("score", score);
                result.add(map);
            }
        }

        // Sort by score descending
        result.sort((a, b) -> ((Integer)b.get("score")).compareTo((Integer)a.get("score")));
        return result;
    }
}