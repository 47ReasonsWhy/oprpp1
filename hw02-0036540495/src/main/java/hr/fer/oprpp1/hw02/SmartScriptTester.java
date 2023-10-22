package hr.fer.oprpp1.hw02;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class SmartScriptTester is used to test the SmartScriptParser class.
 * <p>
 * It takes one argument, the path to the document to be parsed,
 * parses the document, prints out the result, parses the result again,
 * and then checks if the two documents are equal.
 */
public class SmartScriptTester {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Expected 1 argument (path to document), got " + args.length);
            System.exit(-1);
        }
        String filepath = args[0];
        String docBody = null;
        try {
            docBody = Files.readString(Paths.get(filepath));
        } catch (IOException e) {
            System.out.println("Unable to read file " + filepath);
            System.exit(-1);
        }

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch(SmartScriptParserException e) {
            System.out.println("Unable to parse document!");
            System.out.println(e.getMessage());
            System.exit(-1);
        } catch(Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();
        System.out.println("Parsed original:\n");
        System.out.println(document.toString());

        SmartScriptParser parser2 = null;
        try {
            parser2 = new SmartScriptParser(document.toString());
        } catch(SmartScriptParserException e) {
            System.out.println("Unable to reparse parsed document!");
            System.out.println(e.getMessage());
            System.exit(-1);
        } catch(Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document2 = parser2.getDocumentNode();
        System.out.println("\nParsed re-parsed:\n");
        System.out.println(document2.toString());

        System.out.println("\nAre the two documents equal? " + document.equals(document2));
    }
}
