package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {
    @Test
    public void testDefaultConstructor() {
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        assertEquals(0, dictionary.size());
    }

    @Test
    public void testIsEmpty() {
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        assertTrue(dictionary.isEmpty());
        dictionary.put("one", 1);
        assertFalse(dictionary.isEmpty());
    }

    @Test
    public void testSize() {
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        assertEquals(0, dictionary.size());
        dictionary.put("one", 1);
        assertEquals(1, dictionary.size());
        dictionary.put("two", 2);
        assertEquals(2, dictionary.size());
        dictionary.put("three", 3);
        assertEquals(3, dictionary.size());
        dictionary.remove("one");
        assertEquals(2, dictionary.size());
        dictionary.remove("two");
        assertEquals(1, dictionary.size());
        dictionary.remove("three");
        assertEquals(0, dictionary.size());
    }

    @Test
    public void testClear() {
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        dictionary.put("one", 1);
        dictionary.put("two", 2);
        dictionary.put("three", 3);
        dictionary.clear();
        assertTrue(dictionary.isEmpty());
    }

    @Test
    public void testPut() {
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        dictionary.put("one", 1);
        assertEquals(1, dictionary.size());
        dictionary.put("one", 2);
        assertEquals(1, dictionary.size());
        dictionary.put("two", 3);
        assertEquals(2, dictionary.size());
        dictionary.put("two", null);
        assertEquals(2, dictionary.size());
    }

    @Test
    public void testGet() {
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        dictionary.put("one", 1);
        dictionary.put("two", 2);
        dictionary.put("three", 3);
        assertEquals(1, dictionary.get("one"));
        assertEquals(2, dictionary.get("two"));
        assertEquals(3, dictionary.get("three"));
        assertNull(dictionary.get("four"));
        assertNull(dictionary.get(null));
        assertNull(dictionary.get(1));
        assertNull(dictionary.get(1.0));
        assertNull(dictionary.get(new Object()));
        assertNull(dictionary.get(new Dictionary<String, Integer>()));
    }

    @Test
    public void testRemove() {
        Dictionary<String, Integer> dictionary = new Dictionary<>();
        dictionary.put("one", 1);
        dictionary.put("two", 2);
        dictionary.put("three", 3);
        assertEquals(1, dictionary.remove("one"));
        assertEquals(2, dictionary.size());
        assertEquals(2, dictionary.remove("two"));
        assertEquals(1, dictionary.size());
        assertEquals(3, dictionary.remove("three"));
        assertEquals(0, dictionary.size());
        assertNull(dictionary.remove("four"));
    }
}
