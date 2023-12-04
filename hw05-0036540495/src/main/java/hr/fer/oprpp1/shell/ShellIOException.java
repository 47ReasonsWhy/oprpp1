package hr.fer.oprpp1.shell;

/**
 * Exception thrown when an error occurs while reading from or writing to {@link MyShell}
 *
 * @see MyShell
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class ShellIOException extends RuntimeException {
    /**
     * Constructs an {@code ShellIOException} with a specified detail message.
     */
    public ShellIOException(String message) {
        super(message);
    }
}
