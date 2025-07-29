package com.planwise.service;

import com.planwise.autocomplete.AVLTree;
import com.planwise.autocomplete.Extractor;
import com.planwise.model.AutoCompleteRsult;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
public class WordCompletionService {

//    private final AVLTree engine = new AVLTree();

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
    @PostConstruct
    public void init() {
        try {
            // Load merged CSV from classpath
            String resource = "merged-csv.csv";
            URL url = getClass().getClassLoader().getResource(resource);
            if (url == null) {
                throw new IllegalStateException("Could not find " + resource);
            }

            // Build vocabulary from all columns
            Map<String,Integer> vocab = Extractor.extractVocabulary(resource);

            // Push into AVL: token → frequency
            vocab.forEach((token, freq) -> engine.push(token, freq));

            System.out.println("Autocomplete engine loaded " + vocab.size() + " tokens");
        } catch (Exception e) {
            e.printStackTrace();
        }
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