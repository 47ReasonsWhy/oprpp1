package hr.fer.oprpp1.shell;

import hr.fer.oprpp1.shell.commands.*;

import java.util.*;

/**
 * Represents a simple shell program.
 * It supports multiline input, as well as the following commands:
 * <ul>
 *     <li>cat</li>
 *     <li>charsets</li>
 *     <li>copy</li>
 *     <li>exit</li>
 *     <li>help</li>
 *     <li>hexdump</li>
 *     <li>ls</li>
 *     <li>mkdir</li>
 *     <li>symbol</li>
 *     <li>tree</li>
 * </ul>
 *
 * @see ShellCommand
 * @see Environment
 *
 * @see CatShellCommand
 * @see CharsetsShellCommand
 * @see CopyShellCommand
 * @see ExitShellCommand
 * @see HelpShellCommand
 * @see HexdumpShellCommand
 * @see LsShellCommand
 * @see MkdirShellCommand
 * @see SymbolShellCommand
 * @see TreeShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class MyShell {
    public static void main(String[] args) {
        Environment environment = new Environment() {
            Character PROMPTSYMBOL = '>';
            Character MORELINESSYMBOL = '\\';
            Character MULTILINESYMBOL = '|';

            final SortedMap<String, ShellCommand> commands = new TreeMap<>(Map.of(
                "exit", new ExitShellCommand(),
                "symbol", new SymbolShellCommand(),
                "charsets", new CharsetsShellCommand(),
                "cat", new CatShellCommand(),
                "ls", new LsShellCommand(),
                "tree", new TreeShellCommand(),
                "copy", new CopyShellCommand(),
                "mkdir", new MkdirShellCommand(),
                "hexdump", new HexdumpShellCommand(),
                "help", new HelpShellCommand()
            ));

            @Override
            public String readLine() throws ShellIOException {
                StringBuilder sb = new StringBuilder();
                Scanner sc = new Scanner(System.in);
                String line;
                try {
                    line = sc.nextLine();
                } catch (Exception e) {
                    throw new ShellIOException(e.getMessage());
                }
                while (line.endsWith(MORELINESSYMBOL.toString())) {
                    sb.append(line, 0, line.length() - 1);
                    System.out.print(MULTILINESYMBOL + " ");
                    try {
                        line = sc.nextLine();
                    } catch (Exception e) {
                        throw new ShellIOException(e.getMessage());
                    }
                }
                sb.append(line);
                return sb.toString();
            }

            @Override
            public void write(String text) throws ShellIOException {
                System.out.print(text);
            }

            @Override
            public void writeln(String text) throws ShellIOException {
                System.out.println(text);
            }

            @Override
            public SortedMap<String, ShellCommand> commands() {
                return Collections.unmodifiableSortedMap(commands);
            }

            @Override
            public Character getMultilineSymbol() {
                return MULTILINESYMBOL;
            }

            @Override
            public void setMultilineSymbol(Character symbol) {
                MULTILINESYMBOL = symbol;
            }

            @Override
            public Character getPromptSymbol() {
                return PROMPTSYMBOL;
            }

            @Override
            public void setPromptSymbol(Character symbol) {
                PROMPTSYMBOL = symbol;
            }

            @Override
            public Character getMoreLinesSymbol() {
                return MORELINESSYMBOL;
            }

            @Override
            public void setMoreLinesSymbol(Character symbol) {
                MORELINESSYMBOL = symbol;
            }
        };

        environment.writeln("Welcome to MyShell v 1.0");
        ShellStatus status = ShellStatus.CONTINUE;
        do {
            environment.write(environment.getPromptSymbol() + " ");
            String l = environment.readLine();
            String commandName = l.split("\\s+")[0];
            String arguments = l.substring(commandName.length()).trim();
            ShellCommand command = environment.commands().get(commandName);
            if (command == null) {
                environment.writeln("Unknown command: " + commandName);
                continue;
            }
            status = command.executeCommand(environment, arguments);
        } while (status != ShellStatus.TERMINATE);
    }
}
