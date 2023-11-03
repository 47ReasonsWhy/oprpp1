package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that represents a database of student records.
 * The records are indexed by JMBAG.
 *
 * @see StudentRecord
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class StudentDatabase {
    /**
     * A list of all student records.
     */
    private final List<StudentRecord> students;

    /**
     * A map of all student records indexed by JMBAG.
     */
    private final Map<String, StudentRecord> studentsIndexedByJMBAG;

    /**
     * Constructs a new student database from the given list of strings.
     * <p>
     * Each string in the given list represents a single student record.
     * The string is expected to be in the following format:
     * <p>
     * JMBAG\tlastName\tfirstName\tfinalGrade
     * <p>
     * JMBAG is a unique 10-digit string.
     * lastName and firstName are strings of variable length.
     * finalGrade is an integer between 1 and 5.
     *
     * @param lines a list of strings to be parsed into student records
     * @throws NullPointerException if the given list of strings is {@code null}
     * @throws IllegalArgumentException if the given list of strings is invalid
     */
    public StudentDatabase(List<String> lines) {
        if (lines == null) {
            throw new NullPointerException("Lines cannot be null.");
        }
        students = new ArrayList<>();
        studentsIndexedByJMBAG = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split("\\t");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid line (" + line + "). Expected 4 parts, got " + parts.length + ".");
            }
            String jmbag = parts[0];
            if (jmbag.length() != 10) {
                throw new IllegalArgumentException("Invalid JMBAG (" + jmbag + ") in line " + line + ".");
            }
            if (studentsIndexedByJMBAG.containsKey(jmbag)) {
                throw new IllegalArgumentException("Duplicate JMBAG (" + jmbag + ") in line " + line + ".");
            }
            String lastName = parts[1];
            String firstName = parts[2];
            int finalGrade;
            try {
                finalGrade = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid final grade (" + parts[3] + ") in line " + line + ".");
            }
            if (finalGrade < 1 || finalGrade > 5) {
                throw new IllegalArgumentException("Invalid final grade (" + finalGrade + ") in line: " + line + ".");
            }
            StudentRecord student = new StudentRecord(jmbag, lastName, firstName, finalGrade);
            students.add(student);
            studentsIndexedByJMBAG.put(jmbag, student);
        }
    }

    /**
     * Returns the student record with the given JMBAG or {@code null} if no such record exists.
     * The method uses the proper indexing.
     *
     * @param jmbag JMBAG of the student record to be returned
     * @return the student record with the given JMBAG
     */
    public StudentRecord forJMBAG(String jmbag) {
        return studentsIndexedByJMBAG.get(jmbag);
    }

    /**
     * Returns a list of all student records that satisfy the given filter.
     * The method keeps the order of the records as they were in the original list.
     *
     * @param filter filter to be used
     * @return list of all student records that satisfy the given filter
     */
    public List<StudentRecord> filter(IFilter filter) {
        List<StudentRecord> filtered = new ArrayList<>();
        for (StudentRecord student : students) {
            if (filter.accepts(student)) {
                filtered.add(student);
            }
        }
        return filtered;
    }
}
