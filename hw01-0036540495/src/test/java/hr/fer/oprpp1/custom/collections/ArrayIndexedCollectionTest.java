package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayIndexedCollectionTest {

    @Test
    void testArrayIndexedCollectionDefault() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        assertEquals(16, collection.getCapacity());
    }

    @Test
    void testArrayIndexedCollectionWithInitialCapacity() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection(47);
        assertEquals(47, collection.getCapacity());
    }

    @Test
    void testArrayIndexedCollectionWithInitialCapacityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(-1));
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
        assertDoesNotThrow(() -> new ArrayIndexedCollection(1));
    }

    @Test
    void testArrayIndexedCollectionOfOtherCollection() {
        ArrayIndexedCollection otherCollection = new ArrayIndexedCollection();
        otherCollection.add(1);
        otherCollection.add("dva");
        otherCollection.add(4.7);

        ArrayIndexedCollection collection = new ArrayIndexedCollection(otherCollection);
        assertEquals(16, collection.getCapacity());
        assertEquals(3, collection.size());
        assertEquals(1, collection.get(0));
        assertEquals("dva", collection.get(1));
        assertEquals(4.7, collection.get(2));
    }

    @Test
    void testArrayIndexedCollectionOfOtherCollectionThrows() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
    }

    @Test
    void testArrayIndexedCollectionOfOtherCollectionWithInitialCapacity() {
        ArrayIndexedCollection otherCollection = new ArrayIndexedCollection();
        otherCollection.add(1);
        otherCollection.add("dva");
        otherCollection.add(4.7);

        ArrayIndexedCollection collection = new ArrayIndexedCollection(otherCollection, 47);
        assertEquals(47, collection.getCapacity());
        assertEquals(1, collection.get(0));
        assertEquals("dva", collection.get(1));
        assertEquals(4.7, collection.get(2));

        collection = new ArrayIndexedCollection(otherCollection, 1);
        assertEquals(3, collection.getCapacity());
        assertEquals(1, collection.get(0));
        assertEquals("dva", collection.get(1));
        assertEquals(4.7, collection.get(2));
    }

    @Test
    void testArrayIndexedCollectionOfOtherCollectionWithInitialCapacityThrows() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, -1));
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 0));
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 1));
        assertThrows(IllegalArgumentException.class, () ->
                new ArrayIndexedCollection(new ArrayIndexedCollection(), -1));
    }

    // The following tests are the same as in LinkedListIndexedCollectionTest class.
    // I don't know how to solve the redundancy without using interfaces.

    @Test
    void testIsEmpty() {
        ArrayIndexedCollection emptyCollection = new ArrayIndexedCollection();
        ArrayIndexedCollection nonEmptyCollection = new ArrayIndexedCollection();
        nonEmptyCollection.add(1);

        assertTrue(emptyCollection.isEmpty());
        assertFalse(nonEmptyCollection.isEmpty());
    }

    @Test
    void testSize() {
        ArrayIndexedCollection emptyCollection = new ArrayIndexedCollection();
        ArrayIndexedCollection nonEmptyCollection = new ArrayIndexedCollection();
        nonEmptyCollection.add(1);

        assertEquals(0, emptyCollection.size());
        assertEquals(1, nonEmptyCollection.size());

        nonEmptyCollection.add(2);
        assertEquals(2, nonEmptyCollection.size());
    }

    @Test
    void testAddAndGet() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();

        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        assertEquals(1, collection.get(0));
        assertEquals("dva", collection.get(1));
        assertEquals(4.7, collection.get(2));
    }

    @Test
    void testAddNull() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection().add(null));
    }

    @Test
    void testGetIndexOutOfBounds() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        assertTrue(collection.remove((Object) 1));
        assertFalse(collection.remove((Object) 4));
        assertTrue(collection.remove(4.7));
    }

    @Test
    void testToArray() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection().forEach(null));
    }

    @Test
    void testAddAll() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(3.4);

        ArrayIndexedCollection otherCollection = new ArrayIndexedCollection();
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
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection().addAll(null));
    }

    @Test
    void testClear() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        collection.clear();
        assertEquals(0, collection.size());
        assertEquals(0, collection.toArray().length);
    }

    @Test
    void testInsert() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection().insert(null, 0));
    }

    @Test
    void testInsertOutOfBounds() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(1);
        collection.add("dva");
        collection.add(4.7);

        assertEquals(-1, collection.indexOf(null));
        assertEquals(-1, new ArrayIndexedCollection().indexOf(null));
    }

    @Test
    void testVoidRemove() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add(0);
        collection.add("null");
        collection.add(4.7);

        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(-47));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(3));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.remove(47));
    }
}
