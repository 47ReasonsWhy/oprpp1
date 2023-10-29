package hr.fer.oprpp1.custom.collections;

/**
 * A functional interface which represents a general processor of objects.
 *
 * @param <T> type of object to be processed
 *
 * @version 2.0
 * @author Marko Šelendić
 */
public interface Processor<T> {
    /**
     * Processes the given value.
     *
     * @param value value to be processed
     */
    void process(T value);
}
