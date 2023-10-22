package hr.fer.oprpp1.custom.collections;

/**
 * Collection of objects implemented as an indexed linked list.
 * <p>
 * Duplicate elements are allowed.
 * Storage of null references is not allowed.
 *
 * @author Marko Šelendić
 * @version 1.0
 */
public class LinkedListIndexedCollection extends Collection {

    private static class ListNode {
        /**
         * Reference to the previous node in the list.
         */
        ListNode previous;

        /**
         * Reference to the next node in the list.
         */
        ListNode next;

        /**
         * Value of the node.
         */
        Object value;

        /**
         * Creates a new node with the given value.
         *
         * @param value value of the node
         */
        ListNode(Object value) {
            this.previous = null;
            this.next = null;
            this.value = value;
        }
    }

    /**
     * Current number of elements in the collection.
     */
    private int size;

    /**
     * Reference to the first node of the list.
     */
    private ListNode first;

    /**
     * Reference to the last node of the list.
     */
    private ListNode last;

    /**
     * Creates an empty collection.
     */
    public LinkedListIndexedCollection() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    /**
     * Creates a new collection and copies all elements from the given collection into it.
     *
     * @param other collection whose elements are copied into this collection
     */
    public LinkedListIndexedCollection(Collection other) {
        this();
        this.addAll(other);
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
     * <p>
     * The element is added at the end of the collection (at the biggest index).
     *
     * @param value object to be added to the collection
     * @throws NullPointerException if value is null
     */
    @Override
    public void add(Object value) {
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        ListNode newNode = new ListNode(value);

        if (this.isEmpty()) {
            this.first = newNode;
        } else {
            this.last.next = newNode;
            newNode.previous = this.last;
        }
        this.last = newNode;

        this.size++;
    }

    /**
     * Returns true if the collection contains the given value.
     *
     * @param value value to be checked if it is in the collection
     * @return true if the collection contains given value, false otherwise
     */
    @Override
    public boolean contains(Object value) {
        return this.indexOf(value) != -1;
    }

    /**
     * Removes the first occurrence of the given value from the collection.
     *
     * @param value value to be removed from the collection
     * @return true if the collection contains given value, false otherwise
     */
    @Override
    public boolean remove(Object value) {
        int index = this.indexOf(value);
        if (index == -1) {
            return false;
        }

        this.remove(index);
        return true;
    }

    /**
     * Returns a new array filled with all elements from this collection.
     * <p>
     * The array is the same size as the collection.
     * <p>
     * Does not modify the collection.
     *
     * @return new array filled with objects from this collection
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];

        ListNode current = this.first;
        for (int i = 0; i < this.size; i++) {
            array[i] = current.value;
            current = current.next;
        }

        return array;
    }

    /**
     * Calls processor.process() for each element of this collection.
     *
     * @param processor processor whose method process() is called for each element of this collection
     * @throws NullPointerException if processor is null
     */
    @Override
    public void forEach(Processor processor) {
        if (processor == null) {
            throw new NullPointerException("Processor must not be null.");
        }

        ListNode current = this.first;
        while (current != null) {
            processor.process(current.value);
            current = current.next;
        }
    }

    /**
     * Adds all elements from the given collection to this collection.
     * <p>
     * The given collection remains unchanged.
     *
     * @param other collection whose elements are added to this collection
     * @throws NullPointerException if the other collection is null
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
     * Returns the object that is stored in linked list at position index.
     * <p>
     * Valid indexes are 0 to size-1.
     *
     * @param index position of the object to be returned
     * @return object at the given index
     * @throws IndexOutOfBoundsException if index is not in [0, size-1] range
     */
    public Object get(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException("Index must be between 0 and size-1.");
        }

        ListNode node = findNodeAtIndex(index);

        return node.value;
    }

    /**
     * Removes all elements from this collection.
     */
    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * Inserts (does not overwrite) the given value at the given position in linked-list.
     * <p>
     * Elements starting from this position are shifted one position up.
     * The legal positions are 0 to size.
     *
     * @param value    value to be inserted
     * @param position position at which the value will be inserted
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

        if (position == this.size) {
            this.add(value);
            return;
        }

        ListNode newNode = new ListNode(value);

        if (position == 0) {
            newNode.next = this.first;
            this.first.previous = newNode;
            this.first = newNode;
        } else {
            ListNode node = findNodeAtIndex(position);
            newNode.previous = node.previous;
            newNode.next = node;
            node.previous.next = newNode;
            node.previous = newNode;
        }

        this.size++;
    }

    /**
     * Returns the node at the given index.
     *
     * @param index index of the node to be returned
     * @return node at the given index
     */
    private ListNode findNodeAtIndex(int index) {
        ListNode current;
        if (index < this.size / 2) {
            current = this.first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = this.last;
            for (int i = this.size - 1; i > index; i--) {
                current = current.previous;
            }
        }
        return current;
    }

    /**
     * Searches the collection and returns the index of the first occurrence of the given value.
     * <p>
     * Returns -1 if the value is not found or if the value is null.
     *
     * @param value value to be searched for
     * @return index of the first occurrence of the given value, -1 if the value is not found or if the value is null
     */
    public int indexOf(Object value) {
        if (value == null) {
            return -1;
        }

        ListNode current = this.first;
        for (int i = 0; i < this.size; i++) {
            if (current.value.equals(value)) {
                return i;
            }
            current = current.next;
        }

        return -1;
    }

    /**
     * Removes element at the specified index from collection.
     * <p>
     * Shifts the elements at greater positions one place toward the beginning of the array.
     *
     * @param index position of the object to be removed
     * @throws IndexOutOfBoundsException if the index is not between 0 and size-1
     */
    public void remove(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException("Index must be between 0 and size-1.");
        }

        ListNode node = findNodeAtIndex(index);

        if (index == 0) {
            this.first = node.next;
        } else if (index == this.size - 1) {
            this.last = node.previous;
        } else {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }

        this.size--;
    }
}
