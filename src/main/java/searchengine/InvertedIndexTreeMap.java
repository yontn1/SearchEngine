package searchengine;

import java.util.TreeMap;

/**
 * This is the HashMap implementation of the inverted index class.
 *
 * @author intial repository
 */
public class InvertedIndexTreeMap extends InvertedIndex {

    /**
     * The constructor. Initializes the map as a new TreeMap.
     */
    public InvertedIndexTreeMap() {
        this.map = new TreeMap<>();
    }

    @Override
    public String toString() {
        return "InvertedIndexTreeMap{" +
                "keys=" + this.map.keySet() +
                ";values=" + this.map.values() +
                '}';
    }
}
