package hr.fer.zemris.java.gui.calc.components;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.CalcModelImpl;

/**
 * This class represents a button that performs a unary operation on the current value of the calculator.
 * The button has two states, which are toggled by calling the {@link UnaryOperationButton#toggle()} method.
 * The button's text and action listener change accordingly.
 *
 * @see BinaryOperationButton
 * @see SimpleButton
 *
 * @see CalcModelImpl
 * @see hr.fer.zemris.java.gui.calc.Calculator
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class UnaryOperationButton extends JButton {
    /**
     * Button text.
     */
    private final String[] text;

    /**
     * Button action listeners.
     */
    private final ActionListener[] actionListener;

    /**
     * Current button state.
     */
    private int index;

    /**
     * Constructs a new {@link UnaryOperationButton} with the given text.
     *
     * @param text button text
     */
    private UnaryOperationButton(String text) {
        super(text);
        this.text = new String[2];
        this.actionListener = new ActionListener[2];
        this.index = 0;
    }

    /**
     * Constructs a new {@link UnaryOperationButton} with the given texts and action listeners.
     *
     * @param text1           first button text
     * @param actionListener1 first button action listener
     * @param text2           second button text
     * @param actionListener2 second button action listener
     *
     * @return constructed button
     */
    public static UnaryOperationButton of(String text1, ActionListener actionListener1,
                                          String text2, ActionListener actionListener2) {
        UnaryOperationButton button = new UnaryOperationButton(text1);
        button.text[0] = text1;
        button.text[1] = text2;
        button.actionListener[0] = actionListener1;
        button.actionListener[1] = actionListener2;
        button.addActionListener(actionListener1);
        return button;
    }

    /**
     * Toggles the button state.
     */
    public void toggle() {
        index = 1 - index;
        setText(text[index]);
        removeActionListener(actionListener[1 - index]);
        addActionListener(actionListener[index]);
    }

    /**
     * Constructs a new action listener that performs the given unary operation on the current value of the calculator.
     *
     * @param model calculator model
     * @param op    unary operation
     *
     * @return constructed action listener
     */
    public static ActionListener UnaryOperationListener(CalcModelImpl model, DoubleUnaryOperator op) {
        return e -> model.setValue(op.applyAsDouble(model.getValue()));
    }
}
