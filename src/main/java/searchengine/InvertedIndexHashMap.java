package searchengine;

import java.util.HashMap;

/**
 * This is the HashMap implementation of the inverted index class.
 *
 * @author intial repository
 */
public class InvertedIndexHashMap extends InvertedIndex {

    /**
     * The constructor. Initializes the map as a new HashMap.
     */
    public InvertedIndexHashMap() {
        this.map = new HashMap<>();
    }
}
