package searchengine;

import java.util.List;

/**
 * A decoration class for Website. Used in Task 3 for handling scores.
 *
 * @author CodeUnicorns
 */
public class ScoredWebsite extends Website {

    /**
     * the score of the website
     */
    private double score;

    /**
     * Creates a {@code ScoredWebsite} object from a url, a title, a list of words
     * that are contained on the website and a calculated score.
     *
     * @param url the website's url
     * @param title the website's title
     * @param words the website's list of words
     * @param score the website's score
     */
    public ScoredWebsite(String url, String title, List<String> words, double score) {
        super(url, title, words);
        this.score = score;
    }

    /**
     * Gets the website's score.
     *
     * @return the website's score
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Sets the website's score
     *
     * @param score the new score of the website
     */
    public void setScore(double score) {
        this.score = score;
    }
}
