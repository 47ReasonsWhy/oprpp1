package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDatabaseTest {
    private static List<String> database;

    private final IFilter alwaysTrue = record -> true;
    private final IFilter alwaysFalse = record -> false;


    @BeforeAll
    static void setUp() {
        database = List.of(
                "0036511111\tPetrić\tPetra\t5",
                "0036522222\tPetrović\tPetar\t2",
                "0036533333\tAnić\tAna\t4",
                "0036544444\tAntić\tAnte\t1",
                "0036555555\tMarković\tMarko\t5",
                "0036566666\tMatić\tMatija\t3",
                "0036577777\tKarlović\tKarla\t4",
                "0036588888\tHorvat\tHrvoje\t2",
                "0036599999\tKovač\tKarlo\t1",
                "0036500000\tMarković\tMarija\t3"
        );
    }

    @Test
    void testConstructorInvalidJmbag() {
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(List.of(
                "003651111\tDevet\tZnamenaka\t5"
        )));
    }

    @Test
    void testConstructorSameJmbag() {
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(List.of(
                "0036511111\tDva\tStudenta\t5",
                "0036511111\tIsti\tJmbag\t5"
        )));
    }

    @Test
    void testConstructorInvalidFinalGrade() {
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(List.of(
                "0036511111\tPrvi\tMedjuPrvima\t6"
        )));
        assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(List.of(
                "0036511111\tZadnji\tMedjuZadnjima\t0"
        )));
    }

    @Test
    void testForJmbag() {
        StudentDatabase db = new StudentDatabase(database);
        assertEquals("0036511111", db.forJMBAG("0036511111").jmbag());
        assertEquals("Petrić", db.forJMBAG("0036511111").lastName());
        assertEquals("Petra", db.forJMBAG("0036511111").firstName());
        assertEquals(5, db.forJMBAG("0036511111").finalGrade());
        assertEquals("0036522222", db.forJMBAG("0036522222").jmbag());
        assertEquals("Petrović", db.forJMBAG("0036522222").lastName());
        assertEquals("Petar", db.forJMBAG("0036522222").firstName());
        assertEquals(2, db.forJMBAG("0036522222").finalGrade());
        assertEquals("0036533333", db.forJMBAG("0036533333").jmbag());
        assertEquals("Anić", db.forJMBAG("0036533333").lastName());
        assertEquals("Ana", db.forJMBAG("0036533333").firstName());
        assertEquals(4, db.forJMBAG("0036533333").finalGrade());
        assertEquals("0036544444", db.forJMBAG("0036544444").jmbag());
        assertEquals("Antić", db.forJMBAG("0036544444").lastName());
        assertEquals("Ante", db.forJMBAG("0036544444").firstName());
        assertEquals(1, db.forJMBAG("0036544444").finalGrade());
        assertEquals("0036555555", db.forJMBAG("0036555555").jmbag());
        assertEquals("Marković", db.forJMBAG("0036555555").lastName());
        assertEquals("Marko", db.forJMBAG("0036555555").firstName());
        assertEquals(5, db.forJMBAG("0036555555").finalGrade());
        assertEquals("0036566666", db.forJMBAG("0036566666").jmbag());
        assertEquals("0036577777", db.forJMBAG("0036577777").jmbag());
        assertEquals("0036588888", db.forJMBAG("0036588888").jmbag());
        assertEquals("0036599999", db.forJMBAG("0036599999").jmbag());
        assertEquals("0036500000", db.forJMBAG("0036500000").jmbag());
        assertNull(db.forJMBAG("0000000000"));
    }

    @Test
    void testFilter() {
        StudentDatabase db = new StudentDatabase(database);
        assertEquals(10, db.filter(alwaysTrue).size());
        assertEquals(0, db.filter(alwaysFalse).size());

        assertEquals(1, db.filter(record -> record.jmbag().equals("0036544444")).size());
        assertEquals("0036544444", db.filter(record -> record.jmbag().equals("0036544444")).get(0).jmbag());
        assertEquals("Antić", db.filter(record -> record.jmbag().equals("0036544444")).get(0).lastName());
        assertEquals("Ante", db.filter(record -> record.jmbag().equals("0036544444")).get(0).firstName());
        assertEquals(1, db.filter(record -> record.jmbag().equals("0036544444")).get(0).finalGrade());

        assertEquals(2, db.filter(record -> record.lastName().equals("Marković")).size());
        assertEquals(0, db.filter(record -> record.jmbag().equals("0000000000")).size());
    }
}
