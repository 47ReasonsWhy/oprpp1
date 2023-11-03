package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueryFilterTest {
    static IFilter f;
    StudentRecord sr1 = new StudentRecord("0036500000", "Anić", "Ana", 3);
    StudentRecord sr2 = new StudentRecord("0036500001", "Ivić", "Ivan", 4);
    StudentRecord sr3 = new StudentRecord("0036500002", "Anović", "Antiša", 5);

    @BeforeAll
    public static void setUp() {
        ConditionalExpression expr1 = new ConditionalExpression(FieldValueGetters.JMBAG, "0036500000", ComparisonOperators.GREATER_OR_EQUALS);
        ConditionalExpression expr2 = new ConditionalExpression(FieldValueGetters.JMBAG, "0036500002", ComparisonOperators.LESS);
        ConditionalExpression expr3 = new ConditionalExpression(FieldValueGetters.LAST_NAME, "An*", ComparisonOperators.LIKE);
        f = new QueryFilter(List.of(expr1, expr2, expr3));
    }

    @Test
    public void testFilter() {
        assertTrue(f.accepts(sr1));
        assertFalse(f.accepts(sr2));
        assertFalse(f.accepts(sr3));
    }
}
