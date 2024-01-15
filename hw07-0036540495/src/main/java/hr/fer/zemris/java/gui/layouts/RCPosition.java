package hr.fer.zemris.java.gui.layouts;

/**
 * This record represents a position in a grid. It is used in {@link CalcLayout}.
 *
 * @see CalcLayout
 * @see RCPosition
 * @see RCPosition#parse(String)
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public record RCPosition(int row, int column) {
    /**
     * Factory method that parses the given text into a {@link RCPosition}.
     * <p>
     * Expected format: "row,column", where row and column are integers.
     *
     * @param text text to be parsed
     * @return {@link RCPosition} parsed from the given text
     * @throws IllegalArgumentException if the given text is not in the expected format
     */
    public static RCPosition parse(String text) {
        String message = "Invalid format of RCPosition. Expected format: \"row,column\", where row and column are integers.";
        String[] parts = text.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException(message);
        }
        try {
            int row = Integer.parseInt(parts[0]);
            int column = Integer.parseInt(parts[1]);
            return new RCPosition(row, column);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(message);
        }
    }
}
