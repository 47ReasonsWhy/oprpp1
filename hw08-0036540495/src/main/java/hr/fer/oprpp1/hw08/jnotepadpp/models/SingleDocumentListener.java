package hr.fer.oprpp1.hw08.jnotepadpp.models;

/**
 * Interface that represents a listener for a single document.
 * <p>
 * It listens for:
 * <ul>
 *     <li>an update to the modification status of the document</li>
 *     <li>an update to the file path of the document</li>
 * </ul>
 */
public interface SingleDocumentListener {
    /**
     * Method called when the modification status of the document is updated.
     *
     * @param model document whose modification status was updated
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Method called when the file path of the document is updated.
     *
     * @param model document whose file path was updated
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}
