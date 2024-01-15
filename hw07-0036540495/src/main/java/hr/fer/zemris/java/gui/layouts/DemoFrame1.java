package hr.fer.zemris.java.gui.layouts;

import javax.swing.*;
import java.awt.*;

/**
 * This program demonstrates the {@link CalcLayout} class.
 * It creates a window with a {@link CalcLayout} layout manager
 * and adds some components to it.
 * <p>
 * The program accepts no command-line arguments.
 * </p>
 *
 * @see CalcLayout
 * @see JFrame
 * @see JLabel
 * @see SwingUtilities
 *
 * @author marcupic
 */
public class DemoFrame1 extends JFrame {
    /**
     * Constructs a new {@link DemoFrame1} object.
     */
    public DemoFrame1() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //setSize(500, 500);
        initGUI();
        pack();
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(3));
        cp.add(l("tekst 1"), new RCPosition(1,1));
        cp.add(l("tekst 2"), new RCPosition(2,3));
        cp.add(l("tekst stvarno najdulji"), new RCPosition(2,7));
        cp.add(l("tekst kraći"), new RCPosition(4,2));
        cp.add(l("tekst srednji"), new RCPosition(4,5));
        cp.add(l("tekst"), new RCPosition(4,7));
    }

    /**
     * Creates a new {@link JLabel} object with the given text.
     *
     * @param text the text
     * @return the new {@link JLabel} object
     */
    private JLabel l(String text) {
        JLabel l = new JLabel(text);
        l.setBackground(Color.YELLOW);
        l.setOpaque(true);
        return l;
    }

    /**
     * The main method. Creates a new {@link DemoFrame1} object and sets it visible.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DemoFrame1().setVisible(true));
    }
}
