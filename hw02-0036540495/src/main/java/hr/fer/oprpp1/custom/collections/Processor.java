package hr.fer.oprpp1.custom.collections;

/**
 * A functional interface which represents a general processor of objects.
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface Processor {
    /**
     * Processes the given value.
     *
     * @param value value to be processed
     */
    void process(Object value);
}
