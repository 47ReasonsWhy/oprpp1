package hr.fer.oprpp1.hw04.db;

/**
 * Class that contains all comparison operators.
 * <p>
 * Comparison operators are:
 * <ul>
 *     <li>LESS</li>
 *     <li>LESS_OR_EQUALS</li>
 *     <li>GREATER</li>
 *     <li>GREATER_OR_EQUALS</li>
 *     <li>EQUALS</li>
 *     <li>NOT_EQUALS</li>
 *     <li>LIKE</li>
 * </ul>
 *
 * @see IComparisonOperator
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class ComparisonOperators {
    /**
     * Operator that checks if value1 is less than value2. (value1 < value2)
     */
    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;

    /**
     * Operator that checks if value1 is less than or equal to value2. (value1 <= value2)
     */
    public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;

    /**
     * Operator that checks if value1 is greater than value2. (value1 > value2)
     */
    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;

    /**
     * Operator that checks if value1 is greater than or equal to value2. (value1 >= value2)
     */
    public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;

    /**
     * Operator that checks if value1 is equal to value2. (value1 == value2)
     */
    public static final IComparisonOperator EQUALS = (value1, value2) -> value1.compareTo(value2) == 0;

    /**
     * Operator that checks if value1 is not equal to value2. (value1 != value2)
     */
    public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> value1.compareTo(value2) != 0;

    /**
     * Operator that checks if value1 is like value2.
     * <p>
     * Value2 can contain wildcard character '*',
     * which can be used to replace any number of characters.
     * <p>
     * Examples:
     * <ul>
     *     <li>LIKE("AA", "AA") returns true</li>
     *     <li>LIKE("AAA", "AA*") returns true</li>
     *     <li>LIKE("AAAA", "AA*AA") returns true</li>
     *     <li>LIKE("AA", "AA*AA") returns false</li>
     * </ul>
     */
    public static final IComparisonOperator LIKE = (value1, value2) -> {
        if (value2.indexOf('*') != value2.lastIndexOf('*')) {
            throw new IllegalArgumentException("Invalid number of wildcards (more than one).");
        }
        if (value2.contains("*")) {
            String[] parts = value2.split("\\*");
            return switch (parts.length) {
                // the wildcard is in the middle of the string
                case 2 -> value1.startsWith(parts[0]) && value1.endsWith(parts[1]) &&
                          value1.length() >= parts[0].length() + parts[1].length();
                // the wildcard is at the beginning or at the end of the string
                case 1 -> {
                    if (value2.startsWith("*")) {
                        yield value1.endsWith(parts[0]);
                    } else {
                        yield value1.startsWith(parts[0]);
                    }
                }
                // the wildcard is the only character in the string
                case 0 -> true;
                // this case should never happen
                default -> throw new IllegalArgumentException("Invalid number of wildcards (more than one).");
            };
        } else {
            return value1.equals(value2);
        }
    };
}
