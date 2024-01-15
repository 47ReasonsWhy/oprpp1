package hr.fer.zemris.java.gui.charts;

/**
 * This record represents a bar chart used in {@link BarChartComponent}.
 * It contains an array of {@link XYValue} objects, a description of the x-axis, a description of the y-axis,
 * the minimum value of the y-axis, the maximum value of the y-axis and the gap between two consecutive values on the y-axis.
 * If the difference between the maximum and minimum value of the y-axis is not divisible by the gap,
 * the maximum value of the y-axis is increased to the next value that satisfies this condition.
 *
 * @see XYValue
 *
 * @see BarChartComponent
 * @see BarChartDemo
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public record BarChart(XYValue[] values, String xDescription, String yDescription, int yMin, int yMax, int yGap) {
    public BarChart {
        if (yGap <= 0) {
            throw new IllegalArgumentException("yGap must be positive.");
        }
        if (yMax <= yMin) {
            throw new IllegalArgumentException("yMax must be greater than yMin.");
        }
        if (yMin < 0) {
            throw new IllegalArgumentException("yMin must be non-negative.");
        }
        for (XYValue value : values) {
            if (value.y() < yMin) {
                throw new IllegalArgumentException("yMin must be less than or equal to y value of every given XYValue.");
            }
        }
    }
}
