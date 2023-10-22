package hr.fer.oprpp1.hw02.prob1;

/**
 * Enumeration of token types.
 *
 * @see Lexer
 * @see Token
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public enum TokenType {
    /**
     * End of file.
     */
    EOF,

    /**
     * Word.
     */
    WORD,

    /**
     * Number.
     */
    NUMBER,

    /**
     * Symbol (everything that is not a word or a number).
     */
    SYMBOL
}
