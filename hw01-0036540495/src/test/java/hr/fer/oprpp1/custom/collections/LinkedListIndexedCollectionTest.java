package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListIndexedCollectionTest {

    @Test
    void testLinkedListIndexedCollectionDefault() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        assertEquals(0, collection.size());
    }

    @Test
    void testLinkedListIndexedCollectionOfOtherCollection() {
        LinkedListIndexedCollection otherCollection = new LinkedListIndexedCollection();
        otherCollection.add(1);
        otherCollection.add("dva");
        otherCollection.add(4.7);

        LinkedListIndexedCollection collection = new LinkedListIndexedCollection(otherCollection);
        assertEquals(3, collection.size());
        assertEquals(1, collection.get(0));
        assertEquals("dva", collection.get(1));
        assertEquals(4.7, collection.get(2));
    }

    @Test
    void testLinkedListIndexedCollectionOfOtherCollectionThrows() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
    }

    // The following tests are the same as in the ArrayIndexedCollectionTest class.
    // I don't know how to solve the redundancy without using interfaces.

    @Test
    void testIsEmpty() {
        LinkedListIndexedCollection emptyCollection = new LinkedListIndexedCollection();
        LinkedListIndexedCollection nonEmptyCollection = new LinkedListIndexedCollection();
        nonEmptyCollection.add(1);

        assertTrue(emptyCollection.isEmpty());
        assertFalse(nonEmptyCollection.isEmpty());
    }

    @Test
    void testSize() {
        LinkedListIndexedCollection emptyCollection = new LinkedListIndexedCollection();
        LinkedListIndexedCollection nonEmptyCollection = new LinkedListIndexedCollection();
        nonEmptyCollection.add(1);

        assertEquals(0, emptyCollection.size());
        assertEquals(1, nonEmptyCollection.size());

        nonEmptyCollection.add(2);
        assertEquals(2, nonEmptyCollection.size());
    }

    @Test
    void testAddAndGet() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();

        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        assertEquals(1, collection.get(0));
        assertEquals("dva", collection.get(1));
        assertEquals(4.7, collection.get(2));
    }

    @Test
    void testAddNull() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection().add(null));
    }

    @Test
    void testGetIndexOutOfBounds() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(0);
        collection.add("null");
        collection.add(4.7);

        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(-47));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(-3));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(47));
    }

    @Test
    void testContains() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        assertTrue(collection.contains(1));
        assertTrue(collection.contains("dva"));
        assertTrue(collection.contains(4.7));
        assertFalse(collection.contains(4));
        assertFalse(collection.contains(null));
    }

    @Test
    void testBooleanRemove() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        assertTrue(collection.remove((Object) 1));
        assertFalse(collection.remove((Object) 4));
        assertTrue(collection.remove(4.7));
    }

    @Test
    void testToArray() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        Object[] array = collection.toArray();
        assertEquals(1, array[0]);
        assertEquals("dva", array[1]);
        assertEquals(4.7, array[2]);
        assertEquals(3, array.length);
    }

    @Test
    void testForEach() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add(2);
        collection.add(3);

        class LocalProcessor extends Processor {
            int sum = 0;

            @Override
            public void process(Object value) {
                sum += (int) value;
            }
        }

        LocalProcessor processor = new LocalProcessor();
        collection.forEach(processor);
        assertEquals(6, processor.sum);
    }

    @Test
    void testForEachNull() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection().forEach(null));
    }

    @Test
    void testAddAll() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(3.4);

        LinkedListIndexedCollection otherCollection = new LinkedListIndexedCollection();
        otherCollection.add("cetiri");
        otherCollection.add(5.0);
        otherCollection.add(6);

        collection.addAll(otherCollection);
        assertEquals(6, collection.size());
        assertEquals(1, collection.get(0));
        assertEquals("dva", collection.get(1));
        assertEquals(3.4, collection.get(2));
        assertEquals("cetiri", collection.get(3));
        assertEquals(5.0, collection.get(4));
        assertEquals(6, collection.get(5));

        assertEquals(3, otherCollection.size());
        assertEquals("cetiri", otherCollection.get(0));
        assertEquals(5.0, otherCollection.get(1));
        assertEquals(6, otherCollection.get(2));
    }

    @Test
    void testAddAllNull() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection().addAll(null));
    }

    @Test
    void testClear() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        collection.clear();
        assertEquals(0, collection.size());
        assertEquals(0, collection.toArray().length);
    }

    @Test
    void testInsert() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        collection.insert(0, 0);
        assertEquals(4, collection.size());
        assertEquals(0, collection.get(0));
        assertEquals(1, collection.get(1));
        assertEquals("dva", collection.get(2));
        assertEquals(4.7, collection.get(3));

        collection.insert(1.5, 2);
        assertEquals(5, collection.size());
        assertEquals(0, collection.get(0));
        assertEquals(1, collection.get(1));
        assertEquals(1.5, collection.get(2));
        assertEquals("dva", collection.get(3));
        assertEquals(4.7, collection.get(4));

        collection.insert("pet", 5);
        assertEquals(6, collection.size());
        assertEquals(0, collection.get(0));
        assertEquals(1, collection.get(1));
        assertEquals(1.5, collection.get(2));
        assertEquals("dva", collection.get(3));
        assertEquals(4.7, collection.get(4));
        assertEquals("pet", collection.get(5));
    }

    @Test
    void testInsertNull() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection().insert(null, 0));
    }

    @Test
    void testInsertOutOfBounds() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(0);
        collection.add("null");
        collection.add(4.7);

        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert(1, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert(1, -47));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert(1, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert(1, 47));
    }

    @Test
    void testIndexOf() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        assertEquals(0, collection.indexOf(1));
        assertEquals(1, collection.indexOf("dva"));
        assertEquals(2, collection.indexOf(4.7));
        assertEquals(-1, collection.indexOf(4));
    }

    @Test
    void testIndexOfNull() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        assertEquals(-1, collection.indexOf(null));
        assertEquals(-1, new LinkedListIndexedCollection().indexOf(null));
    }

    @Test
    void testVoidRemove() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        collection.remove(1);
        assertEquals(2, collection.size());
        assertEquals(1, collection.get(0));
        assertEquals(4.7, collection.get(1));
    }

    @Test
    void testVoidRemoveOutOfBounds() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(0);
        collection.add("null");
        collection.add(4.7);

        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(-47));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(3));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(47));
    }
}
