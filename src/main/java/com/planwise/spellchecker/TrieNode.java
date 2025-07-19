package com.planwise.spellchecker;

import java.util.HashMap;
import java.util.Map;

/**
 * TrieNode represents a single node in a Trie (prefix tree).
 * Each node maintains a map of child characters to subsequent TrieNodes,
 * and a flag indicating whether this node marks the end of a valid word.
 */
public class TrieNode {
    /**
     * Child nodes keyed by character. Each entry represents one possible
     * next character in the prefix tree.
     */
    Map<Character, TrieNode> children = new HashMap<>();

    /**
     * Indicates whether this node corresponds to the end of a stored word.
     * If true, the path from the root to this node spells a complete vocabulary word.
     */
    boolean isWordEnd = false;

    /**
     * Default constructor initializes an empty children map and sets
     * isWordEnd to false by default.
     */
    public TrieNode() {
        // children map and isWordEnd flag are initialized above
    }
}
