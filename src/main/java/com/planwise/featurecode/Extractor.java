package com.planwise.featurecode;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extractor reads a CSV file and extracts word frequencies from specific columns.
 * It combines text from 'Plan Name', 'Features', and 'Description' fields,
 * then counts and returns the frequency of each word.
 */
public class Extractor {

    /**
     * Extracts vocabulary from the given CSV file located in resources folder.
     * @param fileName Name of the CSV file (must be in resources directory)
     * @return Map containing words as keys and their frequencies as values
     */
    public static Map<String, Integer> extractVocabulary(String fileName) {
        Map<String, Integer> wordFrequency = new HashMap<>();

        try {
            // Load the file from the classpath (resources folder)
            InputStream inputStream = Extractor.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                System.out.println("File not found in resources: " + fileName);
                return wordFrequency;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            br.readLine();

// For each data row, split on commas **outside** quotes,
// then tokenize **every** field into [a–z0–9] words of length ≥2:
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(
                        ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)",  // handles quoted commas
                        -1
                );

                for (String field : columns) {
                    if (field == null || field.isBlank()) continue;

                    // Extract alphanumeric tokens, length ≥ 2
                    Matcher matcher = Pattern.compile("\\b[a-z0-9]{2,}\\b")
                            .matcher(field.toLowerCase());
                    while (matcher.find()) {
                        String token = matcher.group();
                        wordFrequency.put(token,
                                wordFrequency.getOrDefault(token, 0) + 1);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }

        return wordFrequency;
    }

    /**
     * Main method to test the vocabulary extraction from CSV.
     * Displays the top 20 most frequent words.
     */
    public static void main(String[] args) {
        String fileName = "merged-csv.csv"; // File must be located in src/main/resources

        Map<String, Integer> vocab = Extractor.extractVocabulary(fileName);

        vocab.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
}