package com.planwise.service;

import com.planwise.util.KMP;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    // Use KMP for substring count (use BoyerMoore if you want)
    public int countOccurrences(String text, String pattern) {
        return KMP.countOccurrences(text, pattern);
    }
}