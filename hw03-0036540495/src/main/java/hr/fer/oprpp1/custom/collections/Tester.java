package hr.fer.oprpp1.custom.collections;

/**
 * A functional interface which represents a general tester of objects.
 *
 * @param <T> type of object to be tested
 *
 * @version 2.0
 * @author Marko Šelendić
 */
public interface Tester<T> {
    /**
     * Tests if the given object satisfies the condition.
     *
     * @param obj object to be tested
     * @return true if the given object satisfies the condition, false otherwise
     */
    boolean test(T obj);
}
