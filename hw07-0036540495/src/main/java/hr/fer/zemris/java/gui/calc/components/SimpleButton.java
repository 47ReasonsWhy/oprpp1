package hr.fer.zemris.java.gui.calc.components;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * A simple button with text and an action listener used in the calculator.
 * <p>
 * The buttons are created using the {@link #of(String, ActionListener)} factory method.
 *
 * @see BinaryOperationButton
 * @see UnaryOperationButton
 *
 * @see hr.fer.zemris.java.gui.calc.Calculator
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class SimpleButton extends JButton {
    /**
     * Constructs a new simple button with the given text.
     *
     * @param text text of the button
     */
    private SimpleButton(String text) {
        super(text);
    }

    /**
     * Factory method that creates a new simple button with the given text and action listener.
     *
     * @param text text of the button
     * @param listener action listener of the button
     * @return new {@link SimpleButton}
     */
    public static SimpleButton of(String text, ActionListener listener) {
        SimpleButton button = new SimpleButton(text);
        button.addActionListener(listener);
        return button;
    }
}
