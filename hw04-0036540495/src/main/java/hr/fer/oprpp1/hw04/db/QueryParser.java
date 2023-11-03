package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that parses the query and stores it in a list of conditional expressions.
 * It also stores the queried JMBAG if the query is direct.
 *
 * @see ConditionalExpression
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class QueryParser {
    /**
     * The query that is being parsed.
     */
    private String query;

    /**
     * Index of the conditional expression that is being parsed.
     */
    private int index;

    /**
     * The list of conditional expressions parsed from the query.
     */
    private final List<ConditionalExpression> conditionalExpressions;

    /**
     * The queried JMBAG if the query is direct.
     */
    private String queriedJMBAG;

    /**
     * Constructs a new query parser that parses the given query.
     *
     * @param query query to be parsed
     */
    public QueryParser(String query) {
        this.query = query;
        this.index = 0;
        conditionalExpressions = new ArrayList<>();
        parseQuery();
        if (conditionalExpressions.size() == 1 &&
            conditionalExpressions.get(0).fieldGetter() == FieldValueGetters.JMBAG &&
            conditionalExpressions.get(0).comparisonOperator() == ComparisonOperators.EQUALS) {
                queriedJMBAG = conditionalExpressions.get(0).stringLiteral();
        }
    }

    /**
     * Checks if the query is direct.
     *
     * @return true if the query is direct, false otherwise
     */
    public boolean isDirectQuery() {
        return queriedJMBAG != null;
    }

    /**
     * Returns the queried JMBAG if the query is direct.
     *
     * @return the queried JMBAG if the query is direct
     * @throws IllegalStateException if the query is not direct
     */
    public String getQueriedJMBAG() {
        if (!isDirectQuery()) {
            throw new IllegalStateException("Query is not direct.");
        }
        return queriedJMBAG;
    }

    /**
     * Returns the list of conditional expressions parsed from the query.
     *
     * @return the list of conditional expressions parsed from the query
     */
    public List<ConditionalExpression> getQuery() {
        return conditionalExpressions;
    }

    /**
     * Parses the query and stores the conditional expressions in a list.
     */
    private void parseQuery() {
        do {
            index++;
            IFieldValueGetter fieldGetter = parseFieldGetter();
            IComparisonOperator comparisonOperator = parseComparisonOperator();
            String stringLiteral = parseStringLiteral();
            conditionalExpressions.add(new ConditionalExpression(fieldGetter, stringLiteral, comparisonOperator));
        } while (moreConditionalExpressions());
    }

    /**
     * Parses the field getter from the query.
     *
     * @return the field getter parsed from the query
     * @throws IllegalArgumentException if the field getter is invalid or missing
     */
    private IFieldValueGetter parseFieldGetter() {
        removeTrailingWhitespace();
        if (query.isEmpty()) {
            throw new IllegalArgumentException("Expression " + index + " is missing a field name.");
        }
        IFieldValueGetter fieldGetter;
        StringBuilder fieldGetterString = new StringBuilder();
        Character c;
        // extract from query until the first non-letter character
        while (!query.isEmpty() && Character.isLetter(c = query.charAt(0))) {
            fieldGetterString.append(c);
            query = query.substring(1);
        }
        // try to parse the fieldGetter and put the rest of the query back
        if (fieldGetterString.toString().startsWith("jmbag")) {
            fieldGetter = FieldValueGetters.JMBAG;
            if (fieldGetterString.toString().length() > 5) {
                query = fieldGetterString.substring(5) + query;
            }
        } else if (fieldGetterString.toString().startsWith("firstName")) {
            fieldGetter = FieldValueGetters.FIRST_NAME;
            if (fieldGetterString.toString().length() > 9) {
                query = fieldGetterString.substring(9) + query;
            }
        } else if (fieldGetterString.toString().startsWith("lastName")) {
            fieldGetter = FieldValueGetters.LAST_NAME;
            if (fieldGetterString.toString().length() > 8) {
                query = fieldGetterString.substring(8) + query;
            }
        } else {
            throw new IllegalArgumentException("Expression " + index + " has an invalid field name: " + fieldGetterString);
        }

        return fieldGetter;
    }

    /**
     * Parses the comparison operator from the query.
     *
     * @return the comparison operator parsed from the query
     * @throws IllegalArgumentException if the comparison operator is invalid or missing
     */
    private IComparisonOperator parseComparisonOperator() {
        removeTrailingWhitespace();
        if (query.isEmpty()) {
            throw new IllegalArgumentException("Expression " + index + " is missing a comparison operator.");
        }
        IComparisonOperator comparisonOperator;
        StringBuilder comparisonOperatorString = new StringBuilder();
        Character c;
        // extract from query while we still may be parsing the comparison operator
        while (!query.isEmpty() && "=<LIKE>!".contains(String.valueOf(c = query.charAt(0)))) {
            comparisonOperatorString.append(c);
            query = query.substring(1);
        }
        // try to parse the comparison operator and put the rest of the query back
        switch (comparisonOperatorString.toString()) {
            case "<" -> {
                comparisonOperator = ComparisonOperators.LESS;
                if (comparisonOperatorString.length() > 1) {
                    query = comparisonOperatorString.substring(1) + query;
                }
            }
            case "<=" -> {
                comparisonOperator = ComparisonOperators.LESS_OR_EQUALS;
                if (comparisonOperatorString.length() > 2) {
                    query = comparisonOperatorString.substring(2) + query;
                }
            }
            case ">" -> {
                comparisonOperator = ComparisonOperators.GREATER;
                if (comparisonOperatorString.length() > 1) {
                    query = comparisonOperatorString.substring(1) + query;
                }
            }
            case ">=" -> {
                comparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;
                if (comparisonOperatorString.length() > 2) {
                    query = comparisonOperatorString.substring(2) + query;
                }
            }
            case "=" -> {
                comparisonOperator = ComparisonOperators.EQUALS;
                if (comparisonOperatorString.length() > 1) {
                    query = comparisonOperatorString.substring(1) + query;
                }
            }
            case "!=" -> {
                comparisonOperator = ComparisonOperators.NOT_EQUALS;
                if (comparisonOperatorString.length() > 2) {
                    query = comparisonOperatorString.substring(2) + query;
                }
            }
            case "LIKE" -> {
                comparisonOperator = ComparisonOperators.LIKE;
                if (comparisonOperatorString.length() > 4) {
                    query = comparisonOperatorString.substring(4) + query;
                }
            }
            default -> throw new IllegalArgumentException("Expression " + index + " has an invalid comparison operator.");
        }

        return comparisonOperator;
    }

    /**
     * Parses the string literal from the query.
     *
     * @return the string literal parsed from the query
     * @throws IllegalArgumentException if the string literal is invalid or missing
     */
    private String parseStringLiteral() {
        removeTrailingWhitespace();
        if (query.isEmpty()) {
            throw new IllegalArgumentException("Missing string literal at index " + index + ".");
        }
        StringBuilder stringLiteral = new StringBuilder();
        Character c;
        // check for the beginning of the string literal
        if (query.charAt(0) != '"') {
            throw new IllegalArgumentException("Expression " + index + " is missing a string literal.");
        }
        query = query.substring(1);
        // extract from query until the end of the string literal
        while (!query.isEmpty() && (c = query.charAt(0)) != '"') {
            stringLiteral.append(c);
            query = query.substring(1);
        }
        // check for the end of the string literal
        if (query.isEmpty()) {
            throw new IllegalArgumentException("Expression " + index + " is missing an enclosing quotation mark.");
        }
        query = query.substring(1);

        return stringLiteral.toString();
    }

    /**
     * Checks if there are more conditional expressions in the query.
     *
     * @return true if there are more conditional expressions in the query, false otherwise
     * @throws IllegalArgumentException if the query is invalid
     */
    private Boolean moreConditionalExpressions() {
        // if the query is empty, there are no more conditional expressions
        if (query.isEmpty()) {
            return false;
        }
        // if it is not, at least one space must follow
        if (!Character.isWhitespace(query.charAt(0))) {
            throw new IllegalArgumentException("Expression " + index + " must be the last one or followed by operator AND." +
                    " Additionally, there must be (at least) a space before and after the operator AND.");
        }
        // remove all other whitespace characters
        removeTrailingWhitespace();
        // again, if the query is empty, there are no more conditional expressions
        if (query.isEmpty()) {
            return false;
        }
        // else, check for the beginning of the next conditional expression
        if ("Aa".contains(String.valueOf(query.charAt(0)))) {
            query = query.substring(1);
        } else {
            throw new IllegalArgumentException("Expression " + index + " must be the last one or followed by operator AND.");
        }
        if (!query.isEmpty() && "Nn".contains(String.valueOf(query.charAt(0)))) {
            query = query.substring(1);
        } else {
            throw new IllegalArgumentException("Expression " + index + " must be the last one or followed by operator AND.");
        }
        if (!query.isEmpty() && "Dd".contains(String.valueOf(query.charAt(0)))) {
            query = query.substring(1);
        } else {
            throw new IllegalArgumentException("Expression " + index + " must be the last one or followed by operator AND.");
        }
        // must be followed by a space
        if (!query.isEmpty() && !Character.isWhitespace(query.charAt(0))) {
            throw new IllegalArgumentException("Expression " + index + " must be the last one or followed by operator AND." +
                    " Additionally, there must be (at least) a space before and after the operator AND.");
        }
        // remove all other whitespace characters
        removeTrailingWhitespace();
        return true;
    }

    /**
     * Removes all whitespace characters from the beginning of the query.
     */
    private void removeTrailingWhitespace() {
        // remove all whitespace characters from the beginning of the query
        while (!query.isEmpty() && Character.isWhitespace(query.charAt(0))) {
            query = query.substring(1);
        }
    }
}
