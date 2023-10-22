package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * A program which evaluates a mathematical expression in postfix notation.
 * The expression is given as a single command line argument with numbers and operators separated by whitespaces.
 */
public class StackDemo {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Expected 1 argument, got " + args.length + ".");
            return;
        }

        String[] elements = args[0].split("\\s+");

        ObjectStack stack = new ObjectStack();

        for (String element : elements) {
            if (element.matches("-?\\d+")) {
                stack.push(Integer.parseInt(element));
                continue;
            }
            int secondOperand = (int) stack.pop();
            int firstOperand = (int) stack.pop();
            int result = 0;

            switch (element) {
                case "+" -> result = firstOperand + secondOperand;
                case "-" -> result = firstOperand - secondOperand;
                case "*" -> result = firstOperand * secondOperand;
                case "/" -> {
                    if (secondOperand == 0) {
                        System.out.println("Cannot divide by 0.");
                        return;
                    }
                    result = firstOperand / secondOperand;
                }
                case "%" -> result = firstOperand % secondOperand;
                default -> {
                    System.out.println("Invalid operator: " + element + ".");
                    return;
                }
            }

            stack.push(result);
        }

        if (stack.size() != 1) {
            System.out.println("Invalid expression.");
            return;
        }

        System.out.println("Expression evaluates to " + stack.pop() + ".");
    }
}
