package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

import static hr.fer.oprpp1.shell.commands.util.Util.parseFilePathArguments;

/**
 * A command that copies a file from one location to another.
 * The user is asked if they want to overwrite the destination file if it already exists.
 * <p>
 * The command expects two arguments: source file path and destination file path.
 * If the destination file path is a directory, the file is copied into that directory, preserving the original file name.
 * If the destination file path is a file, the user is asked if they want to overwrite the file.
 * If the user answers with 'Y' or 'y', the file is overwritten.
 * If the user answers with 'N' or 'n', the command is aborted.
 * In any other case, the user will be asked again.
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class CopyShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args;
        try {
            args = parseFilePathArguments(arguments);
        } catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }
        if (args.length != 2) {
            env.writeln("Invalid number of arguments. Expected 2, got " + args.length + ".");
            return ShellStatus.CONTINUE;
        }

        Path source;
        Path destination;
        try {
            source = Path.of(args[0]);
            destination = Path.of(args[1]);
        } catch (InvalidPathException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        if (!Files.exists(source)) {
            env.writeln("Source file does not exist.");
            return ShellStatus.CONTINUE;
        }
        if (Files.isDirectory(source)) {
            env.writeln("Source file is a directory.");
            return ShellStatus.CONTINUE;
        }

        if (Files.isDirectory(destination)) {
            destination = destination.resolve(source.getFileName());
        }

        if (Files.exists(destination)) {
            env.writeln("Destination file already exists. Do you want to overwrite it? (Y/N)");
            String answer = env.readLine();
            while (answer.length() != 1 && "YNyn".indexOf(answer.charAt(0)) == -1) {
                env.writeln("Invalid answer. Please answer with 'Y' or 'N'.");
                answer = env.readLine();
            }
            if (answer.equals("N") || answer.equals("n")) {
                env.writeln("Aborting command.");
                return ShellStatus.CONTINUE;
            }
        }
        try (InputStream is = Files.newInputStream(source);
             OutputStream os = Files.newOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            while (true) {
                int r = is.read(buffer);
                if (r < 1) break;
                os.write(buffer, 0, r);
            }
        } catch (IOException e) {
            env.writeln("Error while copying file: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(
            "Usage: copy <source> <destination>",
            "Copies the file from <source> to <destination>.",
            "If <destination> does not exist, the file is created.",
            "If <destination> is directory, copies the file into that directory, preserving the original file name.",
            "If <destination> is file, the user is asked if they want to overwrite the file.",
            "If the user answers with 'Y' or 'y', the file is overwritten.",
            "If the user answers with 'N' or 'n', the command is aborted.",
            "In any other case, the user is asked again."
        );
    }
}
