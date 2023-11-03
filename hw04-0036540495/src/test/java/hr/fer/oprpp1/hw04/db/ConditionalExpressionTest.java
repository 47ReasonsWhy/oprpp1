package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionalExpressionTest {
    private static final StudentRecord record = new StudentRecord("0036500000", "Anić", "Ana", 2);

    @Test
    void testConditionalExpression() {
        ConditionalExpression expression = new ConditionalExpression(FieldValueGetters.LAST_NAME, "An*", ComparisonOperators.LIKE);

        assertEquals(FieldValueGetters.LAST_NAME, expression.fieldGetter());
        assertEquals("An*", expression.stringLiteral());
        assertEquals(ComparisonOperators.LIKE, expression.comparisonOperator());
    }

    @Test
    void testConditionalExpressionUsage() {
        ConditionalExpression expression1 = new ConditionalExpression(FieldValueGetters.LAST_NAME, "An*", ComparisonOperators.LIKE);
        ConditionalExpression expression2 = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "An*", ComparisonOperators.LIKE);
        ConditionalExpression expression3 = new ConditionalExpression(FieldValueGetters.JMBAG, "0036500000", ComparisonOperators.EQUALS);
        ConditionalExpression expression4 = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Ivić", ComparisonOperators.LESS_OR_EQUALS);
        ConditionalExpression expression5 = new ConditionalExpression(FieldValueGetters.FIRST_NAME, "Ivić", ComparisonOperators.GREATER_OR_EQUALS);

        assertTrue(expression1.comparisonOperator().satisfied(expression1.fieldGetter().get(record), expression1.stringLiteral()));
        assertTrue(expression2.comparisonOperator().satisfied(expression2.fieldGetter().get(record), expression2.stringLiteral()));
        assertTrue(expression3.comparisonOperator().satisfied(expression3.fieldGetter().get(record), expression3.stringLiteral()));
        assertTrue(expression4.comparisonOperator().satisfied(expression4.fieldGetter().get(record), expression4.stringLiteral()));
        assertFalse(expression5.comparisonOperator().satisfied(expression5.fieldGetter().get(record), expression5.stringLiteral()));
    }
}
