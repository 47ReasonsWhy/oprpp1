package hr.fer.oprpp1.shell.commands.util;

import hr.fer.oprpp1.shell.Environment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Utility class for parsing and checking file paths.
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Util {
    /**
     * Parses the given arguments into an array of file paths.
     * <p>
     * Arguments are separated by spaces. If a file path contains spaces, it must be surrounded by double quotes.
     * Double quotes can be escaped with a backslash. Backslashes can be escaped with another backslash.
     * <p>
     * Examples:
     * <ul>
     *     <li>file.txt</li>
     *     <li>file.txt file2.txt</li>
     *     <li>file.txt "file 2.txt"</li>
     * </ul>
     * <p>
     * If the double quotes are used, they must contain the whole argument,
     * i.e. the closing double quote must be followed by no characters or by a space.
     * <p>
     * The following is invalid:
     * <ul>
     *     <li>"file 1".txt</li>
     * </ul>
     *
     * @param arguments arguments to parse
     * @return array of arguments
     * @throws IllegalArgumentException if the given arguments are invalid
     */
    public static String[] parseFilePathArguments(String arguments) throws IllegalArgumentException {
        ArrayList<String> args = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < arguments.length(); i++) {
            char c = arguments.charAt(i);
            if (c == '"') {
                if (inQuotes) {
                    args.add(sb.toString());
                    sb = new StringBuilder();
                    if (i + 1 < arguments.length() && arguments.charAt(i + 1) != ' ') {
                        throw new IllegalArgumentException("Closing double quotes must be followed by no characters or by a space.");
                    }
                }
                inQuotes = !inQuotes;
            } else if (c == ' ') {
                if (inQuotes) {
                    sb.append(c);
                } else {
                    if (!sb.isEmpty()) {
                        args.add(sb.toString());
                        sb = new StringBuilder();
                    }
                }
            } else if (c == '\\') {
                if (!inQuotes) {
                    sb.append(c);
                    continue;
                }
                if (i + 1 < arguments.length()) {
                    char next = arguments.charAt(i + 1);
                    if (next == '"' || next == '\\') {
                        sb.append(next);
                        i++;
                    } else {
                        sb.append(c);
                    }
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        if (!sb.isEmpty()) {
            args.add(sb.toString());
        }
        if (inQuotes) {
            throw new IllegalArgumentException("Invalid number of double quotes.");
        }
        return args.toArray(new String[0]);
    }

    /**
     * Checks if the given file path is readable.
     *
     * @param file file path to check
     * @param env environment of the shell that called this method
     * @return true if the file path is readable, false otherwise
     * @throws SecurityException if a security manager exists and its checkRead method denies read access to the file
     */
    public static boolean isReadable(Path file, Environment env) throws SecurityException {
        if (!Files.exists(file)) {
            env.writeln("File \"" + file + "\" does not exist.");
            return false;
        }
        if (!Files.isRegularFile(file)) {
            env.writeln("File \"" + file + "\" is not a regular file.");
            return false;
        }
        if (!Files.isReadable(file)) {
            env.writeln("File \"" + file + "\" is not readable.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given file path is a directory.
     *
     * @param file file path to check
     * @param env environment of the shell that called this method
     * @return true if the file path is a directory, false otherwise
     * @throws SecurityException if a security manager exists and its checkRead method denies read access to the file
     */
    public static boolean isDirectory(Path file, Environment env) throws SecurityException {
        if (!Files.exists(file)) {
            env.writeln("File \"" + file + "\" does not exist.");
            return false;
        }
        if (!Files.isDirectory(file)) {
            env.writeln("File \"" + file + "\" is not a directory.");
            return false;
        }
        return true;
    }
}
