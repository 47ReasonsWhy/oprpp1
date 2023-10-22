package hr.fer.oprpp1.hw02.prob1;

/**
 * Exception thrown by the {@link Lexer} when an error occurs.
 *
 * @see Lexer
 * @see RuntimeException
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class LexerException extends RuntimeException {
    /**
     * Creates an instance of LexerException.
     * @param message message of the exception
     */
    public LexerException(String message) {
            super(message);
        }
}
