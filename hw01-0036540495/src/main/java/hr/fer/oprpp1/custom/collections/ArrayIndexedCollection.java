package hr.fer.oprpp1.custom.collections;

/**
 * Collection of objects implemented as an array indexed list.
 * <p>
 * Duplicate elements are allowed.
 * Storage of null references is not allowed.
 *
 * @author Marko Šelendić
 * @version 1.0
 */
public class ArrayIndexedCollection extends Collection {

    /**
     * Current number of elements stored in the array.
     */
    private int size;

    /**
     * Array of object references.
     * Duplicate references are allowed.
     * Null references are NOT allowed.
     */
    private Object[] elements;

    /**
     * Creates an empty collection with initial capacity of 16.
     */
    public ArrayIndexedCollection() {
        this(16);
    }

    /**
     * Creates an empty collection with the given initial capacity.
     *
     * @param initialCapacity initial capacity of the collection
     * @throws IllegalArgumentException if initial capacity is less than 1
     */
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0.");
        }

        this.size = 0;
        this.elements = new Object[initialCapacity];
    }

    /**
     * Creates a collection with the same elements as the given collection.
     * The initial capacity of the collection is 16 or the size of the given collection,
     * whichever is greater.
     *
     * @param other collection whose elements will be copied into this collection
     * @throws NullPointerException if other collection is null
     */
    public ArrayIndexedCollection(Collection other) {
        this(other, 16);
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
    public ArrayIndexedCollection(Collection other, int initialCapacity) {
        if (other == null) {
            throw new NullPointerException("Other collection must not be null.");
        }

        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0.");
        }

        if (initialCapacity < other.size()) {
            initialCapacity = other.size();
        }

        this.elements = new Object[initialCapacity];
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
     * Returns true only if the collection contains no objects.
     *
     * @return true if the collection contains no objects, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Returns the number of currently stored objects in this collection.
     *
     * @return number of currently stored objects
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given object into this collection.
     *
     * @param value object to be added to the collection
     * @throws NullPointerException if the value is null
     */
    @Override
    public void add(Object value) {
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        if (this.size == this.elements.length) {
            this.elements = this.newArrayWithDoubleCapacity();
        }

        this.elements[this.size] = value;
        this.size++;
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
     * Returns true only if the collection contains given value as determined by equals method.
     *
     * @param value value to be checked if it is in the collection
     * @return true if the collection contains given value, false otherwise
     */
    @Override
    public boolean contains(Object value) {
        return this.indexOf(value) != -1;
    }

    /**
     * Removes one occurrence of the given value from the collection.
     * Shifts the elements at greater positions one place toward the beginning of the array.
     * Returns true only if the collection contains given value as determined by equals method.
     *
     * @param value value to be removed from the collection
     * @return true if the collection contains given value, false otherwise
     */
    @Override
    public boolean remove(Object value) {
        if (value == null) {
            return false;
        }

        for (int i = 0; i < this.size; i++) {
            if (this.elements[i].equals(value)) {
                this.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
     * Returns new array with size and content equal to this collection.
     *
     * @return new array filled with objects from this collection
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        System.arraycopy(this.elements, 0, array, 0, this.size);

        return array;
    }

    /**
     * Calls processor.process(.) for each element of this collection.
     *
     * @param processor processor whose process method will be called
     * @throws NullPointerException if processor is null
     */
    @Override
    public void forEach(Processor processor) {
        if (processor == null) {
            throw new NullPointerException("Processor must not be null.");
        }
        for (int i = 0; i < this.size; i++) {
            processor.process(this.elements[i]);
        }
    }

    /**
     * Adds all elements from the given collection to this collection.
     * The given collection remains unchanged.
     *
     * @param other collection whose elements will be added to this collection
     * @throws NullPointerException if other collection is null
     */
    @Override
    public void addAll(Collection other) {
        if (other == null) {
            throw new NullPointerException("Other collection must not be null.");
        }

        for (Object element : other.toArray()) {
            this.add(element);
        }
    }

    /**
     * Returns the object that is stored in backing array at position index.
     * Valid indexes are 0 to size-1.
     *
     * @param index position of the object to be returned
     * @return object at position index
     * @throws IndexOutOfBoundsException if index is not between 0 and size-1
     */
    public Object get(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException("Index must be between 0 and size-1.");
        }

        return this.elements[index];
    }

    /**
     * Removes all elements from the collection.
     */
    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.elements[i] = null;
        }

        this.size = 0;
    }

    /**
     * Inserts (does not overwrite) the given value at the given position in array.
     * Elements at position and at greater positions are shifted one place toward the end.
     *
     * @param value    object to be inserted
     * @param position position at which the object will be inserted,
     *                 must be between 0 and size (inclusive).
     * @throws IndexOutOfBoundsException if position is not between 0 and size
     * @throws NullPointerException      if value is null
     */
    public void insert(Object value, int position) {
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        if (position < 0 || position > this.size) {
            throw new IndexOutOfBoundsException("Position must be between 0 and size.");
        }

        if (this.size == this.elements.length) {
            this.elements = this.newArrayWithDoubleCapacity();
        }

        for (int i = this.size; i > position; i--) {
            this.elements[i] = this.elements[i - 1];
        }

        this.elements[position] = value;
        this.size++;
    }

    /**
     * Searches the collection and returns the index of the first occurrence of the given value.
     * Returns -1 if the value is not found or if the value is null.
     *
     * @param value object to be searched for
     * @return index of the first occurrence of the given value
     */
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
     * Removes element at specified index from collection.
     * Shifts the elements at greater positions one place toward the beginning of the array.
     *
     * @param index position of the object to be removed
     * @throws IndexOutOfBoundsException if index is not between 0 and size-1
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
    }
}
