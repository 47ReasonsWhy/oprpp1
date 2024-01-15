package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.components.BinaryOperationButton;
import hr.fer.zemris.java.gui.calc.components.Display;
import hr.fer.zemris.java.gui.calc.components.SimpleButton;
import hr.fer.zemris.java.gui.calc.components.UnaryOperationButton;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import static hr.fer.zemris.java.gui.calc.components.BinaryOperationButton.BinaryOperationListener;
import static hr.fer.zemris.java.gui.calc.components.UnaryOperationButton.UnaryOperationListener;

/**
 * A simple calculator application that supports basic arithmetic operations,
 * as well as some unary operations. It also supports storing values on a stack.
 * <p>
 * The following operations are provided:
 * <ul>
 * <li> +, -, *, / </li>
 * <li> 1/x, log, ln, x^n, x^(1/n) </li>
 * <li> sin, cos, tan, ctg, arcsin, arccos, arctan, arcctg </li>
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Calculator extends JFrame {
    /**
     * Associated calculator model.
     */
    private final CalcModelImpl model = new CalcModelImpl();

    /**
     * Stack used for storing values.
     */
    private final java.util.List<String> stack = new LinkedList<>();

    /**
     * Constructs a new calculator frame.
     */
    public Calculator() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(5));

        Display display = new Display();
        model.addCalcValueListener(model -> display.setValue(model.toString()));
        cp.add(display, "1,1");


        String[] digitCoords = new String[]{
                "5,3", "4,3", "4,4", "4,5", "3,3", "3,4", "3,5", "2,3", "2,4", "2,5"
        };
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            SimpleButton digit = SimpleButton.of(String.valueOf(i), e -> {
                if (!model.isEditable()) {
                    model.clear();
                }
                model.insertDigit(finalI);
            });
            cp.add(digit, digitCoords[i]);
        }


        SimpleButton equals = SimpleButton.of("=", e -> {
            if (model.getPendingBinaryOperation() == null) {
                return;
            }
            model.setValue(model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue()));
            model.setPendingBinaryOperation(null);
            model.clearActiveOperand();
        });
        cp.add(equals, "1,6");

        SimpleButton clr = SimpleButton.of("clr", e -> model.clear());
        cp.add(clr, "1,7");

        SimpleButton reset = SimpleButton.of("reset", e -> model.clearAll());
        cp.add(reset, "2,7");

        SimpleButton push = SimpleButton.of("push", e -> stack.add(0, model.toString()));
        cp.add(push, "3,7");

        SimpleButton pop = SimpleButton.of("pop", e -> {
            if (stack.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Stack is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            model.setValue(Double.parseDouble(stack.remove(0)));
        });
        cp.add(pop, "4,7");



        SimpleButton reciprocal = SimpleButton.of("1/x", UnaryOperationListener(model, x -> 1 / x));
        cp.add(reciprocal, "2,1");

        UnaryOperationButton log = UnaryOperationButton.of(
                "log", UnaryOperationListener(model, Math::log10),
                "10^x", UnaryOperationListener(model, x -> Math.pow(10, x))
        );
        cp.add(log, "3,1");

        UnaryOperationButton ln = UnaryOperationButton.of(
                "ln", UnaryOperationListener(model, Math::log),
                "e^x", UnaryOperationListener(model, Math::exp)
        );
        cp.add(ln, "4,1");

        UnaryOperationButton power = UnaryOperationButton.of(
                "x^n", BinaryOperationListener(Math::pow, model),
                "x^(1/n)", BinaryOperationListener((x, y) -> Math.pow(x, 1 / y), model)
        );
        cp.add(power, "5,1");

        UnaryOperationButton sin = UnaryOperationButton.of(
                "sin", UnaryOperationListener(model, Math::sin),
                "arcsin", UnaryOperationListener(model, Math::asin)
        );
        cp.add(sin, "2,2");

        UnaryOperationButton cos = UnaryOperationButton.of(
                "cos", UnaryOperationListener(model, Math::cos),
                "arccos", UnaryOperationListener(model, Math::acos)
        );
        cp.add(cos, "3,2");

        UnaryOperationButton tan = UnaryOperationButton.of(
                "tan", UnaryOperationListener(model, Math::tan),
                "arctan", UnaryOperationListener(model, Math::atan)
        );
        cp.add(tan, "4,2");

        UnaryOperationButton ctg = UnaryOperationButton.of(
                "ctg", UnaryOperationListener(model, x -> 1 / Math.tan(x)),
                "arcctg", UnaryOperationListener(model, x -> Math.PI / 2 - Math.atan(x))
        );
        cp.add(ctg, "5,2");

        SimpleButton plusMinus = SimpleButton.of("+/-", e -> {
            if (!model.isEditable()) {
                model.clear();
            }
            model.swapSign();
        });
        cp.add(plusMinus, "5,4");

        SimpleButton dot = SimpleButton.of(".", e -> {
            if (!model.isEditable()) {
                model.clear();
            }
            try {
                model.insertDecimalPoint();
            } catch (CalculatorInputException ex) {
                if (ex.getMessage().equals("Input is empty.")) {
                    model.insertDigit(0);
                    model.insertDecimalPoint();
                } else {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cp.add(dot, "5,5");

        BinaryOperationButton div = BinaryOperationButton.of("/", (x, y) -> x / y, model);
        cp.add(div, "2,6");

        BinaryOperationButton mul = BinaryOperationButton.of("*", (x, y) -> x * y, model);
        cp.add(mul, "3,6");

        BinaryOperationButton minus = BinaryOperationButton.of("-", (x, y) -> x - y, model);
        cp.add(minus, "4,6");

        BinaryOperationButton plus = BinaryOperationButton.of("+", Double::sum, model);
        cp.add(plus, "5,6");


        JCheckBox inv = new JCheckBox("Inv");
        inv.addActionListener(e -> {
            log.toggle();
            ln.toggle();
            power.toggle();
            sin.toggle();
            cos.toggle();
            tan.toggle();
            ctg.toggle();
        });
        cp.add(inv, "5,7");
    }

    /**
     * Program entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
    }
}
