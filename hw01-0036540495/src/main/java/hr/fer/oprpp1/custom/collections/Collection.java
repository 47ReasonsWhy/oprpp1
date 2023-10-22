package hr.fer.oprpp1.custom.collections;

/**
 * A class that represents a general collection of objects.
 * <p>
 * It is used as a base class for other collections.
 *
 * @author Marko Šelendić
 * @version 1.0
 * @see ArrayIndexedCollection
 * @see LinkedListIndexedCollection
 */
public class Collection {
    protected Collection() {
    }

    /**
     * Returns true only if the collection contains no objects and false otherwise.
     *
     * @return true if the collection contains no objects, false otherwise
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns the number of currently stored objects in this collection.
     *
     * @return number of currently stored objects
     */
    public int size() {
        return 0;
    }

    /**
     * Adds the given object into this collection.
     *
     * @param value object to be added to the collection
     */
    public void add(Object value) {
    }

    /**
     * Returns true only if the collection contains given value as determined by equals method.
     *
     * @param value value to be checked if it is in the collection
     * @return true if the collection contains given value, false otherwise
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Returns true only if the collection contains given value as determined by equals method and removes one occurrence of it.
     *
     * @param value value to be removed from the collection
     * @return true if the collection contains given value, false otherwise
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Allocates new array with size equals to the size of this collection,
     * fills it with collection content and returns the array.
     *
     * @return new array filled with objects from this collection
     */
    public Object[] toArray() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Calls processor.process(.) for each element of this collection.
     *
     * @param processor processor whose method process(.) is called for each element of this collection
     */
    public void forEach(Processor processor) {
    }

    /**
     * Adds all elements from the given collection to this collection.
     * <p>
     * The given collection remains unchanged.
     *
     * @param other collection whose elements are added to this collection
     */
    public void addAll(Collection other) {
        class LocalProcessor extends Processor {
            @Override
            public void process(Object value) {
                Collection.this.add(value);
            }
        }
        other.forEach(new LocalProcessor());
    }

    /**
     * Removes all elements from this collection.
     */
    public void clear() {
    }
}
