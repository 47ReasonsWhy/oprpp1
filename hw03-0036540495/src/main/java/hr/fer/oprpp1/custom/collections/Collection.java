package hr.fer.oprpp1.custom.collections;

/**
 * An interface that represents a general collection of objects.
 * <p>
 * It is used as a base class for other collections.
 *
 * @param <T> type of elements in the collection
 *
 * @see ArrayIndexedCollection
 * @see LinkedListIndexedCollection
 * @version 3.0
 * @author Marko Šelendić
 */
public interface Collection<T> {

    /**
     * Returns true only if the collection contains no objects and false otherwise.
     *
     * @return true if the collection contains no objects, false otherwise
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns the number of currently stored objects in this collection.
     *
     * @return number of currently stored objects
     */
    int size();

    /**
     * Appends the given object to the end of this collection.
     *
     * @param value object to be added to the collection
     */
    void add(T value);

    /**
     * Returns true only if the collection contains given value as determined by equals method.
     *
     * @param value value to be checked if it is in the collection
     * @return true if the collection contains given value, false otherwise
     */
    boolean contains(Object value);

    /**
     * Removes a single instance of the given value from this collection if it exists.
     *
     * @param value value to be removed from the collection
     * @return true if the collection contains given value, false otherwise
     */
    boolean remove(Object value);

    /**
     * Allocates new array with size equals to the size of this collection,
     * fills it with collection content and returns the array.
     *
     * @return new array filled with objects from this collection
     */
    Object[] toArray();

    /**
     * Allocates new array with size equals to the size of this collection,
     * fills it with collection content and returns the array.
     * <p>
     * The runtime type of the returned array is that of the specified array.
     *
     * @param a array into which the elements of this collection are to be stored, if it is big enough;
     *          otherwise, a new array of the same runtime type is allocated for this purpose
     * @return array filled with objects from this collection
     */
    T[] toArray(T[] a);

    /**
     * Calls processor.process(.) for each element of this collection.
     *
     * @param processor processor whose method process(.) is called for each element of this collection
     * @throws NullPointerException if the given processor is null
     */
   default void forEach(Processor<? super T> processor) {
       if (processor == null) {
           throw new NullPointerException("Processor must not be null.");
       }
        ElementsGetter<T> getter = this.createElementsGetter();
        while (getter.hasNextElement()) {
            processor.process(getter.getNextElement());
        }
   }

    /**
     * Adds all elements from the given collection to this collection.
     * <p>
     * The given collection remains unchanged.
     *
     * @param other collection whose elements are added to this collection
     * @throws NullPointerException if other collection is null
     */
    default void addAll(Collection<? extends T> other) {
        if (other == null) {
            throw new NullPointerException("Other collection must not be null.");
        }
        other.forEach(this::add);
    }

    /**
     * Removes all elements from this collection.
     */
    void clear();

    /**
     * Creates a new ElementsGetter for this collection.
     *
     * @return new ElementsGetter for this collection
     */
    ElementsGetter<T> createElementsGetter();

    /**
     * Adds all elements which satisfy the condition of the given tester from the given collection to this collection.
     * @param col    collection whose elements are being added to this collection
     * @param tester tester whose test method is called for each element of the given collection
     * @throws NullPointerException if the given collection or tester is null
     */
    default <S extends T> void addAllSatisfying(Collection<S> col, Tester<? super S> tester) {
        if (col == null || tester == null) {
            throw new NullPointerException("Collection and tester must not be null.");
        }

        ElementsGetter<S> getter = col.createElementsGetter();
        while (getter.hasNextElement()) {
            S next = getter.getNextElement();
            if (tester.test(next)) {
                this.add(next);
            }
        }
    }
}
