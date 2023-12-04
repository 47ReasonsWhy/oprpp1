package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

import static hr.fer.oprpp1.shell.commands.util.Util.parseFilePathArguments;
import static hr.fer.oprpp1.shell.commands.util.Util.isReadable;

/**
 * A command that prints the hexdump of a file.
 * The given file must be readable.
 * <p>
 * The hexdump is printed in the following format:
 * <pre>
 * 00000000: 6C 6F 72 65 6D 20 69 70|73 75 6D 20 64 6F 6C 6F | lorem ipsum dolo
 * 00000010: 72 20 73 69 74 20 61 6D|65 74 2C 20 63 6F 6E 73 | r sit amet, cons
 * 00000020: 65 63 74 65 74 75 72 20|61 64 69 70 69 73 69 63 | ectetur adipisic
 * 00000030: 69 6E 67 20 65 6C 69 74|2E 20 4E 75 6C 6C 61 6D | ing elit. Nullam
 * 00000040: 20 6C 65 6F 20 6E 69 62|68 20 6E 6F 6E 20 6E 69 |  leo nibh non ni
 * 00000050: 62 68 20 6C 61 6F 72 65|65 74 2E                | bh laoreet.
 * </pre>
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class HexdumpShellCommand implements ShellCommand {
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
        } catch (InvalidPathException e) {
            env.writeln("Invalid path: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }

        boolean isReadable;
        try {
            isReadable = isReadable(file, env);
        } catch (SecurityException e) {
            env.writeln("Security manager denies access to given file: " + e.getMessage());
            return ShellStatus.CONTINUE;
        }
        if (!isReadable) {
            return ShellStatus.CONTINUE;
        }

        try (InputStream is = Files.newInputStream(file)) {
            byte[] buffer = new byte[16];
            int offset = 0;
            while (true) {
                int read = is.read(buffer);
                if (read < 1) {
                    break;
                }
                env.write(String.format("%08X: ", offset));
                offset += read;
                for (int i = 0; i < 16; i++) {
                    env.write(i < read ? String.format("%02X", buffer[i]) : "  ");
                    env.write(i == 7 ? "|" : " ");
                }
                env.write("| ");
                for (int i = 0; i < read; i++) {
                    // since byte is signed in Java, all bytes above 127 are actually negative
                    env.write(buffer[i] < 32 /* || buffer[i] > 127 */ ? "." : String.valueOf((char) buffer[i]));
                }
                env.writeln("");
            }
        } catch (Exception e) {
            env.writeln("Error while reading file: " + e.getMessage());
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(
            "Usage: hexdump <file>",
            "Prints the hexdump of <file>."
        );
    }
}
