package hr.fer.oprpp1.custom.scripting.elems;

/**
 * A class that represents a constant of type double parsed by {@link hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser}.
 *
 * @see Element
 * @see ElementConstantInteger
 * @see ElementFunction
 * @see ElementOperator
 * @see ElementString
 * @see ElementVariable
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class ElementConstantDouble extends Element {
    /**
     * Value of the constant.
     */
    private double value;

    /**
     * Creates an instance of ElementConstantDouble with the given value.
     *
     * @param value value of the constant
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * Returns the value of the constant.

     * @return value of the constant
     */
    public double getValue() {
        return value;
    }

    /**
     * @see Element#asText()
     */
    @Override
    public String asText() {
        return Double.toString(value);
    }

    /**
     * Checks if the given object is equal to this ElementConstantDouble.
     * <p>
     * The given object is equal to this ElementConstantDouble if it is an instance of ElementConstantDouble
     * and if their values are equal.
     *
     * @param obj object to be compared to this ElementConstantDouble
     * @return true if the given object is equal to this ElementConstantDouble, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof ElementConstantDouble other)) {
    		return false;
    	}
        return this.value == other.value;
    }
}
