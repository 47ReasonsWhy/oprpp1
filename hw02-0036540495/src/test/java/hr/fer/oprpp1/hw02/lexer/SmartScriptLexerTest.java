package hr.fer.oprpp1.hw02.lexer;

import hr.fer.oprpp1.custom.scripting.lexer.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class SmartScriptLexerTest {

    @Test
    public void testNullInput() {
        assertThrows(NullPointerException.class, () -> new SmartScriptLexer(null));
    }

    @Test
    public void testEOF() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testAfterEOF() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        lexer.nextToken();
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
    }

    @Test
    public void testGetReturnsLastNext() {
        SmartScriptLexer lexer = new SmartScriptLexer("");

        SmartScriptToken token = lexer.nextToken();
        assertEquals(token, lexer.getToken());
        assertEquals(token, lexer.getToken());
    }

    @Test
    public void testExample12367() {
        testExample(1);
        testExample(2);
        testExample(3);
        testExample(6);
        testExample(7);
    }

    @Test
    public void testExample4589() {
        testExampleThrows(4);
        testExampleThrows(5);
        testExampleThrows(8);
        testExampleThrows(9);
    }

    private void testExample(int n) {
        String docBody = readExample(n);
        SmartScriptLexer lexer = new SmartScriptLexer(docBody);
        SmartScriptToken[] correctData = expectedTokens(n);
        for (SmartScriptToken expected : correctData) {
            SmartScriptToken actual = lexer.nextToken();
            assertEquals(expected.getType(), actual.getType());
            assertEquals(expected.getValue(), actual.getValue());
            switch (expected.getType()) {
                case START_TAG_DEF -> lexer.setState(SmartScriptLexerState.TAG_NAME);
                case TAG_NAME -> lexer.setState(SmartScriptLexerState.TAG_DEF);
                case END_TAG_DEF -> lexer.setState(SmartScriptLexerState.TEXT);
                default -> {
                }
            }
        }
        assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType());
    }

    private void testExampleThrows(int n) {
        String docBody = readExample(n);
        SmartScriptLexer lexer = new SmartScriptLexer(docBody);
        SmartScriptToken[] correctData = expectedTokens(n);
        for (SmartScriptToken expected : correctData) {
            SmartScriptToken actual = lexer.nextToken();
            assertEquals(expected.getType(), actual.getType());
            assertEquals(expected.getValue(), actual.getValue());
            switch (expected.getType()) {
                case START_TAG_DEF -> lexer.setState(SmartScriptLexerState.TAG_NAME);
                case TAG_NAME -> lexer.setState(SmartScriptLexerState.TAG_DEF);
                case END_TAG_DEF -> lexer.setState(SmartScriptLexerState.TEXT);
                default -> {
                }
            }
        }
        assertThrows(SmartScriptLexerException.class, lexer::nextToken);
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

    SmartScriptToken[] expectedTokens(int n) {
        SmartScriptToken[] correctData;
        switch (n) {
            case 1 -> {
                SmartScriptToken token11 = new SmartScriptToken(SmartScriptTokenType.TEXT, """
                        Ovo je\s
                        sve jedan text node
                        """);
                correctData = new SmartScriptToken[]{token11};
            }
            case 2 -> {
                SmartScriptToken token21 = new SmartScriptToken(SmartScriptTokenType.TEXT, """
                        Ovo je\s
                        sve jedan {$ text node
                        """);
                correctData = new SmartScriptToken[]{token21};
            }
            case 3 -> {
                SmartScriptToken token31 = new SmartScriptToken(SmartScriptTokenType.TEXT, """
                        Ovo je\s
                        sve jedan \\{$text node
                        """);
                correctData = new SmartScriptToken[]{token31};
            }
            case 4, 5 -> correctData = new SmartScriptToken[]{};
            case 6 -> {
                SmartScriptToken token61 = new SmartScriptToken(SmartScriptTokenType.TEXT, "Ovo je OK ");
                SmartScriptToken token62 = new SmartScriptToken(SmartScriptTokenType.START_TAG_DEF, null);
                SmartScriptToken token63 = new SmartScriptToken(SmartScriptTokenType.TAG_NAME, "=");
                SmartScriptToken token64 = new SmartScriptToken(SmartScriptTokenType.STRING, """
                        String ide
                        u više redaka
                        čak tri""");
                SmartScriptToken token65 = new SmartScriptToken(SmartScriptTokenType.END_TAG_DEF, null);
                SmartScriptToken token66 = new SmartScriptToken(SmartScriptTokenType.TEXT, "\n");
                correctData = new SmartScriptToken[]{token61, token62, token63, token64, token65, token66};
            }
            case 7 -> {
                SmartScriptToken token71 = new SmartScriptToken(SmartScriptTokenType.TEXT, "Ovo je isto OK ");
                SmartScriptToken token72 = new SmartScriptToken(SmartScriptTokenType.START_TAG_DEF, null);
                SmartScriptToken token73 = new SmartScriptToken(SmartScriptTokenType.TAG_NAME, "=");
                SmartScriptToken token74 = new SmartScriptToken(SmartScriptTokenType.STRING, """
                        String ide
                        u "više"\s
                        redaka
                        ovdje a stvarno četiri""");
                SmartScriptToken token75 = new SmartScriptToken(SmartScriptTokenType.END_TAG_DEF, null);
                SmartScriptToken token76 = new SmartScriptToken(SmartScriptTokenType.TEXT, "\n");
                correctData = new SmartScriptToken[]{token71, token72, token73, token74, token75, token76};
            }
            case 8, 9 -> {
                SmartScriptToken token81 = new SmartScriptToken(SmartScriptTokenType.TEXT, "Ovo se ruši ");
                SmartScriptToken token82 = new SmartScriptToken(SmartScriptTokenType.START_TAG_DEF, null);
                SmartScriptToken token83 = new SmartScriptToken(SmartScriptTokenType.TAG_NAME, "=");
                correctData = new SmartScriptToken[]{token81, token82, token83};
            }
            default -> throw new IllegalArgumentException("Invalid example number: " + n);
        }
        return correctData;
    }
}
