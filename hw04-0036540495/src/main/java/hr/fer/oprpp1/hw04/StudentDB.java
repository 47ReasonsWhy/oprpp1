package hr.fer.oprpp1.hw04;

import hr.fer.oprpp1.hw04.db.QueryFilter;
import hr.fer.oprpp1.hw04.db.QueryParser;
import hr.fer.oprpp1.hw04.db.StudentDatabase;
import hr.fer.oprpp1.hw04.db.StudentRecord;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * A simple console program that allows the user to query a database of student records.
 * The database is read from a file named "database.txt" located in the resources folder.
 * The program supports the following commands:
 * <ul>
 *     <li>query followed by conditional expressions</li>
 *     <li>exit</li>
 * </ul>
 * The program will print the results of the query in a formatted table.
 * <p>
 * The program will continue to read commands from the standard input until the user enters the "exit" command.
 * <p>
 *
 * @see StudentRecord
 * @see StudentDatabase
 * @see QueryParser
 * @see QueryFilter
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class StudentDB {
    public static void main(String[] args) {
        System.out.println("Welcome to StudentDB!");
        System.out.println("Type \"query\" followed by conditional expressions to start querying the database.");
        System.out.println("Type \"exit\" to exit the program.");
        System.out.print("> ");

        ClassLoader classLoader = StudentDB.class.getClassLoader();
        File file;
        List<String> databaseLines = null;
        try {
            file = new File(Objects.requireNonNull(classLoader.getResource("database.txt")).getFile());
            databaseLines = Files.readAllLines(file.toPath());
        } catch (Exception e) {
            System.err.println("Error while reading the database file.");
            System.err.println(e.getMessage());
        }

        StudentDatabase database = new StudentDatabase(Objects.requireNonNull(databaseLines));

        while (true) {
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine().trim();
            if (input.equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            if (!input.startsWith("query")) {
                System.out.println("Invalid command: " + input.split("\\s+")[0] + ". Expected \"query\" or \"exit\".");
                System.out.print("> ");
                continue;
            }
            if (input.length() == 5) {
                System.out.println("Invalid number of conditional expressions after \"query\".");
                System.out.print("> ");
                continue;
            }
            input = input.substring(5).trim();
            if (input.isEmpty()) {
                System.out.println("Invalid command. Expected conditional expressions after \"query\".");
                System.out.print("> ");
                continue;
            }
            QueryParser parser;
            try {
                parser = new QueryParser(input);
            } catch (Exception e) {
                System.out.println("Invalid query. " + e.getMessage());
                System.out.print("> ");
                continue;
            }

            List<StudentRecord> filtered;
             try {
                 filtered = filterStudentRecords(parser, database);
             } catch (Exception e) {
                 System.out.println("Error while filtering student records: " + e.getMessage());
                 System.out.print("> ");
                 continue;
             }

            printStudentRecordsFormatted(filtered);
        }
    }

    /**
     * Filters student records based on the given query parser and database.
     *
     * @param parser   query parser
     * @param database student database
     * @return list of filtered student records
     */
    private static List<StudentRecord> filterStudentRecords(QueryParser parser, StudentDatabase database) {
        List<StudentRecord> filtered;
        if (parser.isDirectQuery()) {
            StudentRecord record = database.forJMBAG(parser.getQueriedJMBAG());
            if (record == null) {
                filtered = List.of();
            } else {
                filtered = List.of(record);
            }
            System.out.println("Using index for record retrieval.");
        } else {
            filtered = database.filter(new QueryFilter(parser.getQuery()));
        }
        return filtered;
    }

    /**
     * Prints the given list of student records in a formatted table.
     *
     * @param filtered list of student records
     */
    private static void printStudentRecordsFormatted(List<StudentRecord> filtered) {
        if (filtered.isEmpty()) {
            System.out.println("Records selected: 0");
            System.out.print("> ");
            return;
        }

        int lastNameMaxLength = 0;
        int firstNameMaxLength = 0;
        for (StudentRecord student : filtered) {
            if (student.lastName().length() > lastNameMaxLength) {
                lastNameMaxLength = student.lastName().length();
            }
            if (student.firstName().length() > firstNameMaxLength) {
                firstNameMaxLength = student.firstName().length();
            }
        }

        String border =
                "+============+" +
                "=".repeat(lastNameMaxLength + 2) +
                "+" + "=".repeat(firstNameMaxLength + 2) +
                "+===+";

        System.out.println(border);
        for (StudentRecord student : filtered) {
            System.out.printf("| %s | %-" + lastNameMaxLength + "s | %-" + firstNameMaxLength + "s | %d |\n",
                    student.jmbag(),
                    student.lastName(),
                    student.firstName(),
                    student.finalGrade());
        }
        System.out.println(border);
        System.out.printf("Records selected: %d\n", filtered.size());
        System.out.print("> ");
    }
}
