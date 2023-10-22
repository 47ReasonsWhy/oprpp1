package hr.fer.oprpp1.custom.collections.demo;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackDemoTest {

    @Test
    void test0() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        StackDemo.main(new String[]{ "1 0 /" });

        System.setOut(stdout);

        assertEquals(
                "Cannot divide by 0.\n",
                outputStream.toString().replace("\r\n", "\n")
        );
    }

    @Test
    void test1() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        StackDemo.main(new String[]{ "-1 2 + 3 *" });

        System.setOut(stdout);

        assertEquals(
                "Expression evaluates to 3.\n",
                outputStream.toString().replace("\r\n", "\n")
        );
    }

    @Test
    void test2() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        StackDemo.main(new String[]{ "6 5 4 3 2 1 + / - * %" });

        System.setOut(stdout);

        assertEquals(
                "Expression evaluates to 6.\n",
                outputStream.toString().replace("\r\n", "\n")
        );
    }

    @Test
    void test3() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        StackDemo.main(new String[]{ "1 2 + 3 / 4 - 5 * 6 %" });

        System.setOut(stdout);

        assertEquals(
                "Expression evaluates to -3.\n",
                outputStream.toString().replace("\r\n", "\n")
        );
    }

    @Test
    void test4() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        StackDemo.main(new String[]{ "1 2 &" });

        System.setOut(stdout);

        assertEquals(
                "Invalid operator: &.\n",
                outputStream.toString().replace("\r\n", "\n")
        );
    }

    @Test
    void test5() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        StackDemo.main(new String[]{ "1 2 + 3 / 4 - 5 * 6 % 7" });

        System.setOut(stdout);

        assertEquals(
                "Invalid expression.\n",
                outputStream.toString().replace("\r\n", "\n")
        );
    }

    @Test
    void test6() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        StackDemo.main(new String[]{ "1", "2", "+", "3", "/", "4", "-", "5", "*", "6", "%" });

        System.setOut(stdout);

        assertEquals(
                "Expected 1 argument, got 11.\n",
                outputStream.toString().replace("\r\n", "\n")
        );
    }
}
