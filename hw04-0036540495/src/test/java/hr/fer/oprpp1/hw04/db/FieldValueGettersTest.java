package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldValueGettersTest {
    private static StudentRecord ana;
    private static StudentRecord iva;

    @BeforeAll
    static void setUp() {
        ana = new StudentRecord("0036512121", "Anić", "Ana", 2);
        iva = new StudentRecord("0036534343", "Ivić", "Iva", 3);
    }

    @Test
    void testLastName() {
        assertEquals("Anić", FieldValueGetters.LAST_NAME.get(ana));
        assertEquals("Ivić", FieldValueGetters.LAST_NAME.get(iva));
    }

    @Test
    void testFirstName() {
        assertEquals("Ana", FieldValueGetters.FIRST_NAME.get(ana));
        assertEquals("Iva", FieldValueGetters.FIRST_NAME.get(iva));
    }

    @Test
    void testJmbag() {
        assertEquals("0036512121", FieldValueGetters.JMBAG.get(ana));
        assertEquals("0036534343", FieldValueGetters.JMBAG.get(iva));
    }
}
