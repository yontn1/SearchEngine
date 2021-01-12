package searchengine;

import java.util.List;
import java.util.ArrayList;

/**
 * The search engine. Upon receiving a list of websites, it performs
 * the necessary configuration (i.e. building an index and a query
 * handler) to then be ready to receive search queries.
 *
 * @author Willard Rafnsson
 * @author Martin Aumüller
 * @author Leonid Rusnac
 */
public class SearchEngine {
    private QueryHandler queryHandler;

    /**
     * Creates a {@code SearchEngine} object from a list of websites.
     *
     * @param sites the list of websites
     */
    public SearchEngine(List<Website> sites) {
        // As proven by benchmarking, the fastest index is InvertedIndexHashMap,
        // so we will be using that one from now on.
        Index index = new InvertedIndexHashMap();
        index.build(sites);

        // Specify here the score that will be used to rank the websites,
        // otherwise TFScore will be used by default.
        Score score = new TFIDFScore();
        queryHandler = new QueryHandler(index, score);
    }

    /**
     * Returns the list of websites matching the query.
     *
     * @param query the query
     * @return the list of websites matching the query
     */
    public List<ScoredWebsite> search(String query) {
        if (query == null || query.isEmpty() ) {
            return new ArrayList<ScoredWebsite>();
        }
        List<ScoredWebsite> resultList = queryHandler.getMatchingWebsites(query);
        return resultList;
    }
}
