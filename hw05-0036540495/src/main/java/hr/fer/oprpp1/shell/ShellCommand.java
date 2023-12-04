package hr.fer.oprpp1.shell;

import java.util.List;

/**
 * Interface that represents a {@link MyShell} command.
 * Every {@link MyShell} command must implement this interface.
 *
 * @see MyShell
 * @see ShellStatus
 * @see Environment
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface ShellCommand {
    /**
     * Executes the command in the given environment with the given arguments.
     *
     * @param env environment in which the command is executed
     * @param arguments arguments of the command, excluding the command name
     * @return {@link ShellStatus} after the command is executed
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Returns the name of the command.
     *
     * @return name of the command
     */
    String getCommandName();

    /**
     * Returns the description of the command.
     *
     * @return description of the command
     */
    List<String> getCommandDescription();
}
