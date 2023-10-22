package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.*;
import hr.fer.oprpp1.custom.scripting.nodes.*;

/**
 * A parser that generates a document model from the given input text.
 *
 * @see SmartScriptLexer
 * @see SmartScriptParserException
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class SmartScriptParser {

    /**
     * Lexer that generates tokens from the input text.
     */
    private final SmartScriptLexer lexer;

    /**
     * Document model.
     */
    private final DocumentNode documentNode;

    /**
     * Constructs a new parser that generates a document model from the given input text.
     *
     * @param text input text
     * @throws SmartScriptParserException if the input text is invalid
     */
    public SmartScriptParser(String text) {
        lexer = new SmartScriptLexer(text);
        documentNode = new DocumentNode();
        try {
            parse();
        } catch (SmartScriptLexerException e) {
            throw new SmartScriptParserException(e.getMessage());
        }
    }

    /**
     * Returns the generated document model.
     *
     * @return generated document model
     */
    public DocumentNode getDocumentNode() {
        return documentNode;
    }

    /**
     * Parses the input text and generates a document model.
     *
     * @throws SmartScriptParserException if the input text is invalid
     */
    private void parse() throws SmartScriptParserException {
        ObjectStack stack = new ObjectStack();
        stack.push(documentNode);
        SmartScriptToken token;

        while (true) {
            token = lexer.nextToken();

            if (token.getType() == SmartScriptTokenType.EOF) break;

            if (token.getType() == SmartScriptTokenType.TEXT) {
                ((Node) stack.peek()).addChildNode(new TextNode((String) token.getValue()));
            } else if (token.getType() == SmartScriptTokenType.START_TAG_DEF) {
                lexer.setState(SmartScriptLexerState.TAG_NAME);
            } else if (token.getType() == SmartScriptTokenType.TAG_NAME) {
                lexer.setState(SmartScriptLexerState.TAG_DEF);
                if (token.getValue().equals("=")) {
                    ArrayIndexedCollection elements = new ArrayIndexedCollection();
                    while (true) {
                        token = lexer.nextToken();
                        if (token.getType() == SmartScriptTokenType.END_TAG_DEF) {
                            lexer.setState(SmartScriptLexerState.TEXT);
                            break;
                        } else if (token.getType() == SmartScriptTokenType.VARIABLE ||
                                token.getType() == SmartScriptTokenType.STRING ||
                                token.getType() == SmartScriptTokenType.INTEGER ||
                                token.getType() == SmartScriptTokenType.DOUBLE ||
                                token.getType() == SmartScriptTokenType.FUNCTION ||
                                token.getType() == SmartScriptTokenType.OPERATOR) {
                            elements.add(newElement(token));
                        } else {
                            throw new SmartScriptParserException("Invalid element type in ECHO tag at position " + lexer.getCurrentIndex() + ".");
                        }
                    }
                    Element[] elementsArray = new Element[elements.size()];
                    for (int i = 0; i < elements.size(); i++) {
                        elementsArray[i] = (Element) elements.get(i);
                    }
                    ((Node) stack.peek()).addChildNode(new EchoNode(elementsArray));
                } else if (token.getValue().toString().equalsIgnoreCase("FOR")) {
                    ArrayIndexedCollection elements = new ArrayIndexedCollection();
                    while (true) {
                        token = lexer.nextToken();
                        if (elements.size() == 0 && token.getType() != SmartScriptTokenType.VARIABLE) {
                            throw new SmartScriptParserException(
                                "Expecting a variable on the first position in FOR tag at position " +
                                lexer.getCurrentIndex() + " but got " + token.getType() + "."
                            );
                        }
                        if (token.getType() == SmartScriptTokenType.END_TAG_DEF) {
                            lexer.setState(SmartScriptLexerState.TEXT);
                            break;
                        } else if (token.getType() == SmartScriptTokenType.VARIABLE ||
                                    token.getType() == SmartScriptTokenType.STRING ||
                                    token.getType() == SmartScriptTokenType.INTEGER ||
                                    token.getType() == SmartScriptTokenType.DOUBLE) {
                            elements.add(newElement(token));
                        } else {
                            throw new SmartScriptParserException("Invalid element type in FOR tag at position " + lexer.getCurrentIndex() + ".");
                        }
                    }
                    if (elements.size() != 3 && elements.size() != 4) {
                        throw new SmartScriptParserException("Invalid number of elements in FOR tag at position " + lexer.getCurrentIndex() +
                                                            ". Expected 3 or 4 elements but got " + elements.size() + ".");
                    }
                    Element[] elementsArray = new Element[4];
                    for (int i = 0; i < elements.size(); i++) {
                        elementsArray[i] = (Element) elements.get(i);
                    }
                    ForLoopNode forLoopNode = new ForLoopNode((ElementVariable) elementsArray[0], elementsArray[1], elementsArray[2], elementsArray[3]);
                    ((Node) stack.peek()).addChildNode(forLoopNode);
                    stack.push(forLoopNode);
                } else if (token.getValue().toString().equalsIgnoreCase("END")) {
                    stack.pop();
                    if (stack.size() == 0) {
                        throw new SmartScriptParserException("Too many END tags.");
                    }
                } else {
                    throw new SmartScriptParserException("Invalid tag name at position " + lexer.getCurrentIndex() + ": " + token.getValue() + ".");
                }
            } else if (token.getType() == SmartScriptTokenType.END_TAG_DEF) {
                lexer.setState(SmartScriptLexerState.TEXT);
            } else {
                throw new SmartScriptParserException("Invalid token type at position " + lexer.getCurrentIndex() + ": " + token.getType() + ".");
            }
        }
        if (stack.size() != 1) {
            throw new SmartScriptParserException("Too few END tags.");
        }
    }

    private Element newElement(SmartScriptToken token) {
        if (token.getType() == SmartScriptTokenType.VARIABLE) {
            return new ElementVariable((String) token.getValue());
        } else if (token.getType() == SmartScriptTokenType.STRING) {
            return new ElementString((String) token.getValue());
        } else if (token.getType() == SmartScriptTokenType.INTEGER) {
            return new ElementConstantInteger((int) token.getValue());
        } else if (token.getType() == SmartScriptTokenType.DOUBLE) {
            return new ElementConstantDouble((double) token.getValue());
        } else if (token.getType() == SmartScriptTokenType.FUNCTION) {
            return new ElementFunction((String) token.getValue());
        } else if (token.getType() == SmartScriptTokenType.OPERATOR) {
            return new ElementOperator((String) token.getValue());
        } else {
            throw new SmartScriptParserException("Invalid element type.");
        }
    }
}
