package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * A class that filters student records based on a list of conditional expressions.
 * .
 * @see IFilter
 * @see ConditionalExpression
 * @see StudentRecord
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class QueryFilter implements IFilter {
    /**
     * A list of conditional expressions that will be used to filter student records.
     */
    private final List<ConditionalExpression> expressions;

    /**
     * Constructs a new query filter with the given list of conditional expressions.
     *
     * @param expressions list of conditional expressions
     */
    public QueryFilter(List<ConditionalExpression> expressions) {
        this.expressions = expressions;
    }

    /**
     * Returns the list of conditional expressions.
     *
     * @return list of conditional expressions
     */
    public List<ConditionalExpression> getExpressions() {
        return expressions;
    }

    /**
     * Returns true if the given student record satisfies all conditional expressions in the list.
     *
     * @param record student record to be checked
     * @return true if the given student record object satisfies all conditional expressions in the list
     */
    @Override
    public boolean accepts(StudentRecord record) {
        for (ConditionalExpression expression : expressions) {
            if (!expression.comparisonOperator().satisfied(
                expression.fieldGetter().get(record),
                expression.stringLiteral()
            )) {
                    return false;
            }
        }
        return true;
    }
}
