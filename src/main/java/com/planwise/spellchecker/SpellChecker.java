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
     * Entry point. Builds the Trie, loads vocabulary, and starts the CLI loop.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Trie trie = new Trie();
        List<String> vocabulary = new ArrayList<>();

        // Load all CSV tokens into the trie and vocabulary list
        loadVocabulary("/merged-csv.csv", trie, vocabulary);

        // Inform the user how many entries are available
        System.out.println("Loaded " + vocabulary.size() + " words into dictionary.\n");

        // Launch interactive spell-check session
        runInteractive(trie, vocabulary);
    }

    /**
     * Reads every cell from the specified CSV resource, tokenizes each cell on
     * non-word characters, deduplicates tokens, and populates the given Trie
     * and vocabulary list.
     *
     * @param resourcePath Classpath resource path to the CSV (e.g., "/merged-csv.csv").
     * @param trie         The Trie instance to populate.
     * @param vocab        The List to store unique vocabulary tokens for reference.
     */
    private static void loadVocabulary(String resourcePath, Trie trie, List<String> vocab) {
        Set<String> uniqueWords = new LinkedHashSet<>();
        try (InputStream in = SpellChecker.class.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            // Skip the header row (column names)
            String header = reader.readLine();
            if (header == null) {
                System.err.println("Error: CSV is empty or not found.");
                System.exit(1);
            }

            // Process each line and extract tokens
            String line;
            while ((line = reader.readLine()) != null) {
                // Split row into individual cell values
                String[] cells = line.split(",");
                for (String cell : cells) {
                    // Tokenize cell by any non-alphanumeric characters
                    String[] tokens = cell.toLowerCase().split("\\W+");
                    for (String token : tokens) {
                        if (!token.isEmpty()) {
                            uniqueWords.add(token);
                        }
                    }
                }
            }

            // Insert each unique token into the Trie and vocabulary list
            for (String word : uniqueWords) {
                trie.insert(word);
                vocab.add(word);
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading vocabulary: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Initiates an interactive loop that prompts the user to enter words to check.
     * For each entry:
     * - Exact matches print a confirmation.
     * - Non-matches invoke suggestion logic.
     * Pressing Enter on a blank line exits the loop.
     *
     * @param trie  The Trie containing the loaded vocabulary.
     * @param vocab The list of tokens used for suggestions.
     */
    private static void runInteractive(Trie trie, List<String> vocab) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a word (press Enter to exit): ");
            String input = scanner.nextLine().trim().toLowerCase();

            // Exit condition
            if (input.isEmpty()) {
                System.out.println("Goodbye!");
                break;
            }

            // Check for exact match in Trie
            if (trie.contains(input)) {
                System.out.println("✔️  '" + input + "' is spelled correctly.\n");
            } else {
                System.out.println("❌  '" + input + "' not found.");
                suggestByEditDistance(input, vocab, trie);
            }
        }
        scanner.close();
    }

    /**
     * Suggests up to five alternative words for the given input based solely
     * on the Levenshtein edit distance algorithm (max distance = 2).
     * Results are sorted first by ascending edit distance, then alphabetically.
     *
     * @param input The word to find suggestions for.
     * @param vocab The list of all tokens in the vocabulary.
     * @param trie  The Trie used to compute candidate suggestions.
     */
    private static void suggestByEditDistance(String input, List<String> vocab, Trie trie) {
        List<String> candidates = trie.suggestByEditDistance(input, 2);

        // Sort candidates by distance and lexicographic order
        candidates.sort((a, b) -> {
            int da = Levenshtein.distance(a, input);
            int db = Levenshtein.distance(b, input);
            if (da != db) return Integer.compare(da, db);
            return a.compareTo(b);
        });

        if (candidates.isEmpty()) {
            System.out.println("No suggestions available.\n");
        } else {
            System.out.println("Suggestions (Edit Distance ≤ 2):");
            // Display up to five suggestions
            candidates.stream()
                      .limit(5)
                      .forEach(s -> System.out.println(" - " + s));
            System.out.println();
        }
    }

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
