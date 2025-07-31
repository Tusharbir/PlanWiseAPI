package com.planwise.service;

import com.planwise.autocomplete.AVLTree;

import com.planwise.model.AutoCompleteRsult;
import com.planwise.utilities.UtilFunc;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WordCompletionService {



    private final AVLTree engine;

    private final SearchFrequencyService searchFreqService;

    @Autowired
    public WordCompletionService(SearchFrequencyService searchFreqService) {
        this.searchFreqService = searchFreqService;

        // Initialize AVLTree with the search frequency service
        this.engine = new AVLTree(searchFreqService);
    }
    /**
     * On startup, read the CSV via Extractor and feed every token into the AVL.
     */

    @EventListener(ApplicationReadyEvent.class)
    public void startUp() {
        Map<String,Integer> vocab = UtilFunc.loadVocabulary();
        vocab.forEach((token, freq) -> engine.push(token, freq));
//        engine.explore();
    }

    /**
     * Return up to maxCount completions for the given prefix.
     * Filters out blank or symbol-only prefixes.
     */
    public List<AutoCompleteRsult> complete(String prefix, int maxCount) {
        if (prefix == null || prefix.isBlank() ||
                !prefix.matches(".*[A-Za-z0-9].*")) {
            return List.of();
        }
        return engine.predict(prefix.toLowerCase(), maxCount);
    }
}