package searchengine;

import java.util.*;

/**
 * The inverted index provides better performance of the lookup method.
 *
 * @author CodeUnicorns
 */
abstract public class InvertedIndex implements Index {

    /**
     * The map used for solving the queries.
     */
    protected Map<String, List<Website>> map;

    /**
     * The number of the websites provided in the build method.
     */
    private int databaseSize;

    /**
     * The trie structure for the fast prefix search.
     */
    private UnicornTrie trie;

    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    @Override
    public void build(List<Website> sites) {
        // Set the database size.
        this.databaseSize = sites.size();
        // Instantiate the trie.
        this.trie = new UnicornTrie();

        // Construct the map.
        for (Website website: sites) {
            for (String word: website.getWords()) {
                // Get the existing websites stored in the map.
                List<Website> existingWebsites = this.map.get(word);

                // Build the trie too.
                this.trie.addWord(word);

                // If the list is null (the word is not yet a key in the map), initialize it.
                if (existingWebsites == null) {
                    existingWebsites = new ArrayList<>();
                }

                // Only add the website if it's not there already.
                if (! existingWebsites.contains(website)) {
                    existingWebsites.add(website);

                    // Put the list back.
                    this.map.put(word, existingWebsites);
                }
            }
        }
    }

    /**
     * Given a query word, returns a list of all websites that contain the word.
     *
     * @param query The query word
     * @return the list of websites that contains the query word.
     */
    @Override
    public List<Website> lookup(String query) {
        // treeMap.get(null) throws NullPointerException.
        if (query == null) {
            return new ArrayList<>();
        }

        // We have to check if the query word uses prefix search.
        if (query.endsWith("*")) {
            return prefixSearch(query.replace("*", ""));
        }

        List<Website> result = this.map.get(query);

        // Make sure we don't return null when we don't find any result.
        if (result == null) {
            result = new ArrayList<>();
        }

        return result;
    }

    /**
     * Solves the prefix search by finding all words (using the trie),
     * and then calls the lookup method to add up all the websites.
     *
     * @param prefix the word prefix
     * @return the list of all websites
     */
    private List<Website> prefixSearch(String prefix) {
        // Invalid search.
        if (! prefix.matches("[a-zA-Z0-9]+")) {
            return new ArrayList<>();
        }

        // Use a set to avoid duplicates.
        Set<Website> result = new HashSet<>();
        for (String word: this.trie.getWords(prefix)) {
            result.addAll(lookup(word));
        }

        // Convert from set to list.
        return new ArrayList<>(Arrays.asList(result.toArray(new Website[0])));
    }

    /**
     * Gets the database size.
     *
     * @return the number of websites contained by the index
     */
    @Override
    public int getDatabaseSize() {
        return this.databaseSize;
    }
}
