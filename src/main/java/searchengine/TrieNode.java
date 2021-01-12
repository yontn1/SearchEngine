package searchengine;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a node in our UnicornTrie datastructure.
 * Inspired from <a href="https://community.oracle.com/thread/2070706">here</a>.
 *
 * @author Andrei Cobzaru
 * @author Yonathan Volpin
 */
public class TrieNode {

    /**
     * The parent node.
     */
    private TrieNode parent;

    /**
     * The children nodes (max 36: 'a'-'z', '0'-'9').
     */
    private TrieNode[] children;

    /**
     * Quick way to check if any children exist.
     */
    private boolean isLeaf;

    /**
     * True if this node represent the last character of a word.
     */
    private boolean isWord;

    /**
     * The character represented in this node.
     */
    private char character;

    /**
     * Constructor for top level root node.
     */
    public TrieNode() {
        this.children = new TrieNode[36];
        this.isLeaf = true;
        this.isWord = false;
    }

    /**
     * Constructor for child node.
     */
    public TrieNode(char character) {
        this();
        this.character = character;
    }

    /**
     * Adds a word to this node. This method is called recursively and
     * adds child nodes for each successive letter in the word, therefore
     * recursive calls will be made with partial words.
     *
     * @param word the word to add
     */
    protected void addWord(String word) {
        isLeaf = false;
        int charPosition = word.charAt(0) - 'a';

        if ('0' <= word.charAt(0) && word.charAt(0) <= '9') {
            // It means that the character is a digit.
            charPosition = word.charAt(0) - '0' + 26;
        }

        if (children[charPosition] == null) {
            children[charPosition] = new TrieNode(word.charAt(0));
            children[charPosition].parent = this;
        }

        if (word.length() > 1) {
            children[charPosition].addWord(word.substring(1));
        } else {
            children[charPosition].isWord = true;
        }
    }

    /**
     * Returns the child TrieNode representing the given char,
     * or null if no node exists.
     *
     * @param c the character
     * @return the child node at char c
     */
    protected TrieNode getNode(char c)
    {
        if ('a' <= c && c <= 'z') {
            // If it's a letter.
            return children[c - 'a'];
        }

        // If it's a digit.
        return children[c - '0' + 26];
    }

    /**
     * Gets a list of strings which are lower in the
     * hierarchy than this node.
     *
     * @return the subtrie of this node
     */
    protected List<String> getWords() {
        List<String> list = new ArrayList<>();

        // If this node represents a word, add it.
        if (this.isWord) {
            list.add(toString());
        }

        // If any children.
        if (! this.isLeaf) {
            // Add any words belonging to any children.
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    list.addAll(children[i].getWords());
                }
            }
        }

        return list;
    }

    /**
     * Gets the sequence that this node represents.
     * For example, if this node represents the character t, whose parent
     * represents the charater a, whose parent represents the character
     * c, then the String would be "cat".
     *
     * @return the sequence represented by this node
     */
    public String toString() {
        if (parent == null) {
            return "";
        }

        return parent.toString() + new String(new char[] {character});
    }
}