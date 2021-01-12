package searchengine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit tests for the inverted index with a tree map.
 * It should test both build and lookup methods.
 *
 * {@link InvertedIndexTreeMap}
 * @author CodeUnicorns
 */
class InvertedIndexTreeMapTest {

    private InvertedIndexTreeMap invertedIndexTreeMap;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<Website>();
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
        invertedIndexTreeMap = new InvertedIndexTreeMap();
        invertedIndexTreeMap.build(sites);
    }

    @AfterEach
    void tearDown() {
        invertedIndexTreeMap = null;
    }

    /**
     * There is a way to check the order of the keys in a TreeMap,
     * so we can just use the toString method like for the SimpleIndex.
     */
    @Test
    void buildInvertedIndexTreeMap() {
        String expectedString = "InvertedIndexTreeMap{" +
                "keys=" +
                "[word1, word2, word3]" +
                ";values=" +
                "[[Website{title='example1', url='example1.com', words=[word1, word2, word1]}], " +
                "[Website{title='example1', url='example1.com', words=[word1, word2, word1]}, Website{title='example2', url='example2.com', words=[word2, word3]}], " +
                "[Website{title='example2', url='example2.com', words=[word2, word3]}]]" +
                '}';

        assertEquals(expectedString, invertedIndexTreeMap.toString());
    }

    @Test
    void lookupInvertedIndexTreeMap() {
        assertEquals(1, invertedIndexTreeMap.lookup("word1").size());
        assertEquals("example1", invertedIndexTreeMap.lookup("word1").get(0).getTitle());
        assertEquals(2, invertedIndexTreeMap.lookup("word2").size());
        assertEquals("example1", invertedIndexTreeMap.lookup("word2").get(0).getTitle());
        assertEquals("example2", invertedIndexTreeMap.lookup("word2").get(1).getTitle());
        assertEquals(0, invertedIndexTreeMap.lookup("word4").size());
    }

    @Test
    void testCornerCases() {
        assertEquals(new ArrayList<>(), invertedIndexTreeMap.lookup(null));
        assertEquals(new ArrayList<>(), invertedIndexTreeMap.lookup(""));
        assertEquals(new ArrayList<>(), invertedIndexTreeMap.lookup("           "));
    }

    @Test
    void testPrefixSearch() {
        assertEquals(2, invertedIndexTreeMap.lookup("wo*").size());
        assertEquals(2, invertedIndexTreeMap.lookup("wor*").size());
        assertEquals(2, invertedIndexTreeMap.lookup("word*").size());
        assertEquals(1, invertedIndexTreeMap.lookup("word1*").size());
        assertEquals(0, invertedIndexTreeMap.lookup("a*").size());
        // Corner case.
        assertEquals(0, invertedIndexTreeMap.lookup("$#$!@#!*").size());
    }
}
