package com.planwise.featurecode;

import java.util.*;

/**
 * A custom-built autocomplete system using an AVL Tree.
 * Stores words along with their frequency and suggests
 * completions based on prefix input.
 */
public class AVLTree {

    /** Inner class representing a node in the AVL Tree. */
    class Entry {
        String term;          // Word stored in the node
        int count;            // Frequency of the word
        int depth;            // Height of the node
        Entry leftLink;       // Left child
        Entry rightLink;      // Right child

        /** Constructor to initialize a new tree node. */
        Entry(String term, int count) {
            this.term = term;
            this.count = count;
            this.depth = 1;
        }
    }

    private Entry top; // Root node of the AVL Tree

    /** Inserts a word into the AVL Tree. */
    public void push(String term, int count) {
        top = place(top, term, count);
    }

    /** Recursively inserts a word into the tree and rebalances it. */
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

    /** Updates the height of the node. */
    private void updateDepth(Entry node) {
        node.depth = 1 + Math.max(depth(node.leftLink), depth(node.rightLink));
    }

    /** Returns the height of a node. */
    private int depth(Entry node) {
        return (node == null) ? 0 : node.depth;
    }

    /** Computes the balance factor of a node. */
    private int difference(Entry node) {
        return (node == null) ? 0 : depth(node.leftLink) - depth(node.rightLink);
    }

    /** Performs a right rotation. */
    private Entry rotateRight(Entry y) {
        Entry x = y.leftLink;
        Entry T2 = x.rightLink;
        x.rightLink = y;
        y.leftLink = T2;

        updateDepth(y);
        updateDepth(x);
        return x;
    }

    /** Performs a left rotation. */
    private Entry rotateLeft(Entry x) {
        Entry y = x.rightLink;
        Entry T2 = y.leftLink;
        y.leftLink = x;
        x.rightLink = T2;

        updateDepth(x);
        updateDepth(y);
        return y;
    }

    /** Balances the tree after insertion to maintain AVL property. */
    private Entry balanceTree(Entry node, String term) {
        int balance = difference(node);

        // Left heavy
        if (balance > 1 && term.compareTo(node.leftLink.term) < 0)
            return rotateRight(node);

        // Right heavy
        if (balance < -1 && term.compareTo(node.rightLink.term) > 0)
            return rotateLeft(node);

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

    /** Prints all stored words in alphabetical order. */
    public void printTree() {
        explore(top);
    }

    /** Helper function to perform in-order traversal. */
    private void explore(Entry node) {
        if (node != null) {
            explore(node.leftLink);
            System.out.println(node.term + " (" + node.count + ")");
            explore(node.rightLink);
        }
    }

    /**
     * Returns a list of suggested words based on the input prefix.
     * Modified to collect all terms that start with the prefix, up to maxCount.
     */
    public List<String> predict(String prefix, int maxCount) {
        List<String> results = new ArrayList<>();
        collectMatches(top, prefix, results, maxCount);
        return results;
    }

    /**
     * Traverses the tree to find matching words with the prefix.
     * Only descends into subtrees that can contain matching prefixes.
     */
    private void collectMatches(Entry node, String prefix, List<String> results, int limit) {
        if (node == null || results.size() >= limit) return;

        // If node.term could be in left subtree
        if (node.term.compareTo(prefix) >= 0) {
            collectMatches(node.leftLink, prefix, results, limit);
        }

        // Add if it matches
        if (node.term.startsWith(prefix)) {
            results.add(node.term);
        }

        // If node.term could be in right subtree
        if (node.term.compareTo(prefix) <= 0) {
            collectMatches(node.rightLink, prefix, results, limit);
        }
    }

    /**
     * Entry point to test the AVL-based autocomplete engine via console.
     */
    public static void main(String[] args) {
        String file = "merged-csv.csv";
        Map<String, Integer> terms = Extractor.extractVocabulary(file);

        AVLTree engine = new AVLTree();
        terms.forEach(engine::push);

        System.out.println("AVL Tree (in alphabetic order):");
        engine.printTree();

        Scanner input = new Scanner(System.in);
        System.out.println("\nEnter a word prefix (or 'exit' to stop):");

        while (true) {
            System.out.print("Your prefix: ");
            String typed = input.nextLine().trim().toLowerCase();
            if (typed.equals("exit")) break;

            List<String> found = engine.predict(typed, 5);
            if (found.isEmpty()) {
                System.out.println("No results.");
            } else {
                System.out.println("Suggestions:");
                found.forEach(s -> System.out.println("- " + s));
            }
        }

        input.close();
    }
}