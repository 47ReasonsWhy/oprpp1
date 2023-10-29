package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class represents a simple hash table.
 *
 * @param <K> key
 * @param <V> value
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {
    /**
     * This class models a single entry in the hash table.
     */
    public static class TableEntry<K,V> {

        /**
         * Key of the entry.
         */
        private final K key;

        /**
         * Value of the entry.
         */
        private V value;

        /**
         * Reference to the next entry.
         */
        private TableEntry<K, V> next;

        /**
         * Constructor for the TableEntry
         *
         * @param key   key of the entry
         * @param value value of the entry
         * @param next  reference to the next entry
         */
        public TableEntry(K key, V value, TableEntry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * Getter for the key of the entry.
         *
         * @return key
         */
        public K getKey() {
            return key;
        }

        /**
         * Getter for the value of the entry.
         *
         * @return value
         */
        public V getValue() {
            return value;
        }

        /**
         * Setter for the value of the entry.
         *
         * @param value value to be set
         */
        public void setValue(V value) {
            this.value = value;
        }
    }

    /**
    * Default capacity of the hash table
    */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * Table of entries.
     */
    private TableEntry<K,V>[] table;

    /**
     * Number of entries in the hash table.
     */
    private int size;

    /**
     * Counter for the number of modifications of the hash table.
     */
    private int modificationCount;

    /**
     * Constructs a new hash table with the default capacity (16).
     */
    public SimpleHashtable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new hash table with the given capacity.
     * <p>
     * The capacity of the hash table is the first power of 2 that is greater than or equal to the given capacity.
     *
     * @param capacity capacity of the hash table
     *
     * @throws IllegalArgumentException if the capacity is less than 1
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        capacity = (int) Math.pow(2, Math.ceil(Math.log(capacity) / Math.log(2)));
        table = (TableEntry<K,V>[]) new TableEntry[capacity];
        size = 0;
        modificationCount = 0;
    }

    /**
     * Returns the number of entries in the hash table.
     *
     * @return number of entries in the hash table
     */
    public int size() {
        return size;
    }

    /**
     * Returns the capacity of the hash table.
     *
     * @return capacity of the hash table
     */
    public int capacity() {
        return table.length;
    }

    /**
     * Adds the entry with the given key and value to the hash table.
     * <p>
     * If the entry with the given key already exists, the value of the entry is overwritten with the given value.
     *
     * @param key   key of the entry
     * @param value value of the entry
     * @return value of the overwritten entry or null if a new entry is added
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Key must not be null");
        }
        // increment the modification count
        modificationCount++;
        // calculate the slot with slot=|hashCode()| % table.length
        int slot = Math.abs(key.hashCode()) % table.length;
        ///* debug */ System.out.println("Math.abs(key.hashCode()): " + Math.abs(key.hashCode()));
        TableEntry<K, V> entry = table[slot];
        // if the slot is empty, add the entry to the slot and return null
        if (entry == null) {
            table[slot] = new TableEntry<>(key, value, null);
            size++;
            return null;
        }
        // otherwise, iterate through the entries in the slot and check if the key exists
        // if it does, overwrite the value and return the old value
        TableEntry<K, V> previousEntry = null;
        while (entry != null) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
            previousEntry = entry;
            entry = entry.next;
        }
        // if the key does not exist, first check if the table needs to be resized
        if (size >= table.length * 0.75) {
            // if it does, resize the table and add the entry to the slot
            // (we have to call the method again after resizing because the slot might change)
            // also, one increment of the modification count is enough
            int oldModificationCount = modificationCount;
            resize();
            modificationCount = oldModificationCount;
            return this.put(key, value);
        }
        // otherwise, add the entry to the slot and return null
        previousEntry.next = new TableEntry<>(key, value, null);
        size++;
        return null;
    }

    /**
     * Resizes the hash table.
     * <p>
     * The new capacity is two times the old capacity.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        TableEntry<K, V>[] oldTable = this.toArray();
        table = (TableEntry<K, V>[]) new TableEntry[table.length * 2];
        size = 0;
        for (TableEntry<K, V> entry : oldTable) {
            if (entry != null) {
                this.put(entry.key, entry.value);
            }
        }
    }

    /**
     * Returns the value of the entry with the given key.
     *
     * @param key key of the entry
     * @return value of the entry or null if the entry does not exist
     */
    public V get(Object key) {
        if (key == null) {
            return null;
        }
        // calculate the slot with slot=|hashCode()| % table.length
        int slot = Math.abs(key.hashCode()) % table.length;
        TableEntry<K, V> entry = table[slot];
        // if the slot is empty, return null
        if (entry == null) {
            return null;
        }
        // otherwise, iterate through the entries in the slot and check if the key exists
        // if it does, return the value
        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        // if the key does not exist, return null
        return null;
    }

    /**
     * Checks if the hash table contains the entry with the given key.
     *
     * @param key key of the entry
     * @return true if the hash table contains the entry with the given key, false otherwise
     */
    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        // calculate the slot with slot=|hashCode()| % table.length
        int slot = Math.abs(key.hashCode()) % table.length;
        TableEntry<K, V> entry = table[slot];
        // if the slot is empty, return false
        if (entry == null) {
            return false;
        }
        // otherwise, iterate through the entries in the slot and check if the key exists
        // if it does, return true
        while (entry != null) {
            if (entry.key.equals(key)) {
                return true;
            }
            entry = entry.next;
        }
        // if the key does not exist, return false
        return false;
    }

    /**
     * Checks if the hash table contains the entry with the given value.
     *
     * @param value value of the entry
     * @return true if the hash table contains the entry with the given value, false otherwise
     */
    public boolean containsValue(Object value) {
        // iterate through all the slots in the table
        for (TableEntry<K, V> entry : table) {
            // if the slot is empty, continue
            if (entry == null) {
                continue;
            }
            // otherwise, iterate through the entries in the slot and check if the value exists
            // if it does, return true
            while (entry != null) {
                if (entry.value.equals(value)) {
                    return true;
                }
                entry = entry.next;
            }
        }
        // if the value does not exist, return false
        return false;
    }

    /**
     * Removes the entry with the given key from the hash table and returns its value.
     *
     * @param key key of the entry
     * @return value of the entry or null if the entry does not exist
     */
    public V remove(Object key) {
        if (key == null) {
            return null;
        }
        // calculate the slot with slot=|hashCode()| % table.length
        int slot = Math.abs(key.hashCode()) % table.length;
        TableEntry<K, V> entry = table[slot];
        // if the slot is empty, return null
        if (entry == null) {
            return null;
        }
        // if the entry is the first in the slot, remove it and return its value
        if (entry.key.equals(key)) {
            table[slot] = entry.next;
            size--;
            modificationCount++;
            return entry.value;
        }
        // otherwise, iterate through the entries in the slot and check if the key exists
        // if it does, remove it and return its value
        while (entry.next != null) {
            if (entry.next.key.equals(key)) {
                V value = entry.next.value;
                entry.next = entry.next.next;
                size--;
                modificationCount++;
                return value;
            }
            entry = entry.next;
        }
        // if the key does not exist, return null
        return null;
    }

    /**
     * Checks if the hash table is empty.
     *
     * @return true if the hash table is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the string representation of the hash table.
     *
     * @return string representation of the hash table
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        ///* debug */ int i = 0;
        // iterate through all the slots in the table
        for (TableEntry<K, V> entry : table) {
            ///* debug */ sb.append("(slot ").append(i++).append(":) ");
            // if the slot is empty, continue
            if (entry == null) {
                continue;
            }
            // otherwise, iterate through the entries in the slot and append them to the string
            while (entry != null) {
                sb.append(entry.key).append("=").append(entry.value).append(", ");
                entry = entry.next;
            }
        }
        // remove the last comma and space
        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.append("]").toString();
    }

    /**
     * Returns the array of entries in the hash table.
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K, V>[] toArray() {
        TableEntry<K, V>[] array = (TableEntry<K, V>[]) new TableEntry[size];
        int i = 0;
        // iterate through all the slots in the table
        for (TableEntry<K, V> entry : table) {
            // if the slot is empty, continue
            if (entry == null) {
                continue;
            }
            // otherwise, iterate through the entries in the slot and add them to the array
            while (entry != null) {
                array[i++] = entry;
                entry = entry.next;
            }
        }
        return array;
    }

    /**
     * Clears the hash table.
     */
    public void clear() {
        // iterate through all the slots in the table
        Arrays.fill(table, null);
        size = 0;
        modificationCount++;
    }

    /**
     * Returns the iterator for the hash table.
     *
     * @return iterator for the hash table
     */
    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl();
    }

    /**
     * This class represents an iterator for the hash table.
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {
        /**
         * Index of the current slot.
         */
        private int slotIndex;

        /**
         * "Index" of the current entry.
         */
        private int entryIndex;

        /**
         * Current entry.
         */
        private TableEntry<K,V> entry;

        /**
         * Counter for the saved number of modifications of the hash table.
         */
        private int savedModificationCount;

        /**
         * "Entry removed"-flag.
         * <p>
         * True if the current entry has been removed, false otherwise.
         */
        private boolean removed;

        /**
         * Constructs a new iterator for the hash table.
         */
        public IteratorImpl() {
            slotIndex = -1;
            entryIndex = 0;
            entry = null;
            savedModificationCount = modificationCount;
        }

        /**
         * Checks if there are more entries in the hash table.
         *
         * @return true if there are more entries in the hash table, false otherwise
         *
         * @throws ConcurrentModificationException if the hash table has been modified since the iterator was created
         *                                         (except through the iterator's own remove() method)
         */
        @Override
        public boolean hasNext() {
            checkModificationCount();
            // if the entry index is less than the size of the table, there are more entries
            return entryIndex < size;
        }

        /**
         * Returns the next entry in the hash table.
         *
         * @return next entry in the hash table
         *
         * @throws ConcurrentModificationException if the hash table has been modified since the iterator was created
         *                                         (except through the iterator's own remove() method)
         * @throws NoSuchElementException          if there are no more entries
         */
        @Override
        public SimpleHashtable.TableEntry<K,V> next() {
            // check if the modification count has changed
            checkModificationCount();
            // set the removed flag to false
            removed = false;
            // if there are no more entries, throw an exception
            if (entryIndex >= size) {
                throw new NoSuchElementException("There are no more elements.");
            }
            // if the current entry is null or the next entry is null, find the next entry
            if (entry == null || entry.next == null) {
                // increment the slot index
                slotIndex++;
                // find the next non-empty slot
                while (slotIndex < table.length && table[slotIndex] == null) {
                    slotIndex++;
                }
                // set the current entry to the first entry in the slot
                entry = table[slotIndex];
            } else {
                // otherwise, set the current entry to the next entry
                entry = entry.next;
            }
            entryIndex++;
            return entry;
        }

        /**
         * Removes the current entry from the hash table.
         *
         * @throws ConcurrentModificationException if the hash table has been modified since the iterator was created
         *                                         (except through this method)
         * @throws IllegalStateException           if the current entry is null or the entry has already been removed
         */
        @Override
        public void remove() {
            checkModificationCount();
            // if the current entry is null, throw an exception
            if (entry == null || removed) {
                throw new IllegalStateException("There is no current element.");
            }
            // otherwise, remove the current entry
            SimpleHashtable.this.remove(entry.key);
            // set the removed flag to true
            removed = true;
            // decrement the entry index
            entryIndex--;
            // increment the saved modification count
            savedModificationCount++;
        }

        /**
         * Checks if the modification count has changed since the iterator was created.
         *
         * @throws ConcurrentModificationException if the hash table has been modified since the iterator was created
         *                                         (except through the iterator's own remove() method)
         */
        private void checkModificationCount() {
            if (savedModificationCount != modificationCount) {
                throw new ConcurrentModificationException("The hash table has been modified.");
            }
        }
    }
}
