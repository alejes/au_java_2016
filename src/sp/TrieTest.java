package sp;

import static org.junit.Assert.*;

/**
 * Created by Alexey on 21.02.2016.
 */
public class TrieTest {

    @org.junit.Test
    public void testSize() throws Exception {
        Trie trie = new StringSet();
        assertFalse(trie.contains(""));
        assertEquals(0, trie.howManyStartsWithPrefix(""));
        trie.add("Ha");
        assertEquals(trie.size(), 1);
        trie.add("Hb");
        assertEquals(trie.size(), 2);
        trie.add("Hc");
        assertEquals(trie.size(), 3);
        assertEquals(trie.howManyStartsWithPrefix("H"), 3);
        assertFalse(trie.contains(""));
        assertFalse(trie.remove(""));
        assertFalse(trie.remove("H"));
        assertTrue(trie.remove("Ha"));
        assertFalse(trie.remove("Ha"));
        assertTrue(trie.add("H"));
        assertTrue(trie.remove("H"));
        assertFalse(trie.remove("H"));

    }

    @org.junit.Test
    public void testHowManyStartsWithPrefix() throws Exception {

    }
}