package hr.fer.oprpp1.hw02.parser;

import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class SmartScriptParserTest {
    @Test
    public void testNullInput() {
        assertThrows(NullPointerException.class, () -> new SmartScriptParser(null));
    }

    @Test
    public void testEmptyDocument() {
        SmartScriptParser parser = new SmartScriptParser("");
        assertEquals(0, parser.getDocumentNode().numberOfChildren());
    }

    @Test
    public void testDoc1() {
        String docBody = readDoc(1);
        SmartScriptParser parser = new SmartScriptParser(docBody);

        // we expect four direct children: a text node, a for-loop, a text node, and another for-loop
        assertEquals(4, parser.getDocumentNode().numberOfChildren());
        assertInstanceOf(TextNode.class, parser.getDocumentNode().getChild(0));
        assertInstanceOf(ForLoopNode.class, parser.getDocumentNode().getChild(1));
        assertInstanceOf(TextNode.class, parser.getDocumentNode().getChild(2));
        assertInstanceOf(ForLoopNode.class, parser.getDocumentNode().getChild(3));

        // the first text node should have the following text: "This is sample text.\r\n"
        TextNode text01 = (TextNode) parser.getDocumentNode().getChild(0);
        assertEquals("This is sample text.\r\n", text01.getText());
        // the first for-loop should have the following parameters: i 1 10 1
        ForLoopNode forLoop1 = (ForLoopNode) parser.getDocumentNode().getChild(1);
        assertEquals("i", forLoop1.getVariable().asText());
        assertEquals(1, Integer.parseInt(forLoop1.getStartExpression().asText()));
        assertEquals(10, Integer.parseInt(forLoop1.getEndExpression().asText()));
        assertEquals(1, Integer.parseInt(forLoop1.getStepExpression().asText()));
        // the second text node should have the following text: "\r\n"
        TextNode text02 = (TextNode) parser.getDocumentNode().getChild(2);
        assertEquals("\r\n", text02.getText());
        // the second for-loop should have the following parameters: i 0 10 2
        ForLoopNode forLoop2 = (ForLoopNode) parser.getDocumentNode().getChild(3);
        assertEquals("i", forLoop2.getVariable().asText());
        assertEquals(0, Integer.parseInt(forLoop2.getStartExpression().asText()));
        assertEquals(10, Integer.parseInt(forLoop2.getEndExpression().asText()));
        assertEquals(2, Integer.parseInt(forLoop2.getStepExpression().asText()));

        // the first for-loop should have three children: a text node, an echo node, and another text node
        assertEquals(3, forLoop1.numberOfChildren());
        assertInstanceOf(TextNode.class, forLoop1.getChild(0));
        assertInstanceOf(EchoNode.class, forLoop1.getChild(1));
        assertInstanceOf(TextNode.class, forLoop1.getChild(2));
        // the second for-loop should have five children:
        // a text node, an echo node, a text node, an echo node, and another text node
        assertEquals(5, forLoop2.numberOfChildren());
        assertInstanceOf(TextNode.class, forLoop2.getChild(0));
        assertInstanceOf(EchoNode.class, forLoop2.getChild(1));
        assertInstanceOf(TextNode.class, forLoop2.getChild(2));
        assertInstanceOf(EchoNode.class, forLoop2.getChild(3));
        assertInstanceOf(TextNode.class, forLoop2.getChild(4));

        // in the first for-loop:
        // the first text node should have the following text: "\r\n\tThis is "
        TextNode text11 = (TextNode) forLoop1.getChild(0);
        assertEquals("\r\n    This is ", text11.getText());
        // the echo node should have one element: a variable named "i"
        EchoNode echo11 = (EchoNode) forLoop1.getChild(1);
        assertEquals(1, echo11.getElements().length);
        assertEquals("i", echo11.getElements()[0].asText());
        // the second text node should have the following text: "-th time this message is generated.\r\n"
        TextNode text12 = (TextNode) forLoop1.getChild(2);
        assertEquals("-th time this message is generated.\r\n", text12.getText());

        // in the second for-loop:
        // the first text node should have the following text: "\r\n\tsin("
        TextNode text21 = (TextNode) forLoop2.getChild(0);
        assertEquals("\r\n    sin(", text21.getText());
        // the first echo node should have one element: a variable named "i"
        EchoNode echo21 = (EchoNode) forLoop2.getChild(1);
        assertEquals(1, echo21.getElements().length);
        assertInstanceOf(ElementVariable.class, echo21.getElements()[0]);
        assertEquals("i", echo21.getElements()[0].asText());
        // the second text node should have the following text: ") = "
        assertEquals("^2) = ", ((TextNode) forLoop2.getChild(2)).getText());
        // the second echo node should have 6 elements:
        // two times a variable named "i", the operator "*", a function named "sin",
        // a string literal with value "0.000", and a function named "decfmt"
        EchoNode echo22 = (EchoNode) forLoop2.getChild(3);
        assertEquals(6, echo22.getElements().length);
        assertInstanceOf(ElementVariable.class, echo22.getElements()[0]);
        assertEquals("i", echo22.getElements()[0].asText());
        assertInstanceOf(ElementVariable.class, echo22.getElements()[1]);
        assertEquals("i", echo22.getElements()[1].asText());
        assertInstanceOf(ElementOperator.class, echo22.getElements()[2]);
        assertEquals("*", echo22.getElements()[2].asText());
        assertInstanceOf(ElementFunction.class, echo22.getElements()[3]);
        assertEquals("@sin", echo22.getElements()[3].asText());
        assertInstanceOf(ElementString.class, echo22.getElements()[4]);
        assertEquals("\"0.000\"", echo22.getElements()[4].asText());
        assertInstanceOf(ElementFunction.class, echo22.getElements()[5]);
        assertEquals("@decfmt", echo22.getElements()[5].asText());
    }

    @Test
    public void testDoc3() {
        String docBody = readDoc(3);
        SmartScriptParser parser = new SmartScriptParser(docBody);

        // we expect three direct children: a text node, an echo node, and another text node
        assertEquals(3, parser.getDocumentNode().numberOfChildren());
        assertInstanceOf(TextNode.class, parser.getDocumentNode().getChild(0));
        assertInstanceOf(EchoNode.class, parser.getDocumentNode().getChild(1));
        assertInstanceOf(TextNode.class, parser.getDocumentNode().getChild(2));

        // the first text node should have the following text:
        /*
        Now let's try all the escapes!
        In the text we just have \\\\ as \\ and \\\{ as {".
        And in the tags we have:

        */
        TextNode text1 = (TextNode) parser.getDocumentNode().getChild(0);
        assertEquals("""
        Now let's try all the escapes!\r
        In the text we just have \\\\ as \\ and \\{ as {".\r
        And in the tags we have:\r
        """, text1.getText());

        // the echo node should have 1 element: a string literal with value:
        /*
        \\\\ as \\, \\\" as \", \\n as enter, \\t as tab, and \\r as CR.
        */
        EchoNode echo = (EchoNode) parser.getDocumentNode().getChild(1);
        assertEquals(1, echo.getElements().length);
        assertInstanceOf(ElementString.class, echo.getElements()[0]);
        assertEquals("""
            "\\\\\\\\ as \\\\, \\\\\\" as \\", \\\\n as enter, \\\\t as tab, and \\\\r as CR.\"""",
            echo.getElements()[0].asText()
        );

        // the second text node should have the following text: "\r\n"
        TextNode text2 = (TextNode) parser.getDocumentNode().getChild(2);
        assertEquals("\r\n", text2.getText());
    }

    @Test
    public void testExamples123() {
        testExample123(1);
        testExample123(2);
        testExample123(3);
    }

    @Test
    public void testExamples67() {
        testExample67(6);
        testExample67(7);
    }

    @Test
    public void testExamples4589() {
        testExample4589(4);
        testExample4589(5);
        testExample4589(8);
        testExample4589(9);
    }

    private void testExample123(int n) {
        String docBody = readExample(n);
        SmartScriptParser parser = new SmartScriptParser(docBody);
        // we should have only one text node
        assertEquals(1, parser.getDocumentNode().numberOfChildren());
        assertEquals(0, parser.getDocumentNode().getChild(0).numberOfChildren());
        assertInstanceOf(TextNode.class, parser.getDocumentNode().getChild(0));
        // and now to check if the parsed and the double-parsed document are equal
        SmartScriptParser parser2 = new SmartScriptParser(parser.getDocumentNode().toString());
        assertEquals(parser.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    private void testExample67(int n) {
        String docBody = readExample(n);
        SmartScriptParser parser = new SmartScriptParser(docBody);
        // we expect 3 nodes: TextNode, EchoNode, TextNode ("\n" at the end of the document)
        assertEquals(3, parser.getDocumentNode().numberOfChildren());
        assertEquals(0, parser.getDocumentNode().getChild(0).numberOfChildren());
        assertInstanceOf(TextNode.class, parser.getDocumentNode().getChild(0));
        assertEquals(0, parser.getDocumentNode().getChild(1).numberOfChildren());
        assertInstanceOf(EchoNode.class, parser.getDocumentNode().getChild(1));
        assertEquals(0, parser.getDocumentNode().getChild(2).numberOfChildren());
        assertInstanceOf(TextNode.class, parser.getDocumentNode().getChild(2));
        // and now to check if the parsed and the double-parsed document are equal
        SmartScriptParser parser2 = new SmartScriptParser(parser.getDocumentNode().toString());
        assertEquals(parser.getDocumentNode().toString(), parser2.getDocumentNode().toString());
    }

    private void testExample4589(int n) {
        String docBody = readExample(n);
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    private String readExample(int n) {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/example" + n + ".txt")) {
            if (is == null) throw new RuntimeException("File extra/example" + n + ".txt is unavailable.");
            byte[] data = is.readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Error while reading file.", ex);
        }
    }

    private String readDoc(int n) {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/doc" + n + ".txt")) {
            if (is == null) throw new RuntimeException("File examples/doc" + n + ".txt is unavailable.");
            byte[] data = is.readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Error while reading file.", ex);
        }
    }
}
