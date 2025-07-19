package com.planwise.controller;

import com.planwise.service.SpellCheckerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class SpellCheckController {

    private final SpellCheckerService svc;

    public SpellCheckController(SpellCheckerService svc) {
        this.svc = svc;
    }

    /**
     * GET /api/spellcheck?word={word}&max={n}
     */
    @GetMapping("/spellcheck")
    public List<String> spellcheck(
            @RequestParam("word") String word,
            @RequestParam(value="max", defaultValue="5") int maxCount) {
        return svc.suggest(word, maxCount);
    }
}
