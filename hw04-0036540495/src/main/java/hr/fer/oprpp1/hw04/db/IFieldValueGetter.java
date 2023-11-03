package hr.fer.oprpp1.hw04.db;

/**
 * A functional interface that represents a getter for a field value.
 *
 * @see StudentRecord
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface IFieldValueGetter {
    /**
     * Returns a field value of a given record.
     *
     * @param record record whose field value is returned
     * @return field value of a given record
     */
    String get(StudentRecord record);
}
