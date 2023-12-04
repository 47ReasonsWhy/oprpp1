package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

import static hr.fer.oprpp1.shell.commands.util.Util.parseFilePathArguments;

/**
 * A command that creates a directory.
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class MkdirShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args;
        try {
            args = parseFilePathArguments(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        if (args.length != 1) {
            env.writeln("Expected 1 argument, got " + args.length + ".");
            return ShellStatus.CONTINUE;
        }

        Path path;
        try {
            path = Path.of(args[0]);
        } catch (InvalidPathException e) {
            env.writeln("Invalid path: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }
        if (Files.exists(path)) {
            env.writeln("A file or directory with the same name already exists.");
            return ShellStatus.CONTINUE;
        }

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            env.writeln("An error occurred while creating the directory: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(
            "Usage: mkdir <directory>",
            "Creates the <directory>."
        );
    }
}
