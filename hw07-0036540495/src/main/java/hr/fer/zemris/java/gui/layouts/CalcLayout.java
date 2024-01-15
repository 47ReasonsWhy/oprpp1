package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A layout manager that arranges components in a grid with 5 rows and 7 columns.
 * Each component takes up one cell in the grid, except for the component in the
 * first row and first column, which takes up 5 columns.
 *
 * @see RCPosition
 * @see CalcLayoutException
 * @see DemoFrame1
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class CalcLayout implements LayoutManager2 {
    /**
     * The number of rows in the grid.
     */
    private final int rows = 5;

    /**
     * The number of columns in the grid.
     */
    private final int columns = 7;

    /**
     * The gap between components.
     */
    private final int gap;

    /**
     * A map of components in the grid.
     * The key is the row number and the value is a map of components in that row.
     * The key of the inner map is the column number and the value is the component.
     */
    private final Map<Integer, Map<Integer, Component>> components = new HashMap<>();


    /**
     * Creates a new {@code CalcLayout} with a gap of 0.
     */
    public CalcLayout() {
        this(0);
    }

    /**
     * Creates a new {@code CalcLayout} with the given gap.
     *
     * @param gap the gap between components
     * @throws IllegalArgumentException if the gap is a negative integer
     */
    public CalcLayout(int gap) {
        if (gap < 0) {
            throw new IllegalArgumentException("Gap must be a non-negative integer.");
        }
        this.gap = gap;
        for (int i = 0; i < rows; i++) {
            components.put(i, new HashMap<>());
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateLayoutSize(parent, Component::getPreferredSize, Math::max);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateLayoutSize(parent, Component::getMinimumSize, Math::max);
    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        return calculateLayoutSize(parent, Component::getMaximumSize, Math::min);
    }

    /**
     * Helper function for calculating the preferred, minimum or maximum size of the layout.
     *
     * @param parent the parent container
     * @param sizeFunction the function that returns the size of the component
     * @param minOrMax the function that returns the minimum or maximum of two integers
     *
     * @return the preferred, minimum or maximum size of the layout
     */
    private Dimension calculateLayoutSize(Container parent,
                                          Function<Component, Dimension> sizeFunction,
                                          BiFunction<Integer, Integer, Integer> minOrMax) {
        int rowHeight = 0;
        int columnWidth = 0;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                if (i == 1 && java.util.List.of(2, 3, 4, 5).contains(j)) {
                    continue;
                }
                Component component = components.get(i-1).get(j-1);
                if (component == null) {
                    continue;
                }
                Dimension maximumSize = sizeFunction.apply(component);
                if (maximumSize == null) {
                    continue;
                }
                rowHeight = minOrMax.apply(rowHeight, maximumSize.height);
                if (i == 1 && j == 1) {
                    // the component takes up 1 row and 5 columns
                    columnWidth = minOrMax.apply(columnWidth, (maximumSize.width - 4 * gap) / 5);
                } else {
                    columnWidth = minOrMax.apply(columnWidth, maximumSize.width);
                }
            }
        }
        Insets insets = parent.getInsets();
        int width = insets.left + insets.right + (columns - 1) * gap + columns * columnWidth;
        int height = insets.top + insets.bottom + (rows - 1) * gap + rows * rowHeight;
        return new Dimension(width, height);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("We do not want anyone calling this method.");
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        RCPosition position;

        if (constraints instanceof RCPosition) {
            position = (RCPosition) constraints;
        } else if (constraints instanceof String) {
            position = RCPosition.parse((String) constraints);
        } else {
            throw new IllegalArgumentException("Constraints must be an instance of RCPosition or String.");
        }

        if (position.row() < 1 || position.row() > rows) {
            throw new CalcLayoutException("Row must be between 1 and 5.");
        }
        if (position.column() < 1 || position.column() > columns) {
            throw new CalcLayoutException("Column must be between 1 and 7.");
        }
        if (position.row() == 1 && java.util.List.of(2, 3, 4, 5).contains(position.column())) {
            throw new CalcLayoutException("Components cannot be added to the first row in columns 2-5.");
        }
        if (components.get(position.row() - 1).get(position.column() - 1) != null) {
            throw new CalcLayoutException("There is already a component at the given position.");
        }

        components.get(position.row() - 1).put(position.column() - 1, comp);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        for (int i = 0; i < rows; i++) {
            Map<Integer, Component> row = components.get(i);
            for (int j = 0; j < columns; j++) {
                if (row.get(j) == comp) {
                    row.remove(j);
                    return;
                }
            }
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int width = parent.getWidth() - insets.left - insets.right;
        int height = parent.getHeight() - insets.top - insets.bottom;

        double rowHeight = 1.0 * (height - (rows - 1) * gap) / rows;
        double columnWidth = 1.0 * (width - (columns - 1) * gap) / columns;

        for (int i = 0; i < rows; i++) {
            Map<Integer, Component> row = components.get(i);
            for (int j = 0; j < columns; j++) {
                Component component = row.get(j);
                if (component == null) {
                    continue;
                }
                int x = insets.left + j * (int) Math.round(columnWidth + gap);
                int y = insets.top + i * (int) Math.round(rowHeight + gap);
                int w = (int) Math.round(columnWidth);
                int h = (int) Math.round(rowHeight);
                if (i == 0 && j == 0) {
                    // the component takes up 1 row and 5 columns
                    w = (int) Math.round(5 * columnWidth + 4 * gap);
                }
                // we know that there won't be any components in the first row in columns 2-5
                // because we don't allow that in addLayoutComponent
                component.setBounds(x, y, w, h);
            }
        }
    }

    @Override
    public void invalidateLayout(Container target) {
        // do nothing
    }
}
