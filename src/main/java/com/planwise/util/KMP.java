package com.planwise.util;

public class KMP {
    public static int countOccurrences(String text, String pattern) {
        if (pattern == null || pattern.isEmpty() || text == null || text.isEmpty())
            return 0;
        String lowerText = text.toLowerCase();
        String lowerPattern = pattern.toLowerCase();

        int[] lps = buildLps(lowerPattern);
        int i = 0, j = 0, count = 0;
        int n = lowerText.length(), m = lowerPattern.length();

        while (i < n) {
            if (lowerText.charAt(i) == lowerPattern.charAt(j)) {
                i++; j++;
                if (j == m) {
                    count++;
                    j = lps[j - 1]; // Prepare for next match
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return count;
    }

    private static int[] buildLps(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        lps[0] = 0;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}