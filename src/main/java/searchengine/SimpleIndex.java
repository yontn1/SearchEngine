package searchengine;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the implementation of a simple index, using a list.
 *
 * @author CodeUnicorns
 */
public class SimpleIndex implements Index {

    /**
     * The list of websites stored in the index.
     */
    private List<Website> sites;

    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */
    @Override
    public void build(List<Website> sites) {
        this.sites = sites;
    }

    /**
     * Given a query string, returns a list of all websites that contain the query.
     *
     * @param query The query
     * @return the list of websites that contains the query word.
     */
    @Override
    public List<Website> lookup(String query) {
        // We have to check if the query word uses prefix search.
        if (query != null) {
            if (query.endsWith("*")) {
                return prefixSearch(query.replace("*", ""));
            }
        }

        List<Website> result = new ArrayList<>();
        for (Website site: sites) {
            if (site.containsWord(query)) {
                result.add(site);
            }
        }

        return result;
    }

    /**
     * Solves the prefix search by simply finding all words
     * that start with the specified prefix.
     *
     * @param prefix the word prefix
     * @return the list of all websites
     */
    private List<Website> prefixSearch(String prefix) {
        // Invalid search.
        if (! prefix.matches("[a-zA-Z0-9]+")) {
            return new ArrayList<>();
        }

        List<Website> result = new ArrayList<>();
        for (Website site: sites) {
            for (String word: site.getWords()) {
                if (word.startsWith(prefix)) {
                    result.add(site);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Gets the database size.
     *
     * @return the number of websites contained by the index
     */
    @Override
    public int getDatabaseSize() {
        return this.sites.size();
    }

    @Override
    public String toString() {
        return "SimpleIndex{" +
                "sites=" + sites +
                '}';
    }
}