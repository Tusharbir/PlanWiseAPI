package com.planwise.spellchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * The SpellChecker class implements an interactive spell-checking
 * application that reads vocabulary entries from a CSV file into
 * a Trie data structure and suggests alternative words based solely
 * on the Levenshtein edit distance algorithm.
 *
 * Usage:
 * 1. Place the "merged-csv.csv" file in the classpath under /resources.
 * 2. Run the application.
 * 3. Enter words at the prompt; press Enter on an empty line to exit.
 *
 * Key features:
 * - Efficient prefix-based word lookup via Trie.
 * - Edit-distance-based suggestions (max distance = 2).
 * - Limits output to the top 5 closest matches.
 */
public class SpellChecker {

    /**
     * Return up to maxSuggestions words within editDistance of input,
     * ranked by (distance asc, then lex order).
     */
    public static List<String> suggest(String input,
                                       int maxDistance,
                                       int maxSuggestions,
                                       Trie trie) {
        // 1) Generate raw candidates from trie
        List<String> candidates = trie.suggestByEditDistance(input, maxDistance);

        // 2) Sort by distance then lex:
        candidates.sort((a,b) -> {
            int da = Levenshtein.distance(a, input);
            int db = Levenshtein.distance(b, input);
            if (da != db) return Integer.compare(da, db);
            return a.compareTo(b);
        });

        // 3) Trim to maxSuggestions
        return candidates.size() <= maxSuggestions
                ? candidates
                : candidates.subList(0, maxSuggestions);
    }
}
