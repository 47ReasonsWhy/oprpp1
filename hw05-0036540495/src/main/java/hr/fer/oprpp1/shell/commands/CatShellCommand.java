package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

import static hr.fer.oprpp1.shell.commands.util.Util.*;

/**
 * A command that opens a file, reads its content with the given charset and writes it to the console.
 * If charset is not given, default platform charset is used (UTF-8).
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class CatShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args;
        try {
            args = parseFilePathArguments(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        if (args.length != 1 && args.length != 2) {
            env.writeln("Invalid number of arguments. Expected 1 or 2, got " + args.length + ".");
            return ShellStatus.CONTINUE;
        }

        Path file;
        try {
            file = Path.of(args[0]);
        } catch (InvalidPathException e) {
            env.writeln("Invalid path: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }

        Charset charset;
        try {
            boolean isReadable = isReadable(file, env);
            if (!isReadable) {
                if (Files.isDirectory(file)) env.writeln("Given file is a directory.");
                return ShellStatus.CONTINUE;
            }

            charset = args.length == 2 ? Charset.forName(args[1]) : Charset.defaultCharset();
            try (var reader = Files.newBufferedReader(Path.of(args[0]),charset)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    env.writeln(line);
                }
            }
        } catch (SecurityException e) {
            env.writeln("Security manager denies access to given file: " + e.getMessage());
        } catch (IllegalCharsetNameException e) {
            env.writeln("Invalid charset name: " + e.getMessage());
        } catch (UnsupportedCharsetException e) {
            env.writeln("Unsupported charset: " + e.getMessage());
        } catch (IOException e) {
            env.writeln("Error while reading given file: " + e.getMessage());
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(
            "Usage: cat <file> [<charset>]",
            "Opens <file> and writes its content to console.",
            "If <charset> is not given, default platform charset is used (UTF-8)."
        );
    }
}
