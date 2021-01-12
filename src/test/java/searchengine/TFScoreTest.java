package searchengine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit tests for the term frequency core.
 * It should test that the TF score is calculated correctly.
 *
 * {@link TFScore}
 * @author CodeUnicorns
 */
class TFScoreTest {

    private TFScore tfScore;
    private Index index;
    private Website site1;
    private Website site2;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<Website>();
        site1 = new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1"));
        site2 = new Website("example2.com", "example2", Arrays.asList("word2", "word3"));
        sites.add(site1);
        sites.add(site2);

        tfScore = new TFScore();
        index = new InvertedIndexHashMap();
        index.build(sites);
    }

    @AfterEach
    void tearDown() {
        tfScore = null;
        index = null;
        site1 = null;
        site2 = null;
    }

    @Test
    void checkTFScores() {
        assertEquals(2.0, tfScore.getScore("word1", site1, index));
        assertEquals(1.0, tfScore.getScore("word2", site1, index));
        assertEquals(0.0, tfScore.getScore("word3", site1, index));
        assertEquals(1.0, tfScore.getScore("word2", site2, index));
        assertEquals(1.0, tfScore.getScore("word3", site2, index));
    }
}
