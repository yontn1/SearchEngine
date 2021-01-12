package searchengine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class is responsible for answering queries to our search engine.
 *
 * @author CodeUnicorns
 */
public class QueryHandler {

    /**
     * The index the QueryHandler uses for answering queries.
     */
    private Index index;

    /**
     * The score used to calculate scores of the words.
     */
    private Score score;

    /**
     * The simple constructor of QueryHandler. Uses a TFScore as a default.
     *
     * @param index The index used by the QueryHandler.
     */
    public QueryHandler(Index index) {
        this(index, new TFScore());
    }

    /**
     * The constructor of QueryHandler. Sets the index and the score.
     *
     * @param index the index used
     * @param score the score used
     */
    public QueryHandler(Index index, Score score) {
        this.index = index;
        this.score = score;
    }

    /**
     * getMatchingWebsites answers queries of the type
     * "subquery1 OR subquery2 OR subquery3 ...". A "subquery"
     * has the form "word1 word2 word3 ...". A website matches
     * a subquery if all the words occur on the website. A website
     * matches the whole query, if it matches at least one subquery.
     *
     * @param line the query string
     * @return the list of websites that matches the query
     */
    public List<ScoredWebsite> getMatchingWebsites(String line) {
        List<ScoredWebsite> finalResult = new ArrayList<>();

        String urlFilter = null;
        if (line.startsWith("site:")) {
            int indexOfFirstSpace = line.indexOf(" ");
            urlFilter = line.substring(0, indexOfFirstSpace).replace("site:", "");
            // Extract the actual query (remove the filter).
            line = line.substring(indexOfFirstSpace + 1);
        }

        String[] subqueries = line.split(" OR ");

        // We will have to combine each of the individual subqueries.
        for (String subquery: subqueries) {
            String[] words = subquery.split(" ");

            // If there are multiple spaces between words, the "words" array
            // might contain empty strings, so we have to filter it.
            List<String> filteredWords = new ArrayList<>();
            for (String word: words) {
                if (! word.isEmpty()) {
                    filteredWords.add(word);
                }
            }
            // Override the initial list with the filtered list.
            words = filteredWords.toArray(new String[0]);

            // Don't do anything if this split doesn't contain any word.
            if (words.length == 0) {
                continue;
            }

            // Initialize the partial result as the first search.
            List<Website> partialResult = index.lookup(words[0]);
            List<ScoredWebsite> scoredPartialResult = calculateScores(partialResult, words[0]);

            // Intersect all the rest.
            for (int i = 1; i < words.length; i++) {
                List<Website> individualResult = index.lookup(words[i]);
                List<ScoredWebsite> scoredIndividualResult = calculateScores(individualResult, words[i]);
                scoredPartialResult = intersectResults(scoredPartialResult, scoredIndividualResult);
            }

            // Combine the results.
            finalResult = combineResults(scoredPartialResult, finalResult);
        }

        if (urlFilter != null) {
            // We have to filter the urls.
            List<ScoredWebsite> urlFilteredResult = new ArrayList<>();
            for (ScoredWebsite site: finalResult) {
                if (site.getUrl().contains(urlFilter)) {
                    urlFilteredResult.add(site);
                }
            }

            // The final result is the url filtered list.
            finalResult = urlFilteredResult;
        }

        // Sort the final result before returning it.
        finalResult.sort(new Comparator<ScoredWebsite>() {
            @Override
            public int compare(ScoredWebsite o1, ScoredWebsite o2) {
                // If the score is the same, order the sites alphabetically by title.
                if (o1.getScore() == o2.getScore()) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }

                // Decreasing order for scores.
                return - Double.compare(o1.getScore(), o2.getScore());
            }
        });

        return finalResult;
    }

    /**
     * Returns a list with scored websites contained by both lists.
     * Adds up the scores.
     *
     * @param list1 first scored list
     * @param list2 second scored list
     * @return the intersection of the 2 scored lists
     */
    private List<ScoredWebsite> intersectResults(List<ScoredWebsite> list1, List<ScoredWebsite> list2) {
        List<ScoredWebsite> result = new ArrayList<>();

        for (ScoredWebsite site1: list1) {
            for (ScoredWebsite site2: list2) {
                if (site1.equals(site2)) {
                    // Add up the score.
                    double totalScore = site1.getScore() + site2.getScore();
                    site1.setScore(totalScore);
                    result.add(site1);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Returns a list with scored websites contained by at least one of the lists.
     * If the website is contained by both lists, its score will be the maximum one.
     *
     * @param list1 first scored list
     * @param list2 second scored list
     * @return the reunion of the 2 scored lists
     */
    private List<ScoredWebsite> combineResults(List<ScoredWebsite> list1, List<ScoredWebsite> list2) {
        // Initialize the result as a copy of list1.
        List<ScoredWebsite> result = new ArrayList<>(list1);

        // Consider the websites in the second list.
        for (ScoredWebsite site2: list2) {
            int index = -1;

            // Try to find the site2 in list1.
            for (ScoredWebsite site1: list1) {
                if (site2.equals(site1)) {
                    index = list1.indexOf(site1);
                    break;
                }
            }

            if (index == -1) {
                // site2 was not found in list1, so we have to add it to the result.
                result.add(site2);
            } else {
                // site2 was found in list1, so if it has a higher score, we should update the list.
                if (site2.getScore() > list1.get(index).getScore()) {
                    list1.set(index, site2);
                }
            }
        }

        return result;
    }

    /**
     * Transforms a list of websites in a list of scored websites,
     * calculating the word's score on each website.
     *
     * @param sites the sites to calculate the scores for
     * @param word the word
     * @return a list of scored websites with calculated scores
     */
    private List<ScoredWebsite> calculateScores(List<Website> sites, String word) {
        List<ScoredWebsite> scoredWebsites = new ArrayList<>();
        for (Website site: sites) {
            double score = this.score.getScore(word, site, index);
            scoredWebsites.add(new ScoredWebsite(site.getUrl(), site.getTitle(), site.getWords(), score));
        }

        return scoredWebsites;
    }
}
