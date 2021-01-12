package searchengine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit tests for the simple index.
 * It should test both build and lookup methods.
 *
 * {@link SimpleIndex}
 * @author CodeUnicorns
 */
class SimpleIndexTest {

    private SimpleIndex simpleIndex;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<Website>();
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
        simpleIndex = new SimpleIndex();
        simpleIndex.build(sites);
    }

    @AfterEach
    void tearDown() {
        simpleIndex = null;
    }

    @Test
    void buildSimpleIndex() {
        assertEquals(
                "SimpleIndex{sites=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}, Website{title='example2', url='example2.com', words=[word2, word3]}]}",
                simpleIndex.toString()
        );
    }

    @Test
    void lookupSimpleIndex() {
        assertEquals(1, simpleIndex.lookup("word1").size());
        assertEquals("example1", simpleIndex.lookup("word1").get(0).getTitle());
        assertEquals(2, simpleIndex.lookup("word2").size());
        assertEquals("example1", simpleIndex.lookup("word2").get(0).getTitle());
        assertEquals("example2", simpleIndex.lookup("word2").get(1).getTitle());
        assertEquals(0, simpleIndex.lookup("word4").size());
    }

    @Test
    void testCornerCases() {
        assertEquals(new ArrayList<>(), simpleIndex.lookup(null));
        assertEquals(new ArrayList<>(), simpleIndex.lookup(""));
        assertEquals(new ArrayList<>(), simpleIndex.lookup("           "));
    }

    @Test
    void testPrefixSearch() {
        assertEquals(2, simpleIndex.lookup("wo*").size());
        assertEquals(2, simpleIndex.lookup("wor*").size());
        assertEquals(2, simpleIndex.lookup("word*").size());
        assertEquals(1, simpleIndex.lookup("word1*").size());
        assertEquals(0, simpleIndex.lookup("a*").size());
        // Corner case.
        assertEquals(0, simpleIndex.lookup("$#$!@#!*").size());
    }
}
