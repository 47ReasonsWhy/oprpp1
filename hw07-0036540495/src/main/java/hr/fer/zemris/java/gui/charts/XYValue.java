package hr.fer.zemris.java.gui.charts;

/**
 * This record represents a pair of integer values (x, y).
 * It is used in {@link BarChart} to represent a single bar.
 *
 * @see BarChart
 * @see BarChartComponent
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public record XYValue(int x, int y) {
}
