package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.util.List;

/**
 * A command that terminates the shell.
 * Takes no arguments.
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class ExitShellCommand implements ShellCommand {
        @Override
        public ShellStatus executeCommand(Environment environment, String arguments) {
            if (!arguments.isEmpty() && !arguments.split("\\s+")[0].isEmpty()) {
                environment.writeln("Exit command does not take any arguments.");
                return ShellStatus.CONTINUE;
            }
            return ShellStatus.TERMINATE;
        }

        @Override
        public String getCommandName() {
            return "exit";
        }

        @Override
        public List<String> getCommandDescription() {
            return List.of(
                "Usage: exit",
                "Terminates the shell."
            );
        }
}
