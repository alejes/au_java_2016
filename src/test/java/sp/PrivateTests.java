package sp;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * Created by Alexey on 21.02.2016.
 */
public class PrivateTests {

    public static Trie instance() {
        try {
            return (Trie) Class.forName("sp.StringSet").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testSimple() {
        StringSet stringSet = new StringSet();
        assertEquals(0, stringSet.size());
        assertEquals(0, stringSet.howManyStartsWithPrefix("AAAAAAAAAAA"));

        stringSet.add("");
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix(""));
        assertEquals(0, stringSet.howManyStartsWithPrefix("a"));
        assertEquals(true, stringSet.add("AAA"));
        assertEquals(true, stringSet.add("A"));
        assertEquals(true, stringSet.add("AAAAA"));
        assertEquals(false, stringSet.add("AAA"));
        stringSet.remove("AAAAA");
        assertEquals(2, stringSet.howManyStartsWithPrefix("A"));
        assertEquals(false, stringSet.contains("AAAAA"));
        stringSet.remove("A");
        stringSet.remove("AAA");
        assertEquals(1, stringSet.size());
        stringSet.remove("");
        assertEquals(0, stringSet.size());
        assertEquals(false, stringSet.contains("A"));
        assertEquals(false, stringSet.contains(""));
    }

    @org.junit.Test
    public void testAdd() throws Exception {
        StringSet trie = new StringSet();

        assertTrue(trie.add("Hello"));
        assertTrue(trie.add("World"));

        assertEquals(2, trie.size());

        assertTrue(trie.contains("Hello"));
        assertTrue(trie.contains("World"));
        assertFalse(trie.contains("H"));
        assertFalse(trie.contains("Wor"));
        assertFalse(trie.contains(""));
        assertFalse(trie.contains("HelloWorld"));

        trie.add("Hi");

        assertEquals(3, trie.size());
        assertEquals(2, trie.howManyStartsWithPrefix("H"));
        assertEquals(1, trie.howManyStartsWithPrefix("W"));

        assertFalse(trie.contains("H"));
        assertFalse(trie.contains("He"));

        trie.add("");

        assertEquals(4, trie.size());
        assertEquals(4, trie.howManyStartsWithPrefix(""));
        assertTrue(trie.contains(""));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        StringSet trie = new StringSet();

        trie.add("Hello");
        trie.add("World");
        trie.add("Hello world!");
        trie.add("Fizz");
        trie.add("Buzz");
        trie.add("FizzBuzz");
        trie.add("C++ > Java");

        assertTrue(trie.contains("Buzz"));
        assertTrue(trie.contains("Fizz"));
        assertTrue(trie.contains("Hello"));
        assertTrue(trie.contains("World"));
        assertTrue(trie.contains("FizzBuzz"));

        trie.remove("Hello world!");
        assertFalse(trie.contains("Hello world!"));
        assertTrue(trie.contains("Hello"));
        trie.remove("Fizz");
        assertTrue(trie.contains("FizzBuzz"));
        assertFalse(trie.contains("Fizz"));
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        StringSet trie = new StringSet();

        trie.add("Hello");
        trie.add("World");

        assertTrue(trie.remove("Hello"));
        assertEquals(1, trie.size());

        trie.add("");
        trie.add("HI");
        trie.add("Hi");

        assertEquals(4, trie.howManyStartsWithPrefix(""));
        assertEquals(4, trie.size());
        assertEquals(2, trie.howManyStartsWithPrefix("H"));

        assertTrue(trie.remove(""));
        assertFalse(trie.remove(""));
        assertFalse(trie.remove("Hello"));
        assertTrue(trie.remove("HI"));

        assertEquals(1, trie.howManyStartsWithPrefix("H"));
    }

    @org.junit.Test
    public void testHowManyStartsWithPrefix2() throws Exception {
        StringSet trie = new StringSet();
        assertTrue(trie.add("AAB"));
        assertFalse(trie.add("AAB"));
        assertFalse(trie.add("AAB"));
        assertEquals(1, trie.size());
        assertTrue(trie.add("AAC"));
        assertEquals(2, trie.size());
        assertTrue(trie.add("AAD"));
        assertEquals(3, trie.size());
        assertTrue(trie.add("AA"));
        assertEquals(4, trie.size());
        assertTrue(trie.add("A"));
        assertEquals(5, trie.size());
        assertTrue(trie.add("BBC"));
        assertEquals(6, trie.size());
        assertTrue(trie.add("BBD"));
        assertEquals(7, trie.size());
        assertTrue(trie.add("BBE"));
        assertEquals(8, trie.size());
        assertTrue(trie.add("B"));
        assertEquals(9, trie.size());
        assertTrue(trie.remove("A"));
        assertEquals(8, trie.size());
        assertTrue(trie.remove("B"));
        assertEquals(7, trie.size());
        assertTrue(trie.contains("AAD"));
        assertTrue(trie.contains("BBC"));
        assertTrue(trie.contains("BBD"));
        assertTrue(trie.contains("BBE"));
        assertEquals(3, trie.howManyStartsWithPrefix("BB"));
    }

    @org.junit.Test
    public void testAdd3() throws Exception {
        Trie trie = new StringSet();

        assertTrue(trie.add("Hello"));
        assertTrue(trie.add("World"));

        assertEquals(2, trie.size());

        assertTrue(trie.contains("Hello"));
        assertTrue(trie.contains("World"));
        assertFalse(trie.contains("H"));
        assertFalse(trie.contains("Wor"));
        assertFalse(trie.contains(""));
        assertFalse(trie.contains("HelloWorld"));

        trie.add("Hi");

        assertEquals(3, trie.size());
        assertEquals(2, trie.howManyStartsWithPrefix("H"));
        assertEquals(1, trie.howManyStartsWithPrefix("W"));

        assertFalse(trie.contains("H"));
        assertFalse(trie.contains("He"));

        trie.add("");

        assertEquals(4, trie.size());
        assertEquals(4, trie.howManyStartsWithPrefix(""));
        assertTrue(trie.contains(""));

        Trie trie2 = new StringSet();
        trie2.add("AAB");
        trie2.add("AAB");
        trie2.add("AAB");
        assertEquals(1, trie2.size());
        trie2.add("AAC");
        assertEquals(2, trie2.size());
        trie2.add("AAD");
        assertEquals(3, trie2.size());
        trie2.add("AA");
        assertEquals(4, trie2.size());
        trie2.add("A");
        assertEquals(5, trie2.size());
        trie2.add("BBC");
        assertEquals(6, trie2.size());
        trie2.add("BBD");
        assertEquals(7, trie2.size());
        trie2.add("BBE");
        assertEquals(8, trie2.size());
        trie2.add("B");
        assertEquals(9, trie2.size());
        assertTrue(trie2.contains("AAD"));
        assertTrue(trie2.contains("BBC"));
        assertTrue(trie2.contains("BBD"));
        assertTrue(trie2.contains("BBE"));
        assertEquals(3, trie2.howManyStartsWithPrefix("BB"));
    }

    @org.junit.Test
    public void testContains5() throws Exception {
        Trie trie = new StringSet();

        assertTrue(trie.add("Hello"));
        assertTrue(trie.add("World"));
        assertTrue(trie.add("Hello world!"));
        assertTrue(trie.add("Fizz"));
        assertTrue(trie.add("Buzz"));
        assertTrue(trie.add("FizzBuzz"));
        assertTrue(trie.add("C++ > Java"));

        assertTrue(trie.contains("Buzz"));
        assertTrue(trie.contains("Fizz"));
        assertTrue(trie.contains("Hello"));
        assertTrue(trie.contains("World"));
        assertTrue(trie.contains("FizzBuzz"));

        assertTrue(trie.remove("Hello world!"));
        assertFalse(trie.contains("Hello world!"));
        assertTrue(trie.contains("Hello"));
        assertTrue(trie.remove("Fizz"));
        assertTrue(trie.contains("FizzBuzz"));
        assertFalse(trie.contains("Fizz"));
    }

    @org.junit.Test
    public void testRemove2() throws Exception {
        Trie trie = new StringSet();

        assertTrue(trie.add("Hello"));
        assertTrue(trie.add("World"));

        assertTrue(trie.remove("Hello"));
        assertEquals(1, trie.size());

        assertTrue(trie.add(""));
        assertTrue(trie.add("HI"));
        assertTrue(trie.add("Hi"));

        assertEquals(4, trie.howManyStartsWithPrefix(""));
        assertEquals(4, trie.size());
        assertEquals(2, trie.howManyStartsWithPrefix("H"));

        assertTrue(trie.remove(""));
        assertFalse(trie.remove(""));
        assertFalse(trie.remove("Hello"));
        assertTrue(trie.remove("HI"));

        assertEquals(1, trie.howManyStartsWithPrefix("H"));
    }

    @Test
    public void testSimple32() {
        Trie trie = instance();

        assertTrue(trie.add("abc"));
        assertTrue(trie.contains("abc"));
        assertEquals(1, trie.size());
        assertEquals(1, trie.howManyStartsWithPrefix("abc"));
    }

    StringSet trie;

    @Before
    public void init() {
        trie = new StringSet();
        trie.add("he");
        trie.add("she");
        trie.add("his");
        trie.add("hers");
    }

    @Test
    public void testAdd2() throws Exception {
        assertTrue("Add he", !trie.add("he"));
        assertTrue("Add empty", trie.add(""));
        assertTrue("Add empty", !trie.add(""));
    }

    @Test
    public void testContainsEmpty() throws Exception {
        assertTrue("Contains empty", !trie.contains(""));
        assertTrue("Add empty", trie.add(""));
        assertTrue("Contains empty", trie.contains(""));
        assertTrue("Add empty", !trie.add(""));
        assertTrue("Contains empty", trie.contains(""));
        assertTrue("Remove empty", trie.remove(""));
        assertTrue("Contains empty", !trie.contains(""));
    }

    @Test
    public void testContains32() throws Exception {
        assertTrue("Contains he", trie.contains("he"));
        assertTrue("Contains she", trie.contains("she"));
        assertTrue("Contains his", trie.contains("his"));
        assertTrue("Contains her", !trie.contains("her"));
        assertTrue("Add her", trie.add("her"));
        assertTrue("Contains her", trie.contains("her"));
        assertTrue("Contains he", trie.contains("he"));
    }

    @Test
    public void testRemove23() throws Exception {
        assertTrue("Remove he", trie.remove("he"));
        assertTrue("Remove h", !trie.remove("h"));
        assertTrue("Remove she", trie.remove("she"));
        assertTrue("Remove he", trie.remove("hers"));
        assertTrue("Remove she", !trie.remove("she"));
    }

    @Test
    public void testSize() throws Exception {
        assertEquals("Size equals 4", 4, trie.size());
        assertTrue("Remove he", trie.remove("he"));
        assertEquals("Size equals 3", 3, trie.size());
        assertTrue("Add empty", trie.add(""));
        assertEquals("Size equals 4", 4, trie.size());
        assertTrue("Remove empty", trie.remove(""));
        assertEquals("Size equals 3", 3, trie.size());
    }

    @Test
    public void testHowManyStartsWithPrefix() throws Exception {
        assertEquals("How many starts with prefix \"h\"", trie.howManyStartsWithPrefix("h"), 3);
        assertEquals("How many starts with prefix \"\"", trie.howManyStartsWithPrefix(""), 4);
        assertTrue("Add her", trie.add("her"));
        assertTrue("Add her", !trie.add("her"));
        assertEquals("How many starts with prefix \"h\"", trie.howManyStartsWithPrefix("h"), 4);
        assertEquals("How many starts with prefix \"sh\"", trie.howManyStartsWithPrefix("sh"), 1);
        assertEquals("How many starts with prefix \"she\"", trie.howManyStartsWithPrefix("she"), 1);
        assertEquals("How many starts with prefix \"she1\"", trie.howManyStartsWithPrefix("she1"), 0);
    }

    @Test
    public void testHowManyStartsWithPrefixSer() throws Exception {
        assertEquals("How many starts with prefix \"h\"", trie.howManyStartsWithPrefix("h"), 3);
        assertEquals("How many starts with prefix \"\"", trie.howManyStartsWithPrefix(""), 4);
        assertTrue("Add her", trie.add("her"));
        assertTrue("Add her", !trie.add("her"));
        assertEquals("How many starts with prefix \"h\"", trie.howManyStartsWithPrefix("h"), 4);
        assertEquals("How many starts with prefix \"sh\"", trie.howManyStartsWithPrefix("sh"), 1);
        assertEquals("How many starts with prefix \"she\"", trie.howManyStartsWithPrefix("she"), 1);
        assertEquals("How many starts with prefix \"she1\"", trie.howManyStartsWithPrefix("she1"), 0);

        StringSet newStringSet = serializeDeserialize(trie);

        assertTrue("Add her", !newStringSet.add("her"));
        assertEquals("How many starts with prefix \"h\"", newStringSet.howManyStartsWithPrefix("h"), 4);
        assertEquals("How many starts with prefix \"sh\"", newStringSet.howManyStartsWithPrefix("sh"), 1);
        assertEquals("How many starts with prefix \"she\"", newStringSet.howManyStartsWithPrefix("she"), 1);
        assertEquals("How many starts with prefix \"she1\"", newStringSet.howManyStartsWithPrefix("she1"), 0);
    }

    @Test
    public void testMultySerialize() throws IOException {
        StringSet newStringSet = serializeDeserialize(trie);
        newStringSet = serializeDeserialize(newStringSet);
        newStringSet = serializeDeserialize(newStringSet);
        newStringSet = serializeDeserialize(newStringSet);
        newStringSet = serializeDeserialize(newStringSet);
        newStringSet = serializeDeserialize(newStringSet);

        assertEquals("How many starts with prefix \"sh\"", newStringSet.howManyStartsWithPrefix("sh"), 1);
    }

    @Test
    public void testSimple2() {
        StringSet stringSet = (StringSet) instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
    }

    @Test
    public void testSimpleSer() throws IOException {
        StringSet stringSet = (StringSet) instance();
        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));

        StringSet newStringSet = serializeDeserialize(stringSet);

        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));

        assertTrue(newStringSet.contains("abc"));
        assertEquals(1, newStringSet.size());
        assertEquals(1, newStringSet.howManyStartsWithPrefix("abc"));
        assertTrue(stringSet.add("bcd"));
    }

    private StringSet serializeDeserialize(StringSet set) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) set).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        StringSet newStringSet = (StringSet) instance();
        ((StreamSerializable) newStringSet).deserialize(inputStream);
        return newStringSet;
    }

    @Test
    public void testSimpleSerialization() throws IOException {
        StringSet stringSet = (StringSet) instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("cde"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) stringSet).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        StringSet newStringSet = (StringSet) instance();
        ((StreamSerializable) newStringSet).deserialize(inputStream);

        assertTrue(newStringSet.contains("abc"));
        assertTrue(newStringSet.contains("cde"));
    }


    @Test(expected = IOException.class)
    public void testSimpleSerializationFails() throws IOException {
        StringSet stringSet = (StringSet) instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("cde"));

        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Fail");
            }
        };

        ((StreamSerializable) stringSet).serialize(outputStream);
    }



}