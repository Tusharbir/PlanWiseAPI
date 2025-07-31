package com.planwise.spellchecker;

/**
 * Provides a static method to compute the Levenshtein edit distance between two strings.
 * Edit distance is the minimum number of single-character insertions, deletions, or substitutions
 * required to transform one string into another.
 */
public class Levenshtein {
    /**
     * Computes the Levenshtein distance between two strings.
     *
     * @param source the original string; if null, treated as empty
     * @param target the string to compare; if null, treated as empty
     * @return the minimum number of edits (insertions, deletions, substitutions) to transform source into target
     */
    public static int distance(String source, String target) {
        if (source == null) source = "";
        if (target == null) target = "";

        int lenSrc = source.length();
        int lenTgt = target.length();
        if (lenSrc == 0) return lenTgt;
        if (lenTgt == 0) return lenSrc;

        // Ensure the shorter string drives the outer loop to reduce memory usage
        if (lenSrc > lenTgt) {
            String tmp = source;
            source = target;
            target = tmp;
            lenSrc = source.length();
            lenTgt = target.length();
        }

        int[] prevRow = new int[lenTgt + 1];
        int[] currRow = new int[lenTgt + 1];

        // Initialize base distances from an empty source to prefixes of the target
        for (int j = 0; j <= lenTgt; j++) {
            prevRow[j] = j;
        }

        // Build each distance row iteratively
        for (int i = 1; i <= lenSrc; i++) {
            currRow[0] = i;  // cost of deleting all characters up to position i
            char srcChar = source.charAt(i - 1);

            for (int j = 1; j <= lenTgt; j++) {
                char tgtChar = target.charAt(j - 1);
                int deleteCost = prevRow[j] + 1;      // deletion of srcChar
                int insertCost = currRow[j - 1] + 1;  // insertion of tgtChar
                int replaceCost = prevRow[j - 1] + ((srcChar == tgtChar) ? 0 : 1); // substitution

                // choose the minimum of delete, insert, and replace operations
                currRow[j] = Math.min(Math.min(deleteCost, insertCost), replaceCost);
            }

            // Swap rows to reuse arrays without extra allocation
            int[] temp = prevRow;
            prevRow = currRow;
            currRow = temp;
        }

        // The last cell of prevRow holds the final edit distance
        return prevRow[lenTgt];
    }
}