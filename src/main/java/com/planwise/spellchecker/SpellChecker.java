package com.planwise.spellchecker;

import java.util.List;

// Provides suggestions for misspelled words based on a Trie and edit distance
public class SpellChecker {

    // Suggests up to maxSuggestions words closest to the input
    public static List<String> suggest(String input,
                                       int maxDistance,
                                       int maxSuggestions,
                                       Trie trie) {
        // fetch raw candidates within maxDistance edits
        List<String> candidates = trie.suggestByEditDistance(input, maxDistance);

        // sort by fewest edits needed, then alphabetically
        candidates.sort((a, b) -> {
            int da = Levenshtein.distance(a, input);
            int db = Levenshtein.distance(b, input);
            if (da != db)
                return Integer.compare(da, db);
            return a.compareTo(b);
        });

        // limit to maxSuggestions items and return
        return candidates.size() <= maxSuggestions
                ? candidates
                : candidates.subList(0, maxSuggestions);
    }
}
