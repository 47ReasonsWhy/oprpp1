package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.util.List;

/**
 * A shell command that prints the description for the given command, or lists all commands if no command is given.
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class HelpShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = arguments.split("\\s+");
        if (args.length > 1) {
            env.writeln("Expected 0 or 1 arguments, got " + args.length + ".");
            return ShellStatus.CONTINUE;
        }

        if (args.length == 0 || args[0].isBlank()) {
            env.writeln("Available commands:");
            env.commands().forEach((name, command) -> env.writeln("  " + name));
            return ShellStatus.CONTINUE;
        }

        ShellCommand command = env.commands().get(args[0]);
        if (command == null) {
            env.writeln("Unknown command: " + args[0]);
            return ShellStatus.CONTINUE;
        }

        env.writeln(command.getCommandName());
        command.getCommandDescription().forEach(env::writeln);

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(
            "Usage: help [<command>]",
            "Prints the description for the given command, or lists all commands if no command is given."
        );
    }
}
