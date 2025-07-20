package com.planwise.controller;

import com.planwise.model.Plan;
import com.planwise.service.PlanService;
import com.planwise.service.SearchFrequencyService;
import com.planwise.service.SearchService;
import com.planwise.util.BoyerMoore;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/freq")
@CrossOrigin(origins = "*")
public class SearchFrequencyController {

    private final SearchService searchService;
    private final PlanService planService;
    private final SearchFrequencyService freqService;

    public SearchFrequencyController(SearchService searchService, PlanService planService, SearchFrequencyService freqService) {
        this.searchService = searchService;
        this.planService = planService;
        this.freqService = freqService;
    }

    @PostMapping("/increment")
    public Map<String, Object> increment(@RequestParam String keyword) {
        String normalized = keyword.toLowerCase();
        freqService.incrementFrequency(normalized);
        Map<String, Object> res = new HashMap<>();
        res.put("keyword", normalized);
        res.put("status", "incremented");
        res.put("frequency", freqService.getFrequency(normalized));
        return res;
    }

    @GetMapping("/get")
    public Map<String, Object> get(@RequestParam String keyword) {
        String normalized = keyword.toLowerCase();
        int freq = freqService.getFrequency(normalized);
        Map<String, Object> res = new HashMap<>();
        res.put("keyword", normalized);
        res.put("frequency", freq);
        return res;
    }

    @GetMapping("/count")
    public Map<String, Object> count(@RequestParam String keyword) {
        String normalized = keyword.toLowerCase();
        List<Plan> allPlans = planService.getAllPlans();
        StringBuilder allText = new StringBuilder();
        for (Plan plan : allPlans) {
            allText.append(Optional.ofNullable(plan.getSite()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getPlanName()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getDataLimit()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getPrice()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getDownloadSpeed()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getUploadSpeed()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getTechnology()).orElse("")).append(" ")
                   .append(Optional.ofNullable(plan.getFeatures()).orElse(" ")).append(" ");
        }
        String combined = allText.toString().toLowerCase();
        int occurrences = searchService.countOccurrences(combined, normalized);
        freqService.incrementFrequency(normalized);
        Map<String, Object> res = new HashMap<>();
        res.put("keyword", normalized);
        res.put("occurrences", occurrences);
        res.put("frequency", freqService.getFrequency(normalized));
        return res;
    }

    @GetMapping("/rank")
    public List<Map<String, Object>> rankPlansByKeyword(@RequestParam String keyword) {
        List<Plan> allPlans = planService.getAllPlans();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Plan plan : allPlans) {
            StringBuilder sb = new StringBuilder();
            sb.append(Optional.ofNullable(plan.getSite()).orElse("")).append(" ")
              .append(Optional.ofNullable(plan.getPlanName()).orElse("")).append(" ")
              .append(Optional.ofNullable(plan.getDataLimit()).orElse("")).append(" ")
              .append(Optional.ofNullable(plan.getPrice()).orElse("")).append(" ")
              .append(Optional.ofNullable(plan.getDownloadSpeed()).orElse("")).append(" ")
              .append(Optional.ofNullable(plan.getUploadSpeed()).orElse("")).append(" ")
              .append(Optional.ofNullable(plan.getTechnology()).orElse("")).append(" ")
              .append(Optional.ofNullable(plan.getFeatures()).orElse("")).append(" ");

            String text = sb.toString();
            int score = BoyerMoore.countOccurrences(text, keyword);

            if (score > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("plan", plan);
                map.put("score", score);
                result.add(map);
            }
        }
        result.sort((a, b) -> ((Integer) b.get("score")).compareTo((Integer) a.get("score")));
        return result;
    }
}