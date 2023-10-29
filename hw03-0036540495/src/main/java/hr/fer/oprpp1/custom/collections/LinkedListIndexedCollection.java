package hr.fer.oprpp1.custom.collections;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Collection of objects implemented as an indexed linked list.
 * <p>
 * Duplicate elements are allowed.
 * Storage of null references is not allowed.
 *
 * @param <T> type of elements stored in this collection
 *
 * @see List
 * @see Collection
 * @see ElementsGetter
 *
 * @version 3.0
 * @author Marko Šelendić
 */
public class LinkedListIndexedCollection<T> implements List<T> {

    private static class ListNode<T> {
        /**
         * Reference to the previous node in the list.
         */
        ListNode<T> previous;

        /**
         * Reference to the next node in the list.
         */
        ListNode<T> next;

        /**
         * Value of the node.
         */
        T value;

        /**
         * Creates a new node with the given value.
         *
         * @param value value of the node
         */
        ListNode(T value) {
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
    private ListNode<T> first;

    /**
     * Reference to the last node of the list.
     */
    private ListNode<T> last;

    /**
     * Number of modifications made to the collection.
     */
    private long modificationCount = 0;

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
    public LinkedListIndexedCollection(Collection<? extends T> other) {
        this();
        this.addAll(other);
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
     * @throws NullPointerException if value is null
     */
    @Override
    public void add(T value) {
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        ListNode<T> newNode = new ListNode<>(value);

        if (this.isEmpty()) {
            this.first = newNode;
        } else {
            this.last.next = newNode;
            newNode.previous = this.last;
        }
        this.last = newNode;
        this.size++;
        modificationCount++;
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
     * @see Collection#remove(Object)
     */
    @Override
    public boolean remove(Object value) {
        int index = this.indexOf(value);
        if (index == -1) {
            return false;
        }

        this.remove(index);
        modificationCount++;
        return true;
    }

    /**
     * @see Collection#toArray()
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];

        ListNode<T> current = this.first;
        for (int i = 0; i < this.size; i++) {
            array[i] = current.value;
            current = current.next;
        }

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
        if (a.length < size) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (ListNode<T> x = first; x != null; x = x.next) {
            result[i++] = x.value;
        }
        if (a.length > size) {
            for (int j = size; j < a.length; j++) {
                result[j] = null;
            }
        }
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

        ListNode<T> node = findNodeAtIndex(index);
        return node.value;
    }

    /**
     * @see Collection#clear()
     */
    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.size = 0;
        modificationCount++;
    }

    /**
     * @see List#insert(Object, int)
     */
    @Override
    public void insert(T value, int position) {
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

        ListNode<T> newNode = new ListNode<>(value);

        if (position == 0) {
            newNode.next = this.first;
            this.first.previous = newNode;
            this.first = newNode;
        } else {
            ListNode<T> node = findNodeAtIndex(position);
            newNode.previous = node.previous;
            newNode.next = node;
            node.previous.next = newNode;
            node.previous = newNode;
        }

        this.size++;
        modificationCount++;
    }

    /**
     * Returns the node at the given index.
     *
     * @param index index of the node to be returned
     * @return node at the given index
     */
    private ListNode<T> findNodeAtIndex(int index) {
        ListNode<T> current;
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
     * @see List#indexOf(Object)
     */
    @Override
    public int indexOf(Object value) {
        if (value == null) {
            return -1;
        }

        ListNode<T> current = this.first;
        for (int i = 0; i < this.size; i++) {
            if (current.value.equals(value)) {
                return i;
            }
            current = current.next;
        }

        return -1;
    }

    /**
     * @see List#remove(int)
     */
    @Override
    public void remove(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException("Index must be between 0 and size-1.");
        }

        ListNode<T> node = findNodeAtIndex(index);

        if (index == 0) {
            this.first = node.next;
        } else if (index == this.size - 1) {
            this.last = node.previous;
        } else {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }

        this.size--;
        modificationCount++;
    }

    private static class LinkedListElementsGetter<T> implements ElementsGetter<T> {
        /**
         * Collection whose elements ElementsGetter will be getting.
         */
        private final LinkedListIndexedCollection<T> collection;

        /**
         * Current node ElementsGetter has reached in the collection.
         */
        private ListNode<T> node;

        /**
         * Number of modifications made to the collection when this ElementsGetter was created.
         */
        private final long savedModificationCount;

        /**
         * Creates a new ElementsGetter for the given collection.
         *
         * @param collection collection whose elements ElementsGetter will be getting
         */
        public LinkedListElementsGetter(LinkedListIndexedCollection<T> collection) {
            this.collection = collection;
            this.node = collection.first;
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
            return this.node != null;
        }

        /**
         * @see ElementsGetter#getNextElement()
         * @throws ConcurrentModificationException if the collection has been modified after ElementsGetter was created
         *
         */
        @Override
        public T getNextElement() {
            if (this.savedModificationCount != this.collection.modificationCount) {
                throw new ConcurrentModificationException("The collection has been modified.");
            }

            if (!this.hasNextElement()) {
                throw new NoSuchElementException("The collection has no more elements.");
            }

            T value = this.node.value;
            this.node = this.node.next;
            return value;
        }
    }

    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new LinkedListElementsGetter<>(this);
    }
}
