package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.CalcModelImpl;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;

/**
 * This class represents a button that performs a binary operation on the current value of the calculator.
 *
 * @see UnaryOperationButton
 * @see SimpleButton
 *
 * @see CalcModelImpl
 * @see hr.fer.zemris.java.gui.calc.Calculator
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class BinaryOperationButton extends JButton {
    /**
     * Constructs a new {@link BinaryOperationButton} with the given text.
     *
     * @param text button text
     */
    private BinaryOperationButton(String text) {
        super(text);
    }

    /**
     * Factory method that constructs a new {@link BinaryOperationButton} with the given text and operation.
     *
     * @param text button text
     * @param op operation to be performed
     * @param model calculator model
     *
     * @return new {@link BinaryOperationButton}
     */
    public static BinaryOperationButton of(String text, DoubleBinaryOperator op, CalcModelImpl model) {
        BinaryOperationButton button = new BinaryOperationButton(text);
        button.addActionListener(BinaryOperationListener(op, model));
        return button;
    }

    /**
     * Returns a new {@link ActionListener} that performs the given operation on the current value of the calculator
     * and the active operand.
     *
     * @param op operation to be performed
     * @param model calculator model
     *
     * @return new {@link ActionListener}
     */
    public static ActionListener BinaryOperationListener(DoubleBinaryOperator op, CalcModelImpl model) {
        return e -> {
            Double newValue;
            String valueToFreeze;
            if (model.isActiveOperandSet()) {
                newValue = model.getPendingBinaryOperation().applyAsDouble(
                        model.getActiveOperand(), model.getValue()
                );
                valueToFreeze = String.valueOf(newValue);
            } else {
                newValue = model.getValue();
                valueToFreeze = model.toString();
            }
            model.setPendingBinaryOperation(op);
            model.setActiveOperand(newValue);
            model.clear();
            model.freezeValue(valueToFreeze);
        };
    }
}
