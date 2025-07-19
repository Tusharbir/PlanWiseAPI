package com.planwise.spellchecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Trie implements a prefix tree data structure for storing strings.
 * Each node contains a map of child characters to TrieNode,
 * and a boolean flag marking the end of a valid word.
 */
public class Trie {
    // Root node of the Trie (represents empty prefix)
    private final TrieNode root = new TrieNode();

    /**
     * Inserts a word into the Trie.
     * Iterates over each character, creating child nodes as needed.
     * Marks the final node as a word end.
     *
     * @param word the lowercase string to insert
     */
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            // Fetch or create the child node for this character
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        // Mark this node as the end of a valid word
        node.isWordEnd = true;
    }

    /**
     * Checks if an exact word exists in the Trie.
     * Follows the character path; returns true only if the final node
     * is marked as a word end.
     *
     * @param word the lowercase string to search
     * @return true if the word exists end-to-end in the Trie
     */
    public boolean contains(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                // Missing path for this character
                return false;
            }
        }
        // Word exists only if the final node is flagged
        return node.isWordEnd;
    }

    /**
     * Gathers all words in the Trie within a given maximum
     * Levenshtein edit distance from the target string.
     * This is a brute-force DFS that computes full edit distance
     * at each word-end node.
     *
     * @param target      the string to compare against
     * @param maxDistance maximum allowed edit distance
     * @return list of matching words
     */
    public List<String> suggestByEditDistance(String target, int maxDistance) {
        List<String> results = new ArrayList<>();
        // Start recursion from the root, with empty prefix
        searchRecursive(root, new StringBuilder(), target, maxDistance, results);
        return results;
    }

    /**
     * Recursive helper for suggestByEditDistance.
     * Prunes branches where prefix is too long relative to target + maxDist.
     * Computes full edit distance whenever a word-end node is reached.
     *
     * @param node     current TrieNode
     * @param prefix   StringBuilder accumulating current path
     * @param target   target string to compare
     * @param maxDist  maximum allowed edit distance
     * @param out      list to collect valid suggestions
     */
    private void searchRecursive(TrieNode node,
                                 StringBuilder prefix,
                                 String target,
                                 int maxDist,
                                 List<String> out) {
        // Prune any prefix longer than target length + allowed edits
        if (prefix.length() > target.length() + maxDist) {
            return;
        }

        // If this node marks a complete word, compute its edit distance
        if (node.isWordEnd) {
            String word = prefix.toString();
            int dist = Levenshtein.distance(word, target);
            if (dist <= maxDist) {
                out.add(word);
            }
        }

        // Recurse for each child character
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            prefix.append(entry.getKey());
            searchRecursive(entry.getValue(), prefix, target, maxDist, out);
            // Backtrack to previous state
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
