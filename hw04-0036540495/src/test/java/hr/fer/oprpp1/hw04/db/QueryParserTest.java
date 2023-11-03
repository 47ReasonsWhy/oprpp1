package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {
    @Test
    public void testIsDirectQuery() {
        QueryParser parser = new QueryParser("jmbag = \"0036500000\"");
        assertTrue(parser.isDirectQuery());
        assertEquals("0036500000", parser.getQueriedJMBAG());
        assertEquals(1, parser.getQuery().size());
        assertEquals(FieldValueGetters.JMBAG, parser.getQuery().get(0).fieldGetter());
        assertEquals("0036500000", parser.getQuery().get(0).stringLiteral());
        assertEquals(ComparisonOperators.EQUALS, parser.getQuery().get(0).comparisonOperator());
    }

    @Test
    public void testIsNotDirectQuery() {
        QueryParser parser = new QueryParser("jmbag = \"0036500000\" and lastName LIKE \"B*\"");
        assertFalse(parser.isDirectQuery());
        assertThrows(IllegalStateException.class, parser::getQueriedJMBAG);
        assertEquals(2, parser.getQuery().size());
        assertEquals(FieldValueGetters.JMBAG, parser.getQuery().get(0).fieldGetter());
        assertEquals("0036500000", parser.getQuery().get(0).stringLiteral());
        assertEquals(ComparisonOperators.EQUALS, parser.getQuery().get(0).comparisonOperator());
        assertEquals(FieldValueGetters.LAST_NAME, parser.getQuery().get(1).fieldGetter());
        assertEquals("B*", parser.getQuery().get(1).stringLiteral());
        assertEquals(ComparisonOperators.LIKE, parser.getQuery().get(1).comparisonOperator());
    }

    @Test
    public void testInvalidFieldName() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbg = \"0036500000\""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("JMBAG = \"0036500000\""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" and lastNme LIKE \"B*\""));
    }

    @Test
    public void testInvalidOperator() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag /= \"0036500000\""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" and lastName LKE \"B*\""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("lastName likE \"B*\""));
    }

    @Test
    public void testInvalidStringLiteral() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = 0036500000"));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" and lastName LIKE \"B*"));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("lastName LIKE B*"));
    }

    @Test
    public void testInvalidAnd() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\"and lastName LIKE \"B*\""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" andlastName LIKE \"B*\""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" or lastName LIKE \"B*\""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" annd lastName LIKE \"B*\""));
    }

    @Test
    public void testMissingFieldName() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser(""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" and"));
    }

    @Test
    public void testMissingOperator() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag \"0036500000\" and lastName LIKE \"B*\""));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag \"0036500000\" and lastName LIKE \"B*\""));
    }

    @Test
    public void testMissingStringLiteral() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" and lastName LIKE"));
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" and lastName LIKE  "));
    }

    @Test
    public void testMissingAnd() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmbag = \"0036500000\" lastName LIKE \"B*\""));
    }

    @Test
    public void testSomeValidQueries() {
        assertDoesNotThrow(() -> new QueryParser("  jmbag   =   \"0036500000\"   "));
        assertDoesNotThrow(() -> new QueryParser("jmbag=\"0036500000\""));
        assertDoesNotThrow(() -> new QueryParser("lastNameLIKE\"B*\""));
        assertDoesNotThrow(() -> new QueryParser("jmbag = \"0036500000\" aNd lastName LIKE \"B*\""));
        assertDoesNotThrow(() -> new QueryParser("jmbag=\"\" aNd jmbag=\"\" ANd jmbag=\"\" AND jmbag=\"\" anD jmbag=\"\""));
    }

    @Test
    public void testExamplesFromHomework() {
        assertDoesNotThrow(() -> new QueryParser("jmbag=\"0000000003\""));
        assertDoesNotThrow(() -> new QueryParser("lastName = \"Blažić\""));
        assertDoesNotThrow(() -> new QueryParser("firstName>\"A\" and lastName LIKE \"B*ć\""));
        assertDoesNotThrow(() -> new QueryParser("firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\""));
    }
}
