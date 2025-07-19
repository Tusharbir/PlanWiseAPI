package com.planwise.spellchecker;

/**
 * The Levenshtein class provides a method to calculate the minimum number of single-character
 * edits (insertions, deletions, or substitutions) required to transform one string into another.
 * This optimized implementation uses two rolling arrays to reduce space complexity to O(min(n, m)).
 */
public class Levenshtein {

    /**
     * Computes the Levenshtein (edit) distance between two strings.
     * Uses dynamic programming with space optimization (two 1D arrays).
     *
     * @param source the original string
     * @param target the string to compare against
     * @return the edit distance between source and target
     */
    public static int distance(String source, String target) {
        int lenSrc = source.length();
        int lenTgt = target.length();

        // If one string is empty, distance is the length of the other (all inserts or deletes)
        if (lenSrc == 0) return lenTgt;
        if (lenTgt == 0) return lenSrc;

        // Ensure the shorter string defines the DP row length to minimize memory usage
        if (lenSrc < lenTgt) {
            // Swap source and target
            String temp = source;
            source = target;
            target = temp;
            lenSrc = source.length();
            lenTgt = target.length();
        }

        // Two arrays to hold the previous and current row of the DP table
        int[] prevRow = new int[lenTgt + 1];
        int[] currRow = new int[lenTgt + 1];

        // Initialize the base case: converting empty source to prefixes of target
        for (int j = 0; j <= lenTgt; j++) {
            prevRow[j] = j;  // j insertions
        }

        // Fill DP rows iteratively
        for (int i = 1; i <= lenSrc; i++) {
            // The cost of converting the first i chars of source to empty target = i deletions
            currRow[0] = i;
            char srcChar = source.charAt(i - 1);

            for (int j = 1; j <= lenTgt; j++) {
                char tgtChar = target.charAt(j - 1);
                // Cost is 0 if characters match, else 1 for substitution
                int cost = (srcChar == tgtChar) ? 0 : 1;

                // Calculate costs for deletion, insertion, and substitution
                int deleteCost = prevRow[j] + 1;
                int insertCost = currRow[j - 1] + 1;
                int replaceCost = prevRow[j - 1] + cost;

                // Choose the minimum of the three operations
                currRow[j] = Math.min(Math.min(deleteCost, insertCost), replaceCost);
            }

            // Swap the rows: current becomes previous for next iteration
            int[] temp = prevRow;
            prevRow = currRow;
            currRow = temp;
        }

        // The final distance is the cost to convert full source to full target
        return prevRow[lenTgt];
    }
}
