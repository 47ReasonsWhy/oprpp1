package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexTest {
    @Test
    public void testModule() {
        Complex c = new Complex(3, 4);
        assertEquals(5, c.module(), 1e-5);
    }

    @Test
    public void testMultiply() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, 6);
        Complex c3 = c1.multiply(c2);
        assertEquals(-9, c3.getRe(), 1e-5);
        assertEquals(38, c3.getIm(), 1e-5);
    }

    @Test
    public void testDivide() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, 6);
        Complex c3 = c1.divide(c2);
        assertEquals(39.0/61, c3.getRe(), 1e-5);
        assertEquals(2.0/61, c3.getIm(), 1e-5);
    }

    @Test
    public void testAdd() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, 6);
        Complex c3 = c1.add(c2);
        assertEquals(8, c3.getRe(), 1e-5);
        assertEquals(10, c3.getIm(), 1e-5);
    }

    @Test
    public void testSub() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, 6);
        Complex c3 = c1.sub(c2);
        assertEquals(-2, c3.getRe(), 1e-5);
        assertEquals(-2, c3.getIm(), 1e-5);
    }

    @Test
    public void testNegate() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = c1.negate();
        assertEquals(-3, c2.getRe(), 1e-5);
        assertEquals(-4, c2.getIm(), 1e-5);
    }

    @Test
    public void testPower() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = c1.power(3);
        assertEquals(-117, c2.getRe(), 1e-5);
        assertEquals(44, c2.getIm(), 1e-5);

        Complex c3 = c1.power(0);
        assertEquals(1, c3.getRe(), 1e-5);
        assertEquals(0, c3.getIm(), 1e-5);

        Complex c4 = new Complex(4, -7);
        Complex c5 = c4.power(3);
        assertEquals(-524, c5.getRe(), 1e-5);
        assertEquals(7, c5.getIm(), 1e-5);

        Complex c6 = c4.power(7);
        assertEquals(1046756, c6.getRe(), 1e-5);
        assertEquals(-1951033, c6.getIm(), 1e-5);
    }

    @Test
    public void testRoot() {
        Complex c1 = new Complex(3, 4);
        List<Complex> roots = c1.root(3);
        assertEquals(3, roots.size());
        for (Complex root : roots) {
            assertEquals(0, root.power(3).sub(c1).module(), 1e-5);
        }
        assertEquals(1.62894, roots.get(0).getRe(), 1e-5);
        assertEquals(0.52017, roots.get(0).getIm(), 1e-5);
        assertEquals(-1.26495, roots.get(1).getRe(), 1e-5);
        assertEquals(1.15061, roots.get(1).getIm(), 1e-5);
        assertEquals(-0.36398, roots.get(2).getRe(), 1e-5);
        assertEquals(-1.67079, roots.get(2).getIm(), 1e-5);
    }


    @Test
    public void testParseComplex() {
        String[] input = new String[] {
                "0", "0.0", "-0.0",
                "1.0", "i", "-1", "-i",
                "1.0+i", "1.0- i", "-1.0 +  i", "  -1.0   - i ",
                " 2 +  i2  ", "  4   -  i  7.0 "
        };
        Complex[] expected = new Complex[] {
                new Complex(0, 0), new Complex(0, 0), new Complex(0, 0),
                new Complex(1, 0), new Complex(0, 1), new Complex(-1, 0), new Complex(0, -1),
                new Complex(1, 1), new Complex(1, -1), new Complex(-1, 1), new Complex(-1, -1),
                new Complex(2, 2), new Complex(4, -7)
        };
        for (int i = 0; i < input.length; i++) {
            Complex actual = Complex.parseComplex(input[i].strip());
            assertEquals(expected[i].getRe(), actual.getRe(), 1e-5);
            assertEquals(expected[i].getIm(), actual.getIm(), 1e-5);
        }

        assertThrows(IllegalArgumentException.class, () -> Complex.parseComplex(""));
    }
}
