package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static hr.fer.oprpp1.shell.commands.util.Util.*;

/**
 * A shell command that prints a tree of the given directory.
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class TreeShellCommand implements ShellCommand {
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

        Path file;
        try {
            file = Path.of(args[0]);
        } catch (IllegalArgumentException e) {
            env.writeln("Invalid path: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }

        boolean isDirectory;
        try {
            isDirectory = isDirectory(file, env);
        } catch (SecurityException e) {
            env.writeln("Security manager denies access to given file: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }
        if (!isDirectory) {
            return ShellStatus.CONTINUE;
        }

        FileVisitor<Path> fileVisitor = new FileVisitor<>() {
            int level = 0;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                env.writeln("  ".repeat(level) + dir.getFileName());
                level++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                env.writeln("  ".repeat(level) + file.getFileName());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                env.writeln("  ".repeat(level) + file.getFileName());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                level--;
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(Path.of(args[0]), fileVisitor);
        } catch (IOException e) {
            env.writeln("Error while walking the file tree: " + e.getMessage());
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(
            "Usage: tree directory_path",
            "Prints a tree of the given directory."
        );
    }
}
