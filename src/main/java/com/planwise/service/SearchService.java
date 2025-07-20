package com.planwise.service;

import com.planwise.model.SearchFrequency;
import com.planwise.repository.SearchFrequencyRepository;
import com.planwise.util.KMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchService {

    @Autowired
    private SearchFrequencyRepository freqRepo;

    /**
     * Counts how many times 'pattern' occurs in 'text' using the KMP algorithm.
     * Delegates to com.planwise.util.KMP.
     */
    public int countOccurrences(String text, String pattern) {
        return KMP.countOccurrences(text, pattern);
    }

    /**
     * Increments the search frequency for a keyword in the DB.
     * Creates a new entry if it does not exist.
     */
    @Transactional
    public void incrementFrequency(String keyword) {
        String norm = keyword.toLowerCase();
        SearchFrequency freq = freqRepo.findById(norm)
                .orElse(new SearchFrequency(norm, 0));
        freq.setCount(freq.getCount() + 1);
        freqRepo.save(freq);
    }

    /**
     * Returns how many times a keyword has been searched (from DB).
     */
    public int getFrequency(String keyword) {
        String norm = keyword.toLowerCase();
        return freqRepo.findById(norm)
                .map(SearchFrequency::getCount)
                .orElse(0);
    }
}