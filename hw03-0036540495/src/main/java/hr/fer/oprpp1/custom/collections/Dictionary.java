package hr.fer.oprpp1.custom.collections;

/**
 * This class represents a collection of key-value pairs.
 * @param <K> type of key
 * @param <V> type of value
 */
public class Dictionary<K,V> {
    /**
     * Internally managed collection of key-value pairs as {@link Entry} objects.
     */
    private final ArrayIndexedCollection<Entry<K,V>> collection;
    
    /**
    * Constructs a new dictionary.
    */
    public Dictionary() {
        this.collection = new ArrayIndexedCollection<>();
    }

    /**
    * Checks if the dictionary is empty.
     *
    * @return true if the dictionary is empty, false otherwise
    */
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    /**
    * Returns the number of elements in the dictionary.
     *
    * @return number of elements in the dictionary
    */
    public int size() {
        return this.collection.size();
    }

    /**
    * Removes all elements from the dictionary.
    */
    public void clear() {
        this.collection.clear();
    }

    /**
    * Adds a new {@link Entry} to the dictionary.
     *
    * @param key key of the new entry
    * @param value value of the new entry
    * @throws NullPointerException if the key is null
    */
    public void put(K key, V value) {
        if (key == null) throw new NullPointerException("Key cannot be null.");
        for (int i = 0; i < this.collection.size(); i++) {
            Entry<K,V> entry = this.collection.get(i);
            if (entry.getKey().equals(key)) {
                entry.value = value;
                return;
            }
        }
        Entry<K,V> entry = new Entry<>(key, value);
        this.collection.add(entry);
    }

    /**
    * Returns the value of the entry with the given key.
     *
    * @param key key of the pair
    * @return value of the pair
    */
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        if (key == null || this.collection.isEmpty()) {
            return null;
        }
        K castKey;
        try {
            castKey = (K) key;
        } catch (ClassCastException ex) {
            return null;
        }
        for (int i = 0; i < this.collection.size(); i++) {
            Entry<K,V> entry = this.collection.get(i);
            if (entry.getKey().equals(castKey)) return entry.getValue();
        }
        return null;
    }

    /**
     * Removes the entry with the given key from the dictionary and returns its value.
     *
     * @param key key of the entry to be removed
     * @return value of the removed entry or null if the entry with the given key doesn't exist
     */
    V remove(K key) {
        if (key == null || this.collection.isEmpty()) {
            return null;
        }
        for (int i = 0; i < this.collection.size(); i++) {
            Entry<K,V> entry = this.collection.get(i);
            if (entry.getKey().equals(key)) {
                this.collection.remove(i);
                return entry.getValue();
            }
        }
        return null;
    }

    /**
    * Represents a key-value pair.
     *
    * @param <K> type of key
    * @param <V> type of value
    */
    private static class Entry<K,V> {
        /**
         * Key of the entry.
         */
        private final K key;

        /**
         * Value of the entry.
         */
        private V value;

        /**
        * Constructs a new entry with the given key and value.
         *
        * @param key   key of the pair
        * @param value value of the pair
        */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
        * Returns the key of the pair.
         *
        * @return key of the pair
        */
        public K getKey() {
            return this.key;
        }

        /**
         * Returns the value of the pair.
         *
         * @return value of the pair
         */
        public V getValue() {
            return this.value;
        }
    }
}
