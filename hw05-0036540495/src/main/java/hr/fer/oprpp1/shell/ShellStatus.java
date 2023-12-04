package hr.fer.oprpp1.shell;

/**
 * Enumeration of shell status.
 * <p>
 * CONTINUE - shell should continue with its work
 * TERMINATE - shell should terminate
 *
 * @see MyShell
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public enum ShellStatus {
    /**
     * Shell should continue with its work
     */
    CONTINUE,

    /**
     * Shell should terminate
     */
    TERMINATE
}
