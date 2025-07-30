package com.planwise.autocomplete;

import com.planwise.model.AutoCompleteRsult;
import com.planwise.service.SearchFrequencyService;

import java.util.*;

/**
 * autocomplete system using an AVL Tree.
 * Stores words along with their frequency and suggests
 * completions based on prefix input.
 */
public class AVLTree {

    private final SearchFrequencyService searchFreqService;

    // Constructor to inject the service
    public AVLTree(SearchFrequencyService searchFreqService) {
        this.searchFreqService = searchFreqService;
    }


    /**
     * Inner class representing a node in the AVL Tree.
     */
    static class Entry {
        String term;          // Word stored in the node
        int count;            // Frequency of the word
        int depth;            // Height of the node
        Entry leftLink;       // Left child
        Entry rightLink;      // Right child

        /**
         * Constructor to initialize a new tree node.
         */
        Entry(String term, int count) {
            this.term = term;
            this.count = count;
            this.depth = 1;
        }
    }

    private Entry top; // Root node of the AVL Tree

    /**
     * Inserts a word into the AVL Tree.
     */
    public void push(String term, int count) {
        top = place(top, term, count);
    }

    /**
     * Recursively inserts a word into the tree and rebalances it.i
     */
    private Entry place(Entry spot, String term, int count) {
        if (spot == null) return new Entry(term, count);

        int cmp = term.compareTo(spot.term);
        if (cmp < 0) {
            spot.leftLink = place(spot.leftLink, term, count);
        } else if (cmp > 0) {
            spot.rightLink = place(spot.rightLink, term, count);
        } else {
            spot.count += count; // Update frequency if word already exists
            return spot;
        }

        updateDepth(spot);
        return balanceTree(spot, term);
    }

    /**
     * Updates the height of the node.
     */
    private void updateDepth(Entry node)
    {
        node.depth = 1 + Math.max(depth(node.leftLink), depth(node.rightLink));
    }

    /**
     * Returns the height of a node.
     */
    private int depth(Entry node) {

        return (node == null) ? 0 : node.depth;
    }

    /**
     * Computes the balance factor of a node.
     */
    private int difference(Entry node) {
        return (node == null) ? 0 : depth(node.leftLink) - depth(node.rightLink);
    }

    /**
     * Performs a right rotation.
     */
    private Entry rotateRight(Entry y) {
        Entry x = y.leftLink;
        Entry T2 = x.rightLink;
        x.rightLink = y;
        y.leftLink = T2;

        updateDepth(y);
        updateDepth(x);
        return x;
    }

    /**
     * Performs a left rotation.
     */
    private Entry rotateLeft(Entry x) {
        Entry y = x.rightLink;
        Entry T2 = y.leftLink;
        y.leftLink = x;
        x.rightLink = T2;

        updateDepth(x);
        updateDepth(y);
        return y;
    }

    /**
     * Balances the tree after insertion to maintain AVL property.
     */
    private Entry balanceTree(Entry node, String term) {
        int balance = difference(node);

        // Left heavy
        if (balance > 1 && term.compareTo(node.leftLink.term) < 0) return rotateRight(node);

        // Right heavy
        if (balance < -1 && term.compareTo(node.rightLink.term) > 0) return rotateLeft(node);

        // Left-Right case
        if (balance > 1 && term.compareTo(node.leftLink.term) > 0) {
            node.leftLink = rotateLeft(node.leftLink);
            return rotateRight(node);
        }

        // Right-Left case
        if (balance < -1 && term.compareTo(node.rightLink.term) < 0) {
            node.rightLink = rotateRight(node.rightLink);
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Prints all stored words in alphabetical order.
     */
//    public void printTree() {
//        explore(top);
//    }

//    /**perform in-order traversal.

//     */
//    private void explore(Entry node) {
//        if (node != null) {
//            explore(node.leftLink);
//            System.out.println(node.term + " (" + node.count + ")");
//            explore(node.rightLink);
//        }
//    }

    /**
     * Returns a list of suggested words based on the input prefix.
     * Modified to collect all terms that start with the prefix, up to maxCount.
     */
    public List<AutoCompleteRsult> predict(String prefix, int maxCount) {
        List<AutoCompleteRsult> results = new ArrayList<>();
        collectMatches(top, prefix, results, maxCount);
        return results;
    }

    /**
     * Traverses the tree to find matching words with the prefix.
     * Only descends into subtrees that can contain matching prefixes.
     */

    private void collectMatches(Entry node, String prefix, List<AutoCompleteRsult> results, int limit) {
        if (node == null || results.size() >= limit) return;

        // Create the "end" of the prefix range for comparison
        // For example, if prefix is "str", we want to find all words from "str" to "str" + maxChar
        String prefixEnd = prefix + Character.MAX_VALUE;

        // Only search left subtree if it could contain matches
        // (if the current node's term is greater than or equal to our prefix)
        if (node.term.compareTo(prefix) >= 0) {
            collectMatches(node.leftLink, prefix, results, limit);
        }

        // Stop if we've reached the limit
        if (results.size() >= limit) return;

        // Check if current node matches the prefix
        if (node.term.startsWith(prefix)) {
            results.add(new AutoCompleteRsult(node.term, node.count, getSearchFrequency(node.term)));
        }

        // Stop if we've reached the limit
        if (results.size() >= limit) return;

        // Only search right subtree if it could contain matches
        // (if the current node's term is less than our prefix end range)
        if (node.term.compareTo(prefixEnd) < 0) {
            collectMatches(node.rightLink, prefix, results, limit);
        }
    }

    private int getSearchFrequency(String term) {
        if (searchFreqService == null) {
            return 0; // Return 0 if service is not available
        }
        return searchFreqService.getFrequency(term);
    }


}