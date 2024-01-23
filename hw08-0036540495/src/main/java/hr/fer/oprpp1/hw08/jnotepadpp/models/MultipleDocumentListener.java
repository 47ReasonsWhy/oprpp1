package hr.fer.oprpp1.hw08.jnotepadpp.models;

/**
 * Interface that represents a listener for {@link MultipleDocumentModel}.
 * <p>
 * It listens for:
 * <ul>
 *     <li>setting another document as current</li>
 *     <li>adding a new document</li>
 *     <li>removing a document</li>
 * </ul>
 *
 * @see SingleDocumentModel
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface MultipleDocumentListener {

    /**
     * Method called when another document is set as current.
     *
     * @param previousModel previous document
     * @param currentModel current document
     */
    void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

    /**
     * Method called when a new document is added to the model.
     *
     * @param model added document
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Method called when a document is removed from the model.
     *
     * @param model removed document
     */
    void documentRemoved(SingleDocumentModel model);
}
