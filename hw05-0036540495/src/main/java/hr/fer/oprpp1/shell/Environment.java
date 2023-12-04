package hr.fer.oprpp1.shell;

import java.util.SortedMap;

/**
 * An abstraction of an environment in which {@link MyShell} operates.
 * Each command communicates with the user through this interface.
 *
 * @see MyShell
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface Environment {
    /**
     * Reads a line from the environment.
     *
     * @return line read from the environment
     * @throws ShellIOException if an error occurs while reading
     */
    String readLine() throws ShellIOException;

    /**
     * Writes the given text to the environment.
     *
     * @param text text to be written
     * @throws ShellIOException if an error occurs while writing
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes the given text to the environment and adds a new line.
     *
     * @param text text to be written
     * @throws ShellIOException if an error occurs while writing
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns a sorted map of all commands available in the environment.
     * <p>
     * The implementation should ensure that the returned map is unmodifiable.
     *
     * @return sorted map of all commands available in the environment
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Returns the current multiline symbol.
     *
     * @return current multiline symbol
     */
    Character getMultilineSymbol();

    /**
     * Sets the multiline symbol to the given symbol.
     *
     * @param symbol new multiline symbol
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Returns the current prompt symbol.
     *
     * @return current prompt symbol
     */
    Character getPromptSymbol();

    /**
     * Sets the prompt symbol to the given symbol.
     *
     * @param symbol new prompt symbol
     */
    void setPromptSymbol(Character symbol);

    /**
     * Returns the current more-lines symbol.
     *
     * @return current more-lines symbol
     */
    Character getMoreLinesSymbol();

    /**
     * Sets the more-lines symbol to the given symbol.
     *
     * @param symbol new more-lines symbol
     */
    void setMoreLinesSymbol(Character symbol);
}
