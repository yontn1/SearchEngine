package searchengine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit tests for the inverted index with a hash map.
 * It should test both build and lookup methods.
 *
 * {@link InvertedIndexHashMap}
 * @author CodeUnicorns
 */
class InvertedIndexHashMapTest {

    private InvertedIndexHashMap invertedIndexHashMap;
    private Website site1;
    private Website site2;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<Website>();
        site1 = new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1"));
        site2 = new Website("example2.com", "example2", Arrays.asList("word2", "word3"));
        sites.add(site1);
        sites.add(site2);

        invertedIndexHashMap = new InvertedIndexHashMap();
        invertedIndexHashMap.build(sites);
    }

    @AfterEach
    void tearDown() {
        invertedIndexHashMap = null;
        site1 = null;
        site2 = null;
    }

    /**
     * There is no way to check the order of the keys in a HashMap,
     * so we will just check if each word has the correct list assigned.
     */
    @Test
    void buildInvertedIndexHashMap() {
        assertEquals(Arrays.asList(site1), invertedIndexHashMap.lookup("word1"));
        assertEquals(Arrays.asList(site1, site2), invertedIndexHashMap.lookup("word2"));
        assertEquals(Arrays.asList(site2) , invertedIndexHashMap.lookup("word3"));
        assertEquals(new ArrayList<>(), invertedIndexHashMap.lookup("word4"));
    }

    @Test
    void lookupInvertedIndexHashMap() {
        assertEquals(1, invertedIndexHashMap.lookup("word1").size());
        assertEquals("example1", invertedIndexHashMap.lookup("word1").get(0).getTitle());
        assertEquals(2, invertedIndexHashMap.lookup("word2").size());
        assertEquals("example1", invertedIndexHashMap.lookup("word2").get(0).getTitle());
        assertEquals("example2", invertedIndexHashMap.lookup("word2").get(1).getTitle());
        assertEquals(0, invertedIndexHashMap.lookup("word4").size());
    }

    @Test
    void testCornerCases() {
        assertEquals(new ArrayList<>(), invertedIndexHashMap.lookup(null));
        assertEquals(new ArrayList<>(), invertedIndexHashMap.lookup(""));
        assertEquals(new ArrayList<>(), invertedIndexHashMap.lookup("           "));
    }

    @Test
    void testPrefixSearch() {
        assertEquals(2, invertedIndexHashMap.lookup("wo*").size());
        assertEquals(2, invertedIndexHashMap.lookup("wor*").size());
        assertEquals(2, invertedIndexHashMap.lookup("word*").size());
        assertEquals(1, invertedIndexHashMap.lookup("word1*").size());
        assertEquals(0, invertedIndexHashMap.lookup("a*").size());
        // Corner case.
        assertEquals(0, invertedIndexHashMap.lookup("$#$!@#!*").size());
    }
}
