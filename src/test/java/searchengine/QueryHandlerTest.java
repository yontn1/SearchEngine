package searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit tests for the query handler.
 * It should test single word queries, multiple word queries,
 * OR queries, corner cases and url filtering.
 *
 * {@link QueryHandler}
 * @author CodeUnicorns
 */
class QueryHandlerTest {

    private QueryHandler qh;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<>();
        sites.add(new Website("1.com","example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("2.com","example2", Arrays.asList("word2", "word3")));
        sites.add(new Website("3.com","example3", Arrays.asList("word3", "word4", "word5")));
        Index idx = new InvertedIndexHashMap();
        idx.build(sites);
        qh = new QueryHandler(idx);
    }

    @Test
    void testSingleWord() {
        assertEquals(1, qh.getMatchingWebsites("word1").size());
        assertEquals("example1", qh.getMatchingWebsites("word1").get(0).getTitle());
        assertEquals(2, qh.getMatchingWebsites("word2").size());
        assertEquals("example1", qh.getMatchingWebsites("word2").get(0).getTitle());
        assertEquals("example2", qh.getMatchingWebsites("word2").get(1).getTitle());
    }

    @Test
    void testMultipleWords() {
        assertEquals(1, qh.getMatchingWebsites("word1 word2").size());
        assertEquals("example1", qh.getMatchingWebsites("word1 word2").get(0).getTitle());
        assertEquals(1, qh.getMatchingWebsites("word3 word4").size());
        assertEquals("example3", qh.getMatchingWebsites("word3 word4").get(0).getTitle());
        assertEquals(1, qh.getMatchingWebsites("word4 word3 word5").size());
        assertEquals("example3", qh.getMatchingWebsites("word4 word3 word5").get(0).getTitle());
    }

    @Test
    void testORQueries() {
        assertEquals(3, qh.getMatchingWebsites("word2 OR word3").size());
        assertEquals("example1", qh.getMatchingWebsites("word2 OR word3").get(0).getTitle());
        assertEquals("example2", qh.getMatchingWebsites("word2 OR word3").get(1).getTitle());
        assertEquals("example3", qh.getMatchingWebsites("word2 OR word3").get(2).getTitle());
        assertEquals(2, qh.getMatchingWebsites("word1 OR word4").size());
        assertEquals("example1", qh.getMatchingWebsites("word1 OR word4").get(0).getTitle());
        assertEquals("example3", qh.getMatchingWebsites("word1 OR word4").get(1).getTitle());
    }

    // Test for problematic input.
    @Test
    void testCornerCases() {
        assertEquals(new ArrayList<>(), qh.getMatchingWebsites(" OR "));
        assertEquals(new ArrayList<>(), qh.getMatchingWebsites(""));
        assertEquals(new ArrayList<>(), qh.getMatchingWebsites("         "));
        assertEquals(1, qh.getMatchingWebsites("word1 OR    ").size());
        assertEquals("example1", qh.getMatchingWebsites("word1 OR    ").get(0).getTitle());
        assertEquals(1, qh.getMatchingWebsites("word1 OR word1").size());
        assertEquals("example1", qh.getMatchingWebsites("word1 OR word1").get(0).getTitle());
        assertEquals(1, qh.getMatchingWebsites("   word4    word3     word5   ").size());
        assertEquals("example3", qh.getMatchingWebsites("   word4    word3     word5   ").get(0).getTitle());
    }

    // Test url filter.
    @Test
    void testUrlFilter() {
        assertEquals(2, qh.getMatchingWebsites("word2").size());
        assertEquals(1, qh.getMatchingWebsites("site:2.co word2").size());
        assertEquals("example2", qh.getMatchingWebsites("site:2.co word2").get(0).getTitle());
        assertEquals(2, qh.getMatchingWebsites("site:.com word3").size());
        assertEquals("example2", qh.getMatchingWebsites("site:.com word3").get(0).getTitle());
        assertEquals("example3", qh.getMatchingWebsites("site:.com word3").get(1).getTitle());
        // One corner case for the url filter, nothing after ":".
        assertEquals(2, qh.getMatchingWebsites("site: word2").size());
    }
}
