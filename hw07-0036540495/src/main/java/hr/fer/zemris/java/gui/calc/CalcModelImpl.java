package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementation of {@link CalcModel} interface used in {@link Calculator}.
 *
 * @see CalcModel
 * @see Calculator
 * @see CalcValueListener
 * @see CalculatorInputException
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class CalcModelImpl implements CalcModel {
    /**
     * List of listeners registered to this model.
     */
    private final List<CalcValueListener> listeners = new java.util.ArrayList<>();

    /**
     * Flag that determines if the value is editable.
     */
    private boolean editable;

    /**
     * Flag that determines if the value is positive.
     */
    private boolean positive;

    /**
     * String representation of the value.
     */
    private String input;

    /**
     * Value of the model.
     */
    private double value;

    /**
     * Frozen value of the model.
     */
    private String frozenValue;

    /**
     * Active operand of the model.
     */
    private Double activeOperand;

    /**
     * Pending operation of the model.
     */
    private DoubleBinaryOperator pendingOperation;

    /**
     * Default constructor.
     */
    public CalcModelImpl() {
        this.editable = true;
        this.positive = true;
        this.input = "";
        this.value = 0;
        this.frozenValue = null;
        this.activeOperand = null;
        this.pendingOperation = null;
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        listeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        listeners.remove(l);
    }

    @Override
    public String toString() {
        if (hasFrozenValue()) {
            return frozenValue;
        } else if (input.isEmpty()) {
            return (positive ? "" : "-") + "0";
        } else {
            return (positive ? "" : "-") + input;
        }
    }

    @Override
    public double getValue() {
        return positive ? value : -value;
    }

    @Override
    public void setValue(double value) {
        this.value = Math.abs(value);
        this.input = Double.toString(Math.abs(value));
        this.editable = false;
        this.positive = value >= 0;
        this.frozenValue = null;

        listeners.forEach(l -> l.valueChanged(this));
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void clear() {
        this.positive = true;
        this.input = "";
        this.value = 0;
        this.editable = true;

        listeners.forEach(l -> l.valueChanged(this));
    }

    @Override
    public void clearAll() {
        this.activeOperand = null;
        this.pendingOperation = null;

        clear();
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if (!editable) {
            throw new CalculatorInputException("Cannot swap sign if not editable.");
        }

        positive = !positive;
        frozenValue = null;

        listeners.forEach(l -> l.valueChanged(this));
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (!editable) {
            throw new CalculatorInputException("Cannot insert decimal point if not editable.");
        }
        if (input.contains(".")) {
            throw new CalculatorInputException("Input already contains decimal point.");
        }
        if (input.isEmpty()) {
            throw new CalculatorInputException("Input is empty.");
        }

        input += ".";
        frozenValue = null;

        listeners.forEach(l -> l.valueChanged(this));
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (!editable) {
            throw new CalculatorInputException("Cannot insert digit if not editable.");
        }

        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Digit must be between 0 and 9.");
        }

        if (input.equals("0")) {
            input = Integer.toString(digit);
        } else {
            input += digit;
        }

        double newValue = Double.parseDouble(input);
        if (newValue == Double.POSITIVE_INFINITY) {
            input = input.substring(0, input.length() - 1);
            throw new CalculatorInputException("Input overflows to positive infinity.");
        }
        if (newValue == Double.NEGATIVE_INFINITY) {
            input = input.substring(0, input.length() - 1);
            throw new CalculatorInputException("Input overflows to negative infinity.");
        }
        value = newValue;

        frozenValue = null;

        listeners.forEach(l -> l.valueChanged(this));
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperand != null;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (!isActiveOperandSet()) {
            throw new IllegalStateException("Active operand is not set.");
        }

        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
    }

    @Override
    public void clearActiveOperand() {
        this.activeOperand = null;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        this.pendingOperation = op;
    }

    /**
     * Freezes the value of the model.
     *
     * @param value value to be frozen
     */
    public void freezeValue(String value) {
        this.frozenValue = value;
        clear();
    }

    /**
     * Checks if the value is frozen.
     *
     * @return true if the value is frozen, false otherwise
     */
    public boolean hasFrozenValue() {
        return frozenValue != null;
    }
}
