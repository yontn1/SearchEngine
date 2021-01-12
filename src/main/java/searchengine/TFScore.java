package searchengine;

/**
 * This is the implementation of the term frequency(TF) score.
 *
 * @author CodeUnicorns
 */
public class TFScore implements Score {

    /**
     * Calculates the TFScore of a word on a website. This is just the word count.
     *
     * @param word the word we are calculated the score for
     * @param website the website we are calculated the score for
     * @param index the index used for lookups
     * @return the score of the word on the website
     */
    @Override
    public double getScore(String word, Website website, Index index) {
        return tf(word, website);
    }

    /**
     * Calculates tf(w,S), being just the word count.
     *
     * @param w word
     * @param s website
     * @return tf
     */
    protected double tf(String w, Website s) {
        int count = 0;
        for (String word: s.getWords()) {
            if (w.equals(word)) {
                count++;
            }
        }

        return count;
    }
}