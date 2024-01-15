package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * A simple demonstration program for the {@link BarChartComponent} class.
 * The program expects a single command-line argument: path to the file with bar chart description.
 * The file must contain 6 lines:
 * <ul>
 *     <li>Line 1: x-axis description</li>
 *     <li>Line 2: y-axis description</li>
 *     <li>Line 3: XYValues separated by space, each XYValue separated by comma</li>
 *     <li>Line 4: y-axis minimum</li>
 *     <li>Line 5: y-axis maximum</li>
 *     <li>Line 6: y-axis gap</li>
 * </ul>
 * The program will display the bar chart described in the file, and above it the given path.
 * <p>
 * Example of a valid file:
 * <pre>
 * Number of people in the car
 * Frequency
 * 1,8 2,20 3,22 4,10 5,4
 * 0
 * 22
 * 2
 * </pre>
 * The program will exit if the file is not valid.
 */
public class BarChartDemo extends JFrame {
    /**
     * Program entry point.
     *
     * @param args command-line arguments
     * @throws IllegalArgumentException if the file is not valid
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Expected 1 argument: path to the file with bar chart description.");
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            BarChart barChart = null;
            try {
                barChart = parse(args[0]);
            } catch (Exception e) {
                System.out.println("Error while parsing the file.");
                System.exit(1);
            }
            new BarChartDemo(barChart, args[0]).setVisible(true);
        });
    }

    /**
     * Initializes a new {@link BarChartDemo} object.
     *
     * @param barChart a {@link BarChart} object
     * @param path path to the file with bar chart description
     */
    private BarChartDemo(BarChart barChart, String path) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BarChartDemo");
        setLocation(20, 20);
        setSize(500, 500);
        initGUI(barChart, path);
    }

    /**
     * Initializes the GUI.
     *
     * @param barChart a {@link BarChart} object
     * @param path path to the file with bar chart description
     */
    private void initGUI(BarChart barChart, String path) {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JLabel label = new JLabel(path);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(label, BorderLayout.PAGE_START);

        BarChartComponent barChartComponent = new BarChartComponent(barChart);
        cp.add(barChartComponent, BorderLayout.CENTER);
    }

    /**
     * Parses the file with the given path and returns a {@link BarChart} object,
     * as described in {@link BarChartDemo}.
     *
     * @param path path to the file with bar chart description
     * @return a {@link BarChart} object
     * @throws IllegalArgumentException if the file is not valid
     */
    private static BarChart parse(String path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Path.of(path));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while reading the file.");
        }

        if (lines.size() < 6) {
            throw new IllegalArgumentException("Expected at least 6 lines in the file.");
        }

        String xDescription = lines.get(0);
        String yDescription = lines.get(1);

        String[] values = lines.get(2).split("\\s+");
        XYValue[] valuesInt = new XYValue[values.length];
        for (int i = 0; i < values.length; i++) {
            String[] value = values[i].split(",");
            if (value.length != 2) {
                throw new IllegalArgumentException("Expected 2 values separated by comma per XYValue in line 3.");
            }
            valuesInt[i] = new XYValue(Integer.parseInt(value[0]), Integer.parseInt(value[1]));
        }

        int yMin = Integer.parseInt(lines.get(3));
        int yMax = Integer.parseInt(lines.get(4));
        int yGap = Integer.parseInt(lines.get(5));

        return new BarChart(valuesInt, xDescription, yDescription, yMin, yMax, yGap);
    }
}
