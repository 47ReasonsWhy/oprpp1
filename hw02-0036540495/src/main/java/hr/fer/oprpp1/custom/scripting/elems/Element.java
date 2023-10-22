package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Base class for all elements produced by {@link hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser}.
 *
 * @see ElementConstantDouble
 * @see ElementConstantInteger
 * @see ElementFunction
 * @see ElementOperator
 * @see ElementString
 * @see ElementVariable
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Element {
    /**
     * Returns the element as text.
     *
     * @return string representation of the element
     */
    public String asText() {
        return "";
    }
}
