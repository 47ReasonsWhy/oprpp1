package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.List;

/**
 * A command that takes no arguments and lists names of supported charsets for your Java platform.
 * A single charset name is written per line.
 *
 * @see Charset
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class CharsetsShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        if (!arguments.isEmpty() && !arguments.split("\\s+")[0].isEmpty()) {
            env.writeln("Charsets command does not take any arguments.");
            return ShellStatus.CONTINUE;
        }
        Charset.availableCharsets().forEach((key, value) -> env.writeln(key));
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(
            "Usage: charsets",
            "Lists names of supported charsets for your Java platform.",
            "A single charset name is written per line."
        );
    }
}
