package com.planwise.service;

import com.planwise.model.SearchFrequency;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SearchFrequencyService {
    private final ConcurrentHashMap<String, SearchFrequency> freqMap = new ConcurrentHashMap<>();

    public void incrementFrequency(String keyword) {
        freqMap.compute(keyword, (k, v) -> {
            if (v == null) {
                return new SearchFrequency(k, 1);
            } else {
                v.setCount(v.getCount() + 1);
                return v;
            }
        });
    }

    public int getFrequency(String keyword) {
        SearchFrequency freq = freqMap.get(keyword);
        return freq == null ? 0 : freq.getCount();
    }
}