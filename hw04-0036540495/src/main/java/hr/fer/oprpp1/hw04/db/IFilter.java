package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface that represents a filter for a {@link StudentRecord}.
 *
 * @see StudentRecord
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface IFilter {
    /**
     * Checks if the given record satisfies the filter.
     *
     * @param record record to be checked
     * @return true if the record satisfies the filter, false otherwise
     */
    boolean accepts(StudentRecord record);
}
