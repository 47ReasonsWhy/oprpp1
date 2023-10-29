package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Collection of objects implemented as an array indexed list.
 * <p>
 * Duplicate elements are allowed.
 * Storage of null references is not allowed.
 *
 * @param <T> type of elements stored in the collection
 *
 * @see List
 * @see Collection
 * @see ElementsGetter
 *
 * @version 3.0
 * @author Marko Šelendić
 */
public class ArrayIndexedCollection<T> implements List<T> {

    /**
     * Default capacity of the array.
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * Current number of elements stored in the array.
     */
    private int size;

    /**
     * Array of object references.
     * Duplicate references are allowed.
     * Null references are NOT allowed.
     */
    private T[] elements;

    /**
     * Number of modifications made to the collection.
     */
    private long modificationCount = 0;

    /**
     * Creates an empty collection with default initial capacity.
     */
    public ArrayIndexedCollection() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty collection with the given initial capacity.
     *
     * @param initialCapacity initial capacity of the collection
     * @throws IllegalArgumentException if initial capacity is less than 1
     */
    @SuppressWarnings("unchecked")
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0.");
        }

        this.size = 0;
        this.elements = (T[]) new Object[initialCapacity];
    }

    /**
     * Creates a collection with the same elements as the given collection.
     * The initial capacity of the collection is the default value or the size of the given collection,
     * whichever is greater.
     *
     * @param other collection whose elements will be copied into this collection
     * @throws NullPointerException if other collection is null
     */
    public ArrayIndexedCollection(Collection<? extends T> other) {
        this(other, DEFAULT_CAPACITY);
    }

    /**
     * Creates a collection with the same elements as the given collection and with the given initial capacity.
     * The initial capacity of the collection is the given capacity or the size of the given collection,
     * whichever is greater.
     *
     * @param other collection whose elements will be copied into this collection
     * @param initialCapacity initial capacity of the collection
     * @throws NullPointerException if other collection is null
     * @throws IllegalArgumentException if initial capacity is less than 1
     */
    @SuppressWarnings("unchecked")
    public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
        if (other == null) {
            throw new NullPointerException("Other collection must not be null.");
        }

        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0.");
        }

        if (initialCapacity < other.size()) {
            initialCapacity = other.size();
        }

        this.elements = (T[]) new Object[initialCapacity];
        this.addAll(other);
    }


    /**
     * Returns the current capacity of the collection.
     *
     * @return current capacity of the collection
     */
    public int getCapacity() {
        return this.elements.length;
    }

    /**
     * @see Collection#size()
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * @see Collection#add(T)
     * @throws NullPointerException if the value is null
     */
    @Override
    @SuppressWarnings("unchecked")
    public void add(T value) {
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        if (this.size == this.elements.length) {
            this.elements = (T[]) this.newArrayWithDoubleCapacity();
        }

        this.elements[this.size] = value;
        this.size++;
        modificationCount++;
    }

    /**
     * Doubles the capacity of the elements array.
     *
     * @return new array with double capacity
     */
    private Object[] newArrayWithDoubleCapacity() {
        Object[] newElements = new Object[this.elements.length * 2];
        if (this.size >= 0) System.arraycopy(this.elements, 0, newElements, 0, this.size);
        return newElements;
    }

    /**
     * @see Collection#contains(Object)
     */
    @Override
    public boolean contains(Object value) {
        if (value == null || this.isEmpty()) {
            return false;
        }
        return this.indexOf(value) != -1;
    }

    /**
     * Removes a single instance of the given value from this collection if it exists.
     * Shifts the elements at greater positions one place toward the beginning of the array.
     *
     * @param value value to be removed from the collection
     * @return true if the collection contains given value, false otherwise
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object value) {
        if (value == null || this.isEmpty()) {
            return false;
        }

        for (int i = 0; i < this.size; i++) {
            if (this.elements[i].equals(value)) {
                this.remove(i);
                modificationCount++;
                return true;
            }
        }

        return false;
    }

    /**
     * @see Collection#toArray()
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        System.arraycopy(this.elements, 0, array, 0, this.size);

        return array;
    }

    /**
     * @see Collection#toArray(T[])
     * @throws NullPointerException if the given array is null
     * @throws ArrayStoreException if an element copied from this collection is not of a runtime type
     *                             that can be stored in the new array
     */
    @Override
    @SuppressWarnings("unchecked")
    public  T[] toArray(T[] a) {
        if (a == null) {
            throw new NullPointerException("Given array must not be null.");
        }
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    /**
     * @see List#get(int)
     */
    @Override
    public T get(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException("Index must be between 0 and size-1.");
        }

        return this.elements[index];
    }

    /**
     * @see Collection#clear()
     */
    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.elements[i] = null;
        }
        this.size = 0;
        modificationCount++;
    }

    /**
     * @see List#insert(T, int)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void insert(T value, int position) {
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        if (position < 0 || position > this.size) {
            throw new IndexOutOfBoundsException("Position must be between 0 and size.");
        }

        if (this.size == this.elements.length) {
            this.elements = (T[]) this.newArrayWithDoubleCapacity();
        }

        for (int i = this.size; i > position; i--) {
            this.elements[i] = this.elements[i - 1];
        }

        this.elements[position] = value;
        this.size++;
        modificationCount++;
    }

    /**
     * @see List#indexOf(Object)
     */
    @Override
    public int indexOf(Object value) {
        if (value == null) {
            return -1;
        }

        for (int i = 0; i < this.size; i++) {
            if (this.elements[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * @see List#remove(int)
     */
    public void remove(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException("Index must be between 0 and size-1.");
        }

        for (int i = index; i < this.size - 1; i++) {
            this.elements[i] = this.elements[i + 1];
        }

        this.elements[this.size - 1] = null;
        this.size--;
        modificationCount++;
    }

    /**
     * Implementation of the ElementsGetter interface for the ArrayIndexedCollection class.
     */
    private static class ArrayElementsGetter<T> implements ElementsGetter<T> {
        /**
         * Collection whose elements ElementsGetter will be getting.
         */
        private final ArrayIndexedCollection<T> collection;

        /**
         * Current index ElementsGetter has reached in the collection.
         */
        private int index;

        /**
         * Number of modifications made to the collection when this ElementsGetter was created.
         */
        private final long savedModificationCount;

        /**
         * Creates a new instance of ArrayElementsGetter.
         *
         * @param collection collection whose elements ElementsGetter will be getting
         */
        private ArrayElementsGetter(ArrayIndexedCollection<T> collection) {
            this.collection = collection;
            this.index = 0;
            this.savedModificationCount = collection.modificationCount;
        }

        /**
         * @see ElementsGetter#hasNextElement()
         * @throws ConcurrentModificationException if the collection has been modified after ElementsGetter was created
         */
        @Override
        public boolean hasNextElement() {
            if (this.savedModificationCount != this.collection.modificationCount) {
                throw new ConcurrentModificationException("The collection has been modified.");
            }
            return this.index < this.collection.size;
        }

        /**
         * @see ElementsGetter#getNextElement()
         * @throws ConcurrentModificationException if the collection has been modified after ElementsGetter was created
         */
        @Override
        public T getNextElement() {
            if (this.savedModificationCount != this.collection.modificationCount) {
                throw new ConcurrentModificationException("The collection has been modified.");
            }

            if (!this.hasNextElement()) {
                throw new NoSuchElementException("The collection has no more elements.");
            }

            return this.collection.elements[this.index++];
        }
    }

    /**
     * Creates and returns a new instance of ArrayElementsGetter.
     *
     * @return new instance of ArrayElementsGetter
     */
    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new ArrayElementsGetter<>(this);
    }
}
