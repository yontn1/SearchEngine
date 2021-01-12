package searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit tests for the ranking functionality of the query handler.
 * It should test that the query handler correctly sorts the results
 * decreasingly by score and lexicographically by title.
 *
 * {@link QueryHandler}
 * @author CodeUnicorns
 */
class RankedQueryHandlerTest {

    private QueryHandler qh;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<>();
        // The order that the websites are added is 2,1,3,
        // so the query handler has something to sort.
        sites.add(new Website("2.com","example2", Arrays.asList("word2", "word3")));
        sites.add(new Website("1.com","example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("3.com","example3", Arrays.asList("word2", "word3", "word2")));
        // We use a simple index and a TFScore to make the test easier.
        Index idx = new SimpleIndex();
        idx.build(sites);
        qh = new QueryHandler(idx, new TFScore());
    }

    @Test
    void testWebsitesOrder() {
        assertEquals(3, qh.getMatchingWebsites("word2").size());
        // The final order should be 3 (score 2.0), 1 (score 1.0), 2(score 1.0).
        // 1 should be before 2 because "example1" < "example2" lexicographically.
        assertEquals("example3", qh.getMatchingWebsites("word2").get(0).getTitle());
        assertEquals("example1", qh.getMatchingWebsites("word2").get(1).getTitle());
        assertEquals("example2", qh.getMatchingWebsites("word2").get(2).getTitle());
    }
}
