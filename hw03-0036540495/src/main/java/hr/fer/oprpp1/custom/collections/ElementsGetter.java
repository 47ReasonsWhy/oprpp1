package hr.fer.oprpp1.custom.collections;

import java.util.NoSuchElementException;

/**
 * Interface that represents a getter for elements in a collection.
 *
 * @param <T> type of elements in the collection
 *
 * @version 2.0
 * @author Marko Šelendić
 */
public interface ElementsGetter<T> {
    /**
     * Checks if there are more elements to be returned.
     *
     * @return true if there are more elements to be returned, false otherwise
     */
    boolean hasNextElement();

    /**
     * Returns the next element in the collection.
     *
     * @return next element in the collection
     * @throws NoSuchElementException if there are no more elements to be returned
     */
    T getNextElement();

    /**
     * Calls the process method of the given processor for each remaining element in the collection.
     *
     * @param p processor whose process method will be called
     */
    default void processRemaining(Processor<? super T> p) {
        while (hasNextElement()) {
            p.process(getNextElement());
        }
    }
}
