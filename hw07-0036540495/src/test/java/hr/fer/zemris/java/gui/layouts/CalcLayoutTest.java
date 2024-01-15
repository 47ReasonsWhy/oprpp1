package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CalcLayoutTest {
    @Test
    public void testPreferredLayoutSize() {
        JPanel p1 = new JPanel(new CalcLayout(2));
        JLabel l11 = new JLabel(""); l11.setPreferredSize(new Dimension(10,30));
        JLabel l12 = new JLabel(""); l12.setPreferredSize(new Dimension(20,15));
        p1.add(l11, new RCPosition(2,2));
        p1.add(l12, new RCPosition(3,3));
        Dimension dim1 = p1.getPreferredSize();
        assertEquals(152, dim1.width);
        assertEquals(158, dim1.height);

        JPanel p2 = new JPanel(new CalcLayout(2));
        JLabel l21 = new JLabel(""); l21.setPreferredSize(new Dimension(108,15));
        JLabel l22 = new JLabel(""); l22.setPreferredSize(new Dimension(16,30));
        p2.add(l21, new RCPosition(1,1));
        p2.add(l22, new RCPosition(3,3));
        Dimension dim2 = p2.getPreferredSize();
        assertEquals(152, dim2.width);
        assertEquals(158, dim2.height);
    }
}
