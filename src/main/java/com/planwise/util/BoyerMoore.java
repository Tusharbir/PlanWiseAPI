package com.planwise.util;

import java.util.HashMap;
import java.util.Map;

public class BoyerMoore {

    public static int countOccurrences(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) return 0;

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        // Build the bad character table for ALL possible characters
        Map<Character, Integer> badChar = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            badChar.put(pattern.charAt(i), i);
        }

        int count = 0;
        int shift = 0;
        while (shift <= (text.length() - pattern.length())) {
            int j = pattern.length() - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j))
                j--;
            if (j < 0) {
                count++;
                if (shift + pattern.length() < text.length()) {
                    char nextChar = text.charAt(shift + pattern.length());
                    int skip = pattern.length() - badChar.getOrDefault(nextChar, -1);
                    shift += skip;
                } else {
                    shift += 1;
                }
            } else {
                char badCharAt = text.charAt(shift + j);
                int skip = Math.max(1, j - badChar.getOrDefault(badCharAt, -1));
                shift += skip;
            }
        }
        return count;
    }
}