package hr.fer.oprpp1.hw02.prob1;

/**
 * A lexer that generates tokens from the given input text.
 * The lexer can be in two states: basic and extended.
 * In the basic state, the lexer generates words, numbers and symbols,
 * and the numbers can be escaped with a backslash.
 * After it generates the '#' symbol, it should be switched to the extended state,
 * in which the lexer only generates words.
 * After it generates the '#' symbol again, it should be switched back to the basic state.
 * The lexer can be switched to the extended state by calling the {@link Lexer#setState(LexerState)} method.
 *
 * @see LexerState
 * @see Token
 * @see TokenType
 * @see LexerException
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Lexer {
    /**
     * Input text.
     */
    private final char[] data;

    /**
     * Current token.
     */
    private Token token;

    /**
     * Index of the first unprocessed character.
     */
    private int currentIndex;

    /**
     * Current state of the lexer.
     */
    private LexerState state;

    /**
     * Creates a new lexer with the given text as input.
     *
     * @param text input text
     * @throws NullPointerException if the given text is null
     */
    public Lexer(String text) {
        if (text == null) {
            throw new NullPointerException("Input text cannot be null.");
        }
        this.data = text.toCharArray();
        this.token = null;
        this.currentIndex = 0;
        this.state = LexerState.BASIC;
    }

    /**
     * Returns the last token that was generated.
     * Will not generate the next token even if called multiple times.
     *
     * @return last generated token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Generates and returns the next token.
     *
     * @return next token
     * @throws LexerException if an error occurs
     */
    public Token nextToken() {
        // If we already generated the EOF token, there should be no more tokens available.
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("No more tokens available.");
        }

        // Else, first skip all whitespaces
        while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
            currentIndex++;
        }

        // If we reached the end of the input, generate the EOF token.
        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, null);
            return token;
        }

        // Else, generate the next token.
        // If the Lexer is in the extended state, the next token can only be a word and there is no escaping characters.
        // The only exception is the '#' symbol.
        if (state == LexerState.EXTENDED) {
            if (data[currentIndex] == '#') {
                token = new Token(TokenType.SYMBOL, data[currentIndex]);
                currentIndex++;
                return token;
            }
            StringBuilder word = new StringBuilder();
            while (currentIndex < data.length && !Character.isWhitespace(data[currentIndex]) && data[currentIndex] != '#') {
                word.append(data[currentIndex]);
                currentIndex++;
            }
            token = new Token(TokenType.WORD, word.toString());
            return token;
        }


        // Else, the Lexer is in the basic state.
        // If the current character is a letter or a backslash, the next token is a word.
        if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
            StringBuilder word = new StringBuilder();
            // while we have letters or backslashes, append the letters or the escaped characters to the word
            while (currentIndex < data.length && (
                    Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\'
            )) {
                if (data[currentIndex] == '\\') {
                    currentIndex++;
                    // we only accept a digit or another backslash after the initial backslash
                    if (currentIndex >= data.length || (
                        !Character.isDigit(data[currentIndex]) &&
                        data[currentIndex] != '\\')
                    ) {
                            throw new LexerException("Invalid escape sequence.");
                    }
                }
                word.append(data[currentIndex]);
                currentIndex++;
            }
            token = new Token(TokenType.WORD, word.toString());
            return token;
        }

        // If the current character is a digit, the next token is a number.
        if (Character.isDigit(data[currentIndex])) {
            StringBuilder number = new StringBuilder();
            while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
                number.append(data[currentIndex]);
                currentIndex++;
            }
            // check if the number can be cast to a long
            try {
                Long.parseLong(number.toString());
            } catch (NumberFormatException e) {
                throw new LexerException("Number too large to be cast to a long.");
            }
            token = new Token(TokenType.NUMBER, Long.parseLong(number.toString()));
            return token;
        }

        // Else, the next token is a symbol.
        token = new Token(TokenType.SYMBOL, data[currentIndex]);
        currentIndex++;
        return token;
    }

    /**
     * Sets the new state of the lexer.
     *
     * @param state new state
     * @throws NullPointerException if the given state is null
     */
    public void setState(LexerState state) {
        if (state == null) {
            throw new NullPointerException("Lexer state cannot be null.");
        }
        this.state = state;
    }
}
