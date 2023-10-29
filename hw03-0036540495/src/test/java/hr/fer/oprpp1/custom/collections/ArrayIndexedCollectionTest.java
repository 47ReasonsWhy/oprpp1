package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayIndexedCollectionTest {

    @Test
    void testDefaultConstructor() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        assertEquals(16, collection.getCapacity());
    }

    @Test
    void testConstructorInitialCapacity() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>(10);
        assertEquals(10, collection.getCapacity());
    }

    @Test
    void testConstructorInitialCapacityIllegal() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(-1));
        assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<>(0));
    }

    @Test
    void testConstructorFromNull() {
        assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null));
    }

    @Test
    void testAdd() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        assertEquals(3, collection.size());
    }

    @Test
    void testAddNull() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        assertThrows(NullPointerException.class, () -> collection.add(null));
    }

    @Test
    void testAddAll() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        ArrayIndexedCollection<String> collection2 = new ArrayIndexedCollection<>();
        collection2.addAll(collection);
        assertEquals(3, collection2.size());
    }

    @Test
    void testAddAllExtend() {
        ArrayIndexedCollection<Number> collection = new ArrayIndexedCollection<>();

        ArrayIndexedCollection<Integer> collection2 = new ArrayIndexedCollection<>();
        collection2.add(1);
        collection2.add(2);
        collection2.add(3);

        ArrayIndexedCollection<Double> collection3 = new ArrayIndexedCollection<>();
        collection3.add(1.0);
        collection3.add(2.0);
        collection3.add(3.0);

        ArrayIndexedCollection<Float> collection4 = new ArrayIndexedCollection<>();
        collection4.add(1.0f);
        collection4.add(2.0f);
        collection4.add(3.0f);

        collection.addAll(collection2);
        collection.addAll(collection3);
        collection.addAll(collection4);

        assertEquals(9, collection.size());
        assertEquals(16, collection.getCapacity());

        assertEquals(1, collection.get(0));
        assertEquals(2, collection.get(1));
        assertEquals(3, collection.get(2));
        assertEquals(1.0, collection.get(3));
        assertEquals(2.0, collection.get(4));
        assertEquals(3.0, collection.get(5));
        assertEquals(1.0f, collection.get(6));
        assertEquals(2.0f, collection.get(7));
        assertEquals(3.0f, collection.get(8));
    }

    @Test
    void testAddAllFromNull() {
        ArrayIndexedCollection<Number> collection = new ArrayIndexedCollection<>();
        assertThrows(NullPointerException.class, () -> collection.addAll(null));
    }

    @Test
    void testAddAllSatisfying() {
        ArrayIndexedCollection<Object> collection = new ArrayIndexedCollection<>();
        ArrayIndexedCollection<Integer> collection2 = new ArrayIndexedCollection<>();
        collection2.add(1);
        collection2.add(2);
        collection2.add(3);
        collection.addAllSatisfying(collection2, n -> n > 1);
        assertEquals(2, collection.size());
        assertEquals(2, collection.get(0));
        assertEquals(3, collection.get(1));
    }

    @Test
    void testConstructorFromAnotherCollection() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        ArrayIndexedCollection<String> collection2 = new ArrayIndexedCollection<>(collection);
        assertEquals(3, collection2.size());
        assertEquals(16, collection2.getCapacity());
    }

    @Test
    void testConstructorFromAnotherCollectionWithInitialCapacity() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>(10);
        collection.add("1");
        collection.add("2");
        collection.add("3");
        ArrayIndexedCollection<String> collection2 = new ArrayIndexedCollection<>(collection, 20);
        assertEquals(3, collection2.size());
        assertEquals(20, collection2.getCapacity());
    }

    @Test
    void testContains() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        assertTrue(collection.contains("1"));
        assertTrue(collection.contains("2"));
        assertTrue(collection.contains("3"));
        assertFalse(collection.contains("4"));
    }

    @Test
    void testContainsNull() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        assertFalse(collection.contains(null));
    }

    @Test
    void testContainsNonCastable() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        assertFalse(collection.contains(true));
        assertFalse(collection.contains(1));
        assertFalse(collection.contains(1.0));
        assertFalse(collection.contains('1'));
        assertFalse(collection.contains(new Object()));
        assertFalse(collection.contains(new ArrayIndexedCollection<>()));
        assertFalse(collection.contains(new LinkedListIndexedCollection<>()));
    }

    @Test
    void testRemove() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        assertTrue(collection.remove("1"));
        assertTrue(collection.remove("2"));
        assertTrue(collection.remove("3"));
        assertEquals(0, collection.size());
    }

    @Test
    void testRemoveNull() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        assertFalse(collection.remove(null));
    }

    @Test
    void testRemoveNonCastable() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        assertFalse(collection.remove(true));
        assertFalse(collection.remove((Object) 1));
        assertFalse(collection.remove(1.0));
        assertFalse(collection.remove((Object) '1'));
        assertFalse(collection.remove(new Object()));
        assertFalse(collection.remove(new ArrayIndexedCollection<>()));
        assertFalse(collection.remove(new LinkedListIndexedCollection<>()));
    }

    @Test
    void testToArray() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        Object[] array = collection.toArray();
        assertEquals(Object.class, array.getClass().getComponentType());
        assertEquals(3, array.length);
        assertEquals("1", array[0]);
        assertEquals("2", array[1]);
        assertEquals("3", array[2]);
    }

    @Test
    void testToTypedArray() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");

        String[] array = collection.toArray(new String[0]);
        assertEquals(String.class, array.getClass().getComponentType());
        assertEquals(3, array.length);
        assertEquals("1", array[0]);
        assertEquals("2", array[1]);
        assertEquals("3", array[2]);

        String[] array2 = collection.toArray(new String[5]);
        assertEquals(String.class, array2.getClass().getComponentType());
        assertEquals(5, array2.length);
        assertEquals("1", array2[0]);
        assertEquals("2", array2[1]);
        assertEquals("3", array2[2]);
        assertNull(array2[3]);
        assertNull(array2[4]);
    }

    @Test
    void testToTypedArrayFromNull() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        assertThrows(NullPointerException.class, () -> collection.toArray(null));
    }

    @Test
    void testGet() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        assertEquals("1", collection.get(0));
        assertEquals("2", collection.get(1));
        assertEquals("3", collection.get(2));
    }

    @Test
    void testGetOutOfBounds() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.get(3));
    }

    @Test
    void testClear() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        collection.clear();
        assertEquals(0, collection.size());
    }

    @Test
    void testInsert() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");

        collection.insert("1.5", 1);
        assertEquals(4, collection.size());
        assertEquals("1", collection.get(0));
        assertEquals("1.5", collection.get(1));
        assertEquals("2", collection.get(2));
        assertEquals("3", collection.get(3));

        collection.insert("0", 0);
        assertEquals(5, collection.size());
        assertEquals("0", collection.get(0));
        assertEquals("1", collection.get(1));
        assertEquals("1.5", collection.get(2));
        assertEquals("2", collection.get(3));
        assertEquals("3", collection.get(4));

        collection.insert("4", 5);
        assertEquals(6, collection.size());
        assertEquals("0", collection.get(0));
        assertEquals("1", collection.get(1));
        assertEquals("1.5", collection.get(2));
        assertEquals("2", collection.get(3));
        assertEquals("3", collection.get(4));
        assertEquals("4", collection.get(5));
    }

    @Test
    void testInsertNull() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        assertThrows(NullPointerException.class, () -> collection.insert(null, 0));
    }

    @Test
    void testInsertOutOfBounds() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert("1", -1));
        assertThrows(IndexOutOfBoundsException.class, () -> collection.insert("1", 1));
    }

    @Test
    void testIndexOf() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");

        assertEquals(0, collection.indexOf("1"));
        assertEquals(1, collection.indexOf("2"));
        assertEquals(2, collection.indexOf("3"));
        assertEquals(-1, collection.indexOf("4"));
    }

    @Test
    void testIndexOfNull() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        assertEquals(-1, collection.indexOf(null));
    }

    @Test
    void testIndexOfNonCastable() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        assertEquals(-1, collection.indexOf(true));
        assertEquals(-1, collection.indexOf(1));
        assertEquals(-1, collection.indexOf(1.0));
        assertEquals(-1, collection.indexOf('1'));
        assertEquals(-1, collection.indexOf(new Object()));
        assertEquals(-1, collection.indexOf(new ArrayIndexedCollection<>()));
        assertEquals(-1, collection.indexOf(new LinkedListIndexedCollection<>()));
    }

    @Test
    void testElementsGetter() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");

        ElementsGetter<String> getter = collection.createElementsGetter();
        assertTrue(getter.hasNextElement());
        assertEquals("1", getter.getNextElement());
        assertTrue(getter.hasNextElement());
        assertEquals("2", getter.getNextElement());
        assertTrue(getter.hasNextElement());
        assertEquals("3", getter.getNextElement());
        assertFalse(getter.hasNextElement());
        assertThrows(NoSuchElementException.class, getter::getNextElement);
    }

    @Test
    void testElementsGetterWithConcurrentAdd() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");

        ElementsGetter<String> getter = collection.createElementsGetter();
        assertTrue(getter.hasNextElement());
        assertEquals("1", getter.getNextElement());;

        collection.add("4");
        assertThrows(ConcurrentModificationException.class, getter::getNextElement);
    }

    @Test
    void testElementsGetterWithConcurrentRemove() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");

        ElementsGetter<String> getter = collection.createElementsGetter();
        assertTrue(getter.hasNextElement());
        assertEquals("1", getter.getNextElement());;

        collection.remove("2");
        assertThrows(ConcurrentModificationException.class, getter::getNextElement);
    }

    @Test
    void testElementsGetterWithConcurrentInsert() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");

        ElementsGetter<String> getter = collection.createElementsGetter();
        assertTrue(getter.hasNextElement());
        assertEquals("1", getter.getNextElement());;

        collection.insert("1.5", 1);
        assertThrows(ConcurrentModificationException.class, getter::getNextElement);
    }

    @Test
    void testElementsGetterWithConcurrentClear() {
        ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
        collection.add("1");
        collection.add("2");
        collection.add("3");

        ElementsGetter<String> getter = collection.createElementsGetter();
        assertTrue(getter.hasNextElement());
        assertEquals("1", getter.getNextElement());;

        collection.clear();
        assertThrows(ConcurrentModificationException.class, getter::getNextElement);
    }
}
