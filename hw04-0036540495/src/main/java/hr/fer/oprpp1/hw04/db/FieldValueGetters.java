package hr.fer.oprpp1.hw04.db;

/**
 * This class contains static final instances of {@link IFieldValueGetter}.
 * <p>
 * It is used to get the value of a field from a {@link StudentRecord}.
 *
 * @see IFieldValueGetter
 * @see StudentRecord
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class FieldValueGetters {
    /**
     * Returns the first name of a {@link StudentRecord}.
     */
    public static final IFieldValueGetter FIRST_NAME = StudentRecord::firstName;

    /**
     * Returns the last name of a {@link StudentRecord}.
     */
    public static final IFieldValueGetter LAST_NAME = StudentRecord::lastName;

    /**
     * Returns the jmbag of a {@link StudentRecord}.
     */
    public static final IFieldValueGetter JMBAG = StudentRecord::jmbag;
}
