package searchengine;

/**
 * The score interface provides a way to calculate a word's score on a website.
 *
 * @author CodeUnicorn
 */
public interface Score {

    /**
     * Calculates the score of a word on a website.
     *
     * @param word the word we are calculated the score for
     * @param website the website we are calculated the score for
     * @param index the index used for lookups
     * @return the score of the word on the website
     */
    double getScore(String word, Website website, Index index);
}
