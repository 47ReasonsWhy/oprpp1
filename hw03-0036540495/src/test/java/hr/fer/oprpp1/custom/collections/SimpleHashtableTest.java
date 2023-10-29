package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleHashtableTest {
    @Test
    void testDefaultConstructor() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        assertEquals(16, table.capacity());
    }

    @Test
    void testConstructorWithCapacity() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>(10);
        assertEquals(16, table.capacity());
        SimpleHashtable<String, Integer> table2 = new SimpleHashtable<>(17);
        assertEquals(32, table2.capacity());
    }

    @Test
    void testPut() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
        table.put("one", 1);
        table.put("two", 22);
        table.put("three", 3);
        assertEquals(3, table.size());
        assertEquals(4, table.capacity());
        table.put("four", 4);
        table.put("two", 2);
        assertEquals(4, table.size());
        assertEquals(8, table.capacity());
    }

    @Test
    void testGet() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        table.put("four", 4);
        assertEquals(1, table.get("one"));
        assertEquals(2, table.get("two"));
        assertEquals(3, table.get("three"));
        assertEquals(4, table.get("four"));
        assertNull(table.get("five"));
        assertNull(table.get(null));
        assertNull(table.get(1));
        assertNull(table.get(1.0));
        assertNull(table.get(new Object()));
        assertNull(table.get(new SimpleHashtable<>()));
    }

    @Test
    void testContainsKey() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        table.put("four", 4);
        assertTrue(table.containsKey("one"));
        assertTrue(table.containsKey("two"));
        assertTrue(table.containsKey("three"));
        assertTrue(table.containsKey("four"));
        assertFalse(table.containsKey("five"));
        assertFalse(table.containsKey(null));
        assertFalse(table.containsKey(1));
        assertFalse(table.containsKey(1.0));
        assertFalse(table.containsKey(new Object()));
        assertFalse(table.containsKey(new SimpleHashtable<>()));
    }

    @Test
    void testContainsValue() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        table.put("four", 4);
        assertTrue(table.containsValue(1));
        assertTrue(table.containsValue(2));
        assertTrue(table.containsValue(3));
        assertTrue(table.containsValue(4));
        assertFalse(table.containsValue(5));
        assertFalse(table.containsValue(null));
        assertFalse(table.containsValue("one"));
        assertFalse(table.containsValue(1.0));
        assertFalse(table.containsValue(new Object()));
        assertFalse(table.containsValue(new SimpleHashtable<>()));
    }

    @Test
    void testRemove() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        table.put("four", 4);
        assertEquals(4, table.remove("four"));
        assertEquals(3, table.size());
        assertEquals(3, table.remove("three"));
        assertEquals(2, table.size());
        assertEquals(2, table.remove("two"));
        assertEquals(1, table.size());
        assertEquals(1, table.remove("one"));
        assertEquals(0, table.size());
        assertNull(table.remove("one"));
        assertNull(table.remove(null));
        assertNull(table.remove(1));
        assertNull(table.remove(1.0));
        assertNull(table.remove(new Object()));
        assertNull(table.remove(new SimpleHashtable<>()));
    }

    @Test
    void testIsEmpty() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        assertTrue(table.isEmpty());
        table.put("one", 1);
        assertFalse(table.isEmpty());
        table.remove("one");
        assertTrue(table.isEmpty());
    }

    @Test
    void testToString() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        table.put("four", 4);
        /*int slot1 = Math.abs("one".hashCode()) % 16;
        int slot2 = Math.abs("two".hashCode()) % 16;
        int slot3 = Math.abs("three".hashCode()) % 16;
        int slot4 = Math.abs("four".hashCode()) % 16;
        System.out.println(slot1 + " " + slot2 + " " + slot3 + " " + slot4);*/
        assertEquals("[one=1, four=4, two=2, three=3]", table.toString());
    }

    @Test
    void testClear() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        table.put("four", 4);
        table.clear();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
    }

    @Test
    void testIterator() {
        SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        table.put("four", 4);


        // TODO
    }
}
