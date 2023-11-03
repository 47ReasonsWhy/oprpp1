package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonOperatorsTest {
    @Test
    void testLess() {
        IComparisonOperator operator = ComparisonOperators.LESS;

        assertTrue(operator.satisfied("Ana", "Jasna"));
        assertFalse(operator.satisfied("Jasna", "Ana"));
        assertFalse(operator.satisfied("Ana", "Ana"));
    }

    @Test
    void testLessOrEquals() {
        IComparisonOperator operator = ComparisonOperators.LESS_OR_EQUALS;

        assertTrue(operator.satisfied("Ana", "Jasna"));
        assertFalse(operator.satisfied("Jasna", "Ana"));
        assertTrue(operator.satisfied("Ana", "Ana"));
    }

    @Test
    void testGreater() {
        IComparisonOperator operator = ComparisonOperators.GREATER;

        assertFalse(operator.satisfied("Ana", "Jasna"));
        assertTrue(operator.satisfied("Jasna", "Ana"));
        assertFalse(operator.satisfied("Ana", "Ana"));
    }

    @Test
    void testGreaterOrEquals() {
        IComparisonOperator operator = ComparisonOperators.GREATER_OR_EQUALS;

        assertFalse(operator.satisfied("Ana", "Jasna"));
        assertTrue(operator.satisfied("Jasna", "Ana"));
        assertTrue(operator.satisfied("Ana", "Ana"));
    }

    @Test
    void testEquals() {
        IComparisonOperator operator = ComparisonOperators.EQUALS;

        assertFalse(operator.satisfied("Ana", "Jasna"));
        assertFalse(operator.satisfied("Jasna", "Ana"));
        assertTrue(operator.satisfied("Ana", "Ana"));
    }

    @Test
    void testNotEquals() {
        IComparisonOperator operator = ComparisonOperators.NOT_EQUALS;

        assertTrue(operator.satisfied("Ana", "Jasna"));
        assertTrue(operator.satisfied("Jasna", "Ana"));
        assertFalse(operator.satisfied("Ana", "Ana"));
    }

    @Test
    void testLike() {
        IComparisonOperator operator = ComparisonOperators.LIKE;

        assertFalse(operator.satisfied("Zagreb", "Aba*"));
        assertFalse(operator.satisfied("AAA", "AA*AA"));
        assertTrue(operator.satisfied("AAAA", "AA*AA"));
        assertTrue(operator.satisfied("AAAA", "*AA"));
        assertTrue(operator.satisfied("AAAA", "AA*"));
        assertTrue(operator.satisfied("AAAA", "AAAA*"));
        assertTrue(operator.satisfied("AAAA", "AAAA"));
        assertFalse(operator.satisfied("AAAA", "AAA"));
        assertFalse(operator.satisfied("AAAA", "AA"));
        assertFalse(operator.satisfied("AAAA", "A"));
        assertFalse(operator.satisfied("AAAA", ""));
        assertTrue(operator.satisfied("AAAA", "*"));
        assertTrue(operator.satisfied("", ""));
        assertTrue(operator.satisfied("", "*"));
        assertTrue(operator.satisfied(" ", "*"));
        assertTrue(operator.satisfied(" ", " *"));
        assertTrue(operator.satisfied(" ", "* "));
        assertFalse(operator.satisfied(" ", " * "));
        assertTrue(operator.satisfied("  ", " * "));
        assertThrows(IllegalArgumentException.class, () -> operator.satisfied("", "**"));
        assertThrows(IllegalArgumentException.class, () -> operator.satisfied("", "* *"));
        assertThrows(IllegalArgumentException.class, () -> operator.satisfied("", "  **"));
    }
}
