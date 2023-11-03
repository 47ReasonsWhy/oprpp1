package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface that represents a comparison operator.
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface IComparisonOperator {

    /**
     * Checks if the given values satisfy the comparison operator.
     *
     * @param value1 first value
     * @param value2 second value
     * @return true if the given values satisfy the comparison operator, false otherwise
     */
    boolean satisfied(String value1, String value2);
}
