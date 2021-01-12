package searchengine;

/**
 * This is the implementation of the term frequency–inverse document frequency(TFIDF) score.
 *
 * @author CodeUnicorns
 */
public class TFIDFScore extends TFScore {

    /**
     * Calculates the score of a word on a website. This is tf (the word count),
     * multiplied with idf (the logarithm of the size of the database divided
     * by the number of website the word occurs on).
     *
     * @param word the word we are calculated the score for
     * @param website the website we are calculated the score for
     * @param index the index used for lookups
     * @return the score of the word on the website
     */
    @Override
    public double getScore(String word, Website website, Index index) {
        double tf = tf(word, website);

        // The size of the database.
        int d = index.getDatabaseSize();

        // The number of website the word appears on.
        int n = index.lookup(word).size();

        // If the word does not appear on any website,
        // we can just return 0, as tf will be 0 too.
        if (n == 0) return 0;

        // Computing idf.
        double idf = log2(1.0 * d / n);

        return tf * idf;
    }

    /**
     * Calculates the binary logarithm.
     *
     * @param arg the argument of the logarithm
     * @return the binary logarithm of arg
     */
    private double log2(double arg) {
        // As Math does not have the log2 function, we have to use
        // a math formula to change the base of the logarithm from e to 2.
        return Math.log(arg) / Math.log(2.0);
    }
}
