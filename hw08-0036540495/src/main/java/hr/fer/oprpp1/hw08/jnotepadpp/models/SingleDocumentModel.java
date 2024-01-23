package hr.fer.oprpp1.hw08.jnotepadpp.models;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Represents a model capable of holding a single document.
 * Each document has its file path, modification status and reference to Swing component which is used for editing.
 *
 * @see DefaultSingleDocumentModel
 * @see SingleDocumentListener
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface SingleDocumentModel {

    /**
     * @return text component used for editing the document
     */
    JTextArea getTextComponent();

    /**
     * @return path to the document
     */
    Path getFilePath();

    /**
     * Sets the path to the document
     *
     * @param path path to the document
     * @throws NullPointerException if path is null
     */
    void setFilePath(Path path);

    /**
     * Returns true if document is modified, false otherwise
     *
     * @return true if document is modified, false otherwise
     */
    boolean isModified();

    /**
     * Sets the modified status of the document
     *
     * @param modified modified status of the document
     */
    void setModified(boolean modified);

    /**
     * Adds a listener to the document
     *
     * @param l listener to be added
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Removes a listener from the document
     *
     * @param l listener to be removed
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}
