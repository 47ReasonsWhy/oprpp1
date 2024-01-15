package hr.fer.zemris.java.gui.calc.components;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

/**
 * A simple display component for the calculator.
 * It displays the current value of the calculator.
 *
 * @see JLabel
 * @see hr.fer.zemris.java.gui.calc.CalcModelImpl
 * @see hr.fer.zemris.java.gui.calc.Calculator
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Display extends JLabel {
    /**
     * Constructs a new display component.
     */
    public Display() {
        super("0", SwingConstants.RIGHT);
        setOpaque(true);
        setBackground(Color.YELLOW);
        setFont(getFont().deriveFont(30f));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    /**
     * Sets the value of the display.
     *
     * @param value value to be set
     */
    public void setValue(String value) {
        setText(value);
    }
}
