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
        Trie trie = new StringSet();
        assertTrue(trie.add(""));
        assertTrue(trie.contains(""));
        assertTrue(trie.remove(""));
        assertFalse(trie.contains(""));
        assertEquals(0, trie.size());
        assertEquals(0, trie.howManyStartsWithPrefix(""));
        for(int i =0; i <123; ++i) {
            trie.add("");
            assertTrue(trie.contains(""));
            assertEquals(1, trie.size());
            assertEquals(1, trie.howManyStartsWithPrefix(""));
            assertTrue(trie.remove(""));
            assertFalse(trie.contains(""));
            assertEquals(0, trie.howManyStartsWithPrefix(""));
        }
        assertTrue(trie.add("H"));
        assertFalse(trie.add("H"));
        assertFalse(trie.add("H"));
        assertFalse(trie.add("H"));
        assertTrue(trie.add("Ha"));
        assertFalse(trie.add("Ha"));
        assertTrue(trie.add("Haa"));
        assertFalse(trie.add("Haa"));
        assertTrue(trie.add("Haaaaa"));
        assertFalse(trie.add("Haaaaa"));
        assertEquals(4, trie.size());
        assertEquals(4, trie.howManyStartsWithPrefix(""));
        assertEquals(4, trie.howManyStartsWithPrefix("H"));
        assertTrue(trie.add("Haaaa"));
        assertFalse(trie.add("Haaaa"));
        assertTrue(trie.add("Haaa"));
        assertFalse(trie.add("Haaa"));
        assertEquals(6, trie.size());
        assertEquals(6, trie.howManyStartsWithPrefix(""));
        assertEquals(6, trie.howManyStartsWithPrefix("H"));
        assertEquals(5, trie.howManyStartsWithPrefix("Ha"));
        assertEquals(4, trie.howManyStartsWithPrefix("Haa"));
        assertEquals(3, trie.howManyStartsWithPrefix("Haaa"));
        assertEquals(2, trie.howManyStartsWithPrefix("Haaaa"));
        assertEquals(1, trie.howManyStartsWithPrefix("Haaaaa"));
        assertEquals(0, trie.howManyStartsWithPrefix("Haaaaaa"));
        assertEquals(0, trie.howManyStartsWithPrefix("Haaaaaaa"));
        assertTrue(trie.remove("Haaa"));
        assertFalse(trie.remove("Haaa"));
        assertEquals(5, trie.size());
        assertEquals(5, trie.howManyStartsWithPrefix(""));
        assertEquals(5, trie.howManyStartsWithPrefix("H"));
        assertEquals(4, trie.howManyStartsWithPrefix("Ha"));
        assertEquals(3, trie.howManyStartsWithPrefix("Haa"));
        assertEquals(2, trie.howManyStartsWithPrefix("Haaa"));
        assertEquals(2, trie.howManyStartsWithPrefix("Haaaa"));
        assertEquals(1, trie.howManyStartsWithPrefix("Haaaaa"));
        assertTrue(trie.remove("Haa"));
        assertFalse(trie.remove("Haa"));
        assertEquals(4, trie.size());
        assertEquals(4, trie.howManyStartsWithPrefix(""));
        assertEquals(4, trie.howManyStartsWithPrefix("H"));
        assertEquals(3, trie.howManyStartsWithPrefix("Ha"));
        assertEquals(2, trie.howManyStartsWithPrefix("Haa"));
        assertEquals(2, trie.howManyStartsWithPrefix("Haaa"));
        assertEquals(2, trie.howManyStartsWithPrefix("Haaaa"));
        assertEquals(1, trie.howManyStartsWithPrefix("Haaaaa"));
        assertEquals(0, trie.howManyStartsWithPrefix("Haaaaaa"));
        assertEquals(0, trie.howManyStartsWithPrefix("Haaaaaaadxexed"));

    }
}