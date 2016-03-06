package sp;

import org.junit.Test;
import sp.StreamSerializable;
import sp.StringSet;
import sp.Trie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * Created by Alexey on 21.02.2016.
 */
public class StringSetTest {

    public static Trie instance() {
        try {
            return (Trie) Class.forName("sp.StringSet").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @org.junit.Test
    public void testAdd() throws Exception {
        Trie trie = new StringSet();
        assertTrue(trie.add("Ha"));
        assertTrue(trie.add("Haw"));
        assertTrue(trie.add("Hawr"));
        assertTrue(trie.add("HawrQ"));
        assertTrue(trie.add("HawrQZ"));
        assertFalse(trie.add("Ha"));
        assertFalse(trie.add("Haw"));
        assertFalse(trie.add("Hawr"));
        assertFalse(trie.add("HawrQ"));
        assertFalse(trie.add("HawrQZ"));
        assertTrue(trie.remove("Hawr"));
        assertTrue(trie.add("Hawr"));
        assertFalse(trie.add("Hawr"));
    }

    @org.junit.Test
    public void testSize() throws Exception {
        Trie trie = new StringSet();
        assertFalse(trie.contains(""));
        trie.add("Ha");
        assertEquals(1, trie.size());
        trie.add("Hb");
        assertEquals(2, trie.size());
        trie.add("Hc");
        assertEquals(3, trie.size());
        assertTrue(trie.remove("Ha"));
        assertTrue(trie.add("H"));
        assertTrue(trie.remove("H"));
        assertEquals(2, trie.size());
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
        for (int i = 0; i < 123; ++i) {
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

    @Test
    public void testContains() throws Exception {
        Trie trie = new StringSet();
        assertFalse(trie.contains(""));
        assertTrue(trie.add(""));
        assertTrue(trie.contains(""));
        assertTrue(trie.remove(""));
        assertFalse(trie.contains(""));
        assertFalse(trie.contains("Z"));
        assertTrue(trie.add("Z"));
        assertTrue(trie.contains("Z"));
        assertTrue(trie.add("Z2"));
        assertTrue(trie.contains("Z"));
        assertTrue(trie.add("ZZZ"));
        assertTrue(trie.contains("Z2"));
        assertTrue(trie.contains("ZZZ"));
        assertFalse(trie.remove("ZZ"));
        assertTrue(trie.remove("Z2"));
        assertFalse(trie.contains("Z2"));
        assertFalse(trie.remove("Z2"));
        assertFalse(trie.remove("Z2"));
        assertFalse(trie.add("ZZZ"));
    }

    @Test
    public void testRemove() throws Exception {
        Trie trie = new StringSet();
        assertTrue(trie.add("H"));
        assertTrue(trie.add("Hw"));
        assertTrue(trie.add("Hww"));
        assertTrue(trie.remove("Hw"));
        assertFalse(trie.remove("Hw"));
        assertFalse(trie.remove("Hw"));
        assertFalse(trie.add("Hww"));
    }

    @Test
    public void testSimpleSerialization() throws IOException {
        Trie trie = instance();

        assertTrue(trie.add("abc"));
        assertTrue(trie.add("cde"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) trie).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Trie newTrie = instance();
        ((StreamSerializable) newTrie).deserialize(inputStream);

        assertTrue(newTrie.contains("abc"));
        assertTrue(newTrie.contains("cde"));
    }

    @Test(expected = IOException.class)
    public void testSimpleSerializationFails() throws IOException {
        Trie trie = instance();

        assertTrue(trie.add("abc"));
        assertTrue(trie.add("cde"));

        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Fail");
            }
        };

        ((StreamSerializable) trie).serialize(outputStream);
    }

    @Test
    public void testMySerial() throws IOException {
        Trie trie = instance();

        assertTrue(trie.add("abnmerfcre"));
        assertTrue(trie.add("anbjs"));
        assertTrue(trie.add("anbzs"));
        assertTrue(trie.add("saal"));
        assertTrue(trie.add("aaalkw"));
        assertEquals(5, trie.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) trie).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Trie newTrie = instance();
        ((StreamSerializable) newTrie).deserialize(inputStream);

        assertTrue(newTrie.contains("abnmerfcre"));
        assertTrue(newTrie.contains("anbjs"));
        assertTrue(newTrie.contains("saal"));
        assertTrue(newTrie.contains("aaalkw"));
        assertTrue(newTrie.contains("anbzs"));
        assertEquals(5, newTrie.size());
        assertEquals(4, newTrie.howManyStartsWithPrefix("a"));
        assertEquals(2, newTrie.howManyStartsWithPrefix("an"));
        assertFalse(newTrie.contains(""));
        assertFalse(newTrie.contains("a"));
        assertTrue(newTrie.remove("saal"));
        assertEquals(4, newTrie.size());
    }

}