package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A component that draws a {@link BarChart} on the screen.
 *
 * @see BarChart
 * @see BarChartDemo
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class BarChartComponent extends JComponent {
    /**
     * The associated bar chart.
     */
    private final BarChart barChart;

    /**
     * Creates a new {@code BarChartComponent} with the given bar chart.
     *
     * @param barChart the associated bar chart
     */
    public BarChartComponent(BarChart barChart) {
        this.barChart = barChart;
    }

    /**
     * Paints the {@code BarChartComponent}.
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        setFont(new Font("SansSerif", Font.BOLD, 12));

        Insets insets = getInsets();
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;
        int shadeWidth = 5;

        int yMax = barChart.yMax() % barChart.yGap() == 0 ? barChart.yMax() : barChart.yMax() + barChart.yGap() - barChart.yMax() % barChart.yGap();

        int xStartGrid = insets.left + 25 + g2d.getFontMetrics().getHeight() + g2d.getFontMetrics().stringWidth(String.valueOf(yMax));
        int xEndGrid = insets.left + width - 20;
        int yStartGrid = insets.top + 20;
        int yEndGrid = insets.top + height - g2d.getFontMetrics().getHeight() - 30;

        int xGap = (xEndGrid - xStartGrid) / barChart.values().length;
        int yGap = (yEndGrid - yStartGrid) * barChart.yGap() / (yMax - barChart.yMin());

        // y-axis description
        AffineTransform at = new AffineTransform(g2d.getTransform());
        at.rotate(-Math.PI/2);
        g2d.setTransform(at);
        g2d.drawString(
                barChart.yDescription(),
                -insets.top - height/2 - g2d.getFontMetrics().stringWidth(barChart.yDescription())/2,
                insets.left + 20
        );
        g2d.setTransform(((Graphics2D) g).getTransform());

        // x-axis description
        g2d.drawString(
                barChart.xDescription(),
                insets.left + width/2 - g2d.getFontMetrics().stringWidth(barChart.xDescription())/2 + xStartGrid/2,
                insets.top + height - g2d.getFontMetrics().getHeight()/2
        );

        // shade
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < barChart.values().length; i++) {
            int rectX = xStartGrid + i * xGap + xGap / 2 - xGap / 2;
            int rectY = yEndGrid - barChart.values()[i].y() * yGap / barChart.yGap();
            int barHeight = barChart.values()[i].y() * yGap / barChart.yGap();
            if (i == barChart.values().length - 1) {
                g2d.fillRect(rectX + xGap + 1, rectY + shadeWidth, shadeWidth, barHeight - shadeWidth);
            } else {
                int shadeHeight = (yEndGrid - yStartGrid) *
                        (barChart.values()[i].y() - barChart.values()[i + 1].y()) /
                        (barChart.yMax() - barChart.yMin());
                if (shadeHeight > 0) {
                    g2d.fillRect(rectX + xGap + 1, rectY + shadeWidth, shadeWidth, shadeHeight - shadeWidth);
                }
            }
        }
        g2d.setColor(Color.BLACK);

        // y-axis values
        for (int i = 0; i <= (yMax - barChart.yMin()) / barChart.yGap(); i++) {
            if (i != 0) {
                g2d.setColor(Color.decode("#FCAE1E"));
                g2d.drawLine(xStartGrid, yEndGrid - i * yGap, xEndGrid + 8, yEndGrid - i * yGap);
                g2d.setColor(Color.BLACK);
            }
            g2d.drawLine(xStartGrid - 5, yEndGrid - i * yGap, xStartGrid, yEndGrid - i * yGap);
            g2d.drawString(
                    String.valueOf(barChart.yMin() + i * barChart.yGap()),
                    xStartGrid - 7 - g2d.getFontMetrics().stringWidth(String.valueOf(barChart.yMin() + i * barChart.yGap())),
                    yEndGrid - i * yGap + (g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent())/2
            );
        }

        // x-axis values and bars
        g2d.drawLine(xStartGrid, yEndGrid, xStartGrid, yEndGrid + 5);
        for (int i = 0; i < barChart.values().length; i++) {
            // x-axis value
            g2d.setColor(Color.decode("#FCAE1E"));
            g2d.drawLine(xStartGrid + (i + 1) * xGap, yEndGrid, xStartGrid + (i + 1) * xGap, yStartGrid - 8);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(xStartGrid + (i + 1) * xGap, yEndGrid, xStartGrid + (i + 1) * xGap, yEndGrid + 5);
            g2d.drawString(
                    String.valueOf(barChart.values()[i].x()),
                    xStartGrid + i * xGap + xGap/2 - g2d.getFontMetrics().stringWidth(String.valueOf(barChart.values()[i].x()))/2,
                    yEndGrid + 5 + g2d.getFontMetrics().getHeight()
            );

            // bar
            int rectX = xStartGrid + i * xGap + xGap / 2 - xGap / 2;
            int rectY = yEndGrid - barChart.values()[i].y() * yGap / barChart.yGap();
            int barHeight = barChart.values()[i].y() * yGap / barChart.yGap();
            g2d.setColor(Color.decode("#FF4F00"));
            g2d.fillRect(rectX, rectY, xGap, barHeight);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(rectX, rectY, xGap, barHeight);
            g2d.setColor(Color.BLACK);
        }

        // y-axis
        g2d.drawLine(xStartGrid, yStartGrid - 15, xStartGrid, yEndGrid);
        g2d.fillPolygon(
                new int[]{xStartGrid - 4, xStartGrid + 4, xStartGrid},
                new int[]{yStartGrid - 10, yStartGrid - 10, yStartGrid - 17},
                3
        );
        // x-axis
        g2d.drawLine(xStartGrid, yEndGrid, xEndGrid + 15, yEndGrid);
        g2d.fillPolygon(
                new int[]{xEndGrid + 10, xEndGrid + 10, xEndGrid + 17},
                new int[]{yEndGrid - 4, yEndGrid + 4, yEndGrid},
                3
        );

        g2d.dispose();
    }
}
