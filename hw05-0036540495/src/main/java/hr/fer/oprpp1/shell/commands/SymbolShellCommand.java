package hr.fer.oprpp1.shell.commands;

import hr.fer.oprpp1.shell.Environment;
import hr.fer.oprpp1.shell.ShellCommand;
import hr.fer.oprpp1.shell.ShellStatus;

import java.util.List;

/**
 * A shell command that can be used to print or change the symbols (PROMPT | MORELINES | MULTILINE) used by the shell environment.
 * <p>
 * If a new symbol is not given, prints the current symbol for that argument.
 * <p>
 * Otherwise, changes the symbol for the first argument to the new symbol.
 * The new symbol must be a single character.
 *
 * @see ShellCommand
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class SymbolShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] args = arguments.split("\\s+");
        switch (args.length) {
            case 2 -> {
                if (args[1].length() != 1) {
                    env.writeln("Invalid new character for symbol. Must be a single character.");
                    return ShellStatus.CONTINUE;
                }
                switch (args[0]) {
                    case "PROMPT" -> {
                        env.writeln("Symbol for PROMPT changed from '" + env.getPromptSymbol() + "' to '" + args[1] + "'");
                        env.setPromptSymbol(args[1].charAt(0));
                    }
                    case "MORELINES" -> {
                        env.writeln("Symbol for MORELINES changed from '" + env.getMoreLinesSymbol() + "' to '" + args[1] + "'");
                        env.setMoreLinesSymbol(args[1].charAt(0));
                    }
                    case "MULTILINE" -> {
                        env.writeln("Symbol for MULTILINE changed from '" + env.getMultilineSymbol() + "' to '" + args[1] + "'");
                        env.setMultilineSymbol(args[1].charAt(0));
                    }
                    default -> env.writeln("Invalid symbol. Valid symbols are PROMPT, MORELINES and MULTILINE.");
                }
            }
            case 1 -> {
                switch (args[0]) {
                    case "PROMPT" -> env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'");
                    case "MORELINES" -> env.writeln("Symbol for MORELINES is '" + env.getMoreLinesSymbol() + "'");
                    case "MULTILINE" -> env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'");
                    default -> env.writeln("Invalid symbol. Valid symbols are PROMPT, MORELINES and MULTILINE.");
                }
            }
            default -> env.writeln("Invalid number of arguments. Expecting 1 or 2.");
        }
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of("Usage: symbol PROMPT|MORELINES|MULTILINE [new_symbol]",
                       "If a new symbol is not given, prints the current symbol for that argument.",
                       "Otherwise, changes the symbol for the first argument to the new symbol.",
                       "The new symbol must be a single character.");
    }
}
