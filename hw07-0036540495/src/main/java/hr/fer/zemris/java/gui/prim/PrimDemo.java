package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a demo for the {@link PrimListModel} class.
 * It has two lists of prime numbers and a button for generating the next prime number.
 * The lists are synchronized, so the next prime number is added to both lists.
 *
 * @see PrimListModel
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class PrimDemo extends JFrame {
    /**
     * Associated {@link PrimListModel} object.
     */
    private final PrimListModel primListModel;

    /**
     * Constructs a new {@link PrimDemo} object.
     */
    public PrimDemo() {
        primListModel = new PrimListModel();

        JList<Integer> list1 = new JList<>(primListModel);
        JList<Integer> list2 = new JList<>(primListModel);

        JButton nextButton = new JButton("Sljedeći");
        nextButton.addActionListener(e -> primListModel.next());

        JScrollPane scrollPane1 = new JScrollPane(list1);
        JScrollPane scrollPane2 = new JScrollPane(list2);
        JPanel listsPanel = new JPanel(new GridLayout(1, 2));
        listsPanel.add(scrollPane1);
        listsPanel.add(scrollPane2);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(nextButton, BorderLayout.SOUTH);
        panel.add(listsPanel, BorderLayout.CENTER);

        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Starting point of the program. Expects no command-line arguments.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PrimDemo::new);
    }
}
