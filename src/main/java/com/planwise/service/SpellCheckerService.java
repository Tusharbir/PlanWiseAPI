package com.planwise.service;


import com.planwise.spellchecker.SpellChecker;
import com.planwise.spellchecker.Trie;
import com.planwise.utilities.UtilFunc;

import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SpellCheckerService {

    private final int maxDistance = 3;
    private final Trie trie = new Trie();

    @EventListener(ApplicationReadyEvent.class)
    public void startUp(){
        Map<String,Integer> vocab = UtilFunc.loadVocabulary();
        vocab.keySet().forEach(trie::insert);
    }

    /**
     * Suggest corrections for ‘word’, returning up to maxCount items.
     */
    public List<String> suggest(String word, int maxCount) {
        if (word == null || word.isBlank()) return List.of();
        String w = word.trim().toLowerCase();
        // maxDistance = 3 edits, maxSuggestions = maxCount
        return SpellChecker.suggest(w, maxDistance, maxCount, trie);
    }
}