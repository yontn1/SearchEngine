package searchengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Our custom Trie data structure. Used for prefix search.
 * Inspired from <a href="https://community.oracle.com/thread/2070706">here</a>.
 *
 * @author Anrei Cobzaru
 * @author Yonathan Volpin
 */
public class UnicornTrie {

    /**
     * The root of the trie.
     */
    private TrieNode root;

    /**
     * Initializes the root as a new node.
     */
    public UnicornTrie() {
        this.root = new TrieNode();
    }

    /**
     * Adds a word to the datastructure.
     *
     * @param word the word to be added
     */
    public void addWord(String word) {
        this.root.addWord(word.toLowerCase());
    }

    /**
     * Get the words in the Trie with the given prefix.
     *
     * @param prefix the prefix
     * @return a list containing all words starting with the given prefix
     */
    public List<String> getWords(String prefix) {
        // Find the node which represents the last letter of the prefix.
        TrieNode lastNode = this.root;
        for (int i = 0; i < prefix.length(); i++) {
            lastNode = lastNode.getNode(prefix.charAt(i));

            // If no node matches, then no words exist.
            if (lastNode == null) {
                return new ArrayList<String>();
            }
        }

        // Return the words which start from the last node.
        return lastNode.getWords();
    }
}
