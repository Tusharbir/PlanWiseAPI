package com.planwise.service;

import com.planwise.autocomplete.Extractor;
import com.planwise.spellchecker.SpellChecker;
import com.planwise.spellchecker.Trie;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SpellCheckerService {

    private final int maxDistance = 5;
    private final Trie trie = new Trie();

    @EventListener(ApplicationReadyEvent.class)
    public void loadVocabulary() {
        System.out.println("Loading vocabulary...");
        // 1) Pull every token from the merged CSV
        Map<String,Integer> vocab = Extractor.extractVocabulary("merged-csv.csv");
        // 2) Insert into your Trie
        vocab.keySet().forEach(trie::insert);
    }

    /**
     * Suggest corrections for ‘word’, returning up to maxCount items.
     */
    public List<String> suggest(String word, int maxCount) {
        if (word == null || word.isBlank()) return List.of();
        String w = word.trim().toLowerCase();
        // maxDistance = 2 edits, maxSuggestions = maxCount
        return SpellChecker.suggest(w, maxDistance, maxCount, trie);
    }
}