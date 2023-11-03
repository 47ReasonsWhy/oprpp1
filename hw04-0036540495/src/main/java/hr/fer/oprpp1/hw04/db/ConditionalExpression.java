package hr.fer.oprpp1.hw04.db;

/**
 * A record that represents a conditional expression in a query.
 * <p>
 * It has three properties: fieldGetter, stringLiteral and comparisonOperator.
 *
 * @see IFieldValueGetter
 * @see IComparisonOperator
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public record ConditionalExpression(IFieldValueGetter fieldGetter,
                                    String stringLiteral,
                                    IComparisonOperator comparisonOperator) {

}
