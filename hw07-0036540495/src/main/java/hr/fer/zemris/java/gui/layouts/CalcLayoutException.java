package hr.fer.zemris.java.gui.layouts;

/**
 * Exception thrown when an error occurs in {@link CalcLayout}.
 *
 * @see CalcLayout
 * @see RuntimeException
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class CalcLayoutException extends RuntimeException {
    /**
     * Constructs an {@code CalcLayoutException} with a given message.
     *
     * @param message error message
     */
    public CalcLayoutException(String message) {
        super(message);
    }
}
