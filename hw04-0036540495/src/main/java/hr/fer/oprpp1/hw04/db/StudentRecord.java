package hr.fer.oprpp1.hw04.db;

/**
 * A record of a student.
 * <p>
 * It consists of the student's JMBAG, last name, first name and final grade.
 * <p>
 * The JMBAG is a unique identifier of a student.
 * The last name, first name and final grade are not unique.
 * <p>
 * The JMBAG is a string of 10 digits.
 * The last name and first name are strings of variable length.
 * The final grade is an integer between 1 and 5.
 *
 * @see StudentDatabase
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public record StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
    public StudentRecord {
        if (jmbag == null || jmbag.length() != 10) {
            throw new IllegalArgumentException("JMBAG must be a string of 10 digits.");
        }
        if (finalGrade < 1 || finalGrade > 5) {
            throw new IllegalArgumentException("Final grade must be an integer between 1 and 5.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StudentRecord other)) return false;
        return this.jmbag.equals(other.jmbag);
    }

    @Override
    public int hashCode() {
        return this.jmbag.hashCode();
    }

    @Override
    public String toString() {
        return jmbag + " : " + lastName + " " + firstName + " : " + finalGrade;
    }
}
