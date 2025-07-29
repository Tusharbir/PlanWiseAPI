package com.planwise.controller;

import com.planwise.model.AutoCompleteRsult;
import com.planwise.service.WordCompletionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AutocompleteController {

    private final WordCompletionService autoService;

    public AutocompleteController(WordCompletionService autoService) {
        this.autoService = autoService;
    }

    /**
     * GET /api/autocomplete?prefix={prefix}&max={maxCount}
     * Returns up to maxCount suggestions for the given prefix.
     */
    @GetMapping("/autocomplete")
    public List<AutoCompleteRsult> autocomplete(
            @RequestParam("prefix") String prefix,
            @RequestParam(value = "max", defaultValue = "5") int maxCount) {
        return autoService.complete(prefix, maxCount);
    }
}