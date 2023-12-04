package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

import static hr.fer.oprpp1.shell.commands.util.Util.parseFilePathArguments;

/**
 * A command that lists all files and directories in a given directory.
 * If a given path is instead a regular file, it prints its attributes.
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class LsShellCommand implements ShellCommand {
    @Override
    public ShellStatus executeCommand(Environment environment, String arguments) {
        String[] args;
        try {
            args = parseFilePathArguments(arguments);
        } catch (IllegalArgumentException e) {
            environment.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }
        if (args.length != 1) {
            environment.writeln("Ls command takes one argument.");
            return ShellStatus.CONTINUE;
        }

        Path file = Path.of(args[0]);
        if (!Files.exists(file)) {
            environment.writeln("Given file does not exist.");
            return ShellStatus.CONTINUE;
        }
        try {
            if (!Files.isDirectory(file)) {
                environment.writeln(singleFileFormatted(file));
                return ShellStatus.CONTINUE;
            }
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(file)) {
                for (Path path : stream) {
                    environment.writeln(singleFileFormatted(path));
                }
            }
        } catch (IOException e) {
            environment.writeln("Error while reading directory files: " + e.getMessage());
        } catch (SecurityException e) {
            environment.writeln("Security manager denied access to file: " + e.getMessage());
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public java.util.List<String> getCommandDescription() {
        return java.util.List.of(
            "Usage: ls <file>",
            "If <file> is directory, lists all its contents and their attributes. Is not recursive.",
            "If <file> is a regular file, prints its attributes."
        );
    }

    private String singleFileFormatted(Path file) throws IOException, SecurityException {
        return  (Files.isDirectory(file) ? "d" : "-") +
                (Files.isReadable(file) ? "r" : "-") +
                (Files.isWritable(file) ? "w" : "-") +
                (Files.isExecutable(file) ? "x" : "-") +
                " " +
                " ".repeat(10 - String.valueOf(Files.size(file)).length()) + Files.size(file) +
                " " +
                getFormattedDateTime(file) +
                " " +
                file.getFileName();
    }

    private String getFormattedDateTime(Path file) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BasicFileAttributeView faView = Files.getFileAttributeView(file, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );
        BasicFileAttributes attributes = faView.readAttributes();
        FileTime fileTime = attributes.creationTime();
        return sdf.format(new Date(fileTime.toMillis()));
    }
}
