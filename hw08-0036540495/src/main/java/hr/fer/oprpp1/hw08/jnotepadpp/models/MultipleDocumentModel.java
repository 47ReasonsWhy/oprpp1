package hr.fer.oprpp1.hw08.jnotepadpp.models;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Represents a model capable of holding zero, one or more documents, where each document is an instance of
 * {@link SingleDocumentModel}.
 * It offers methods for creating, opening and closing documents, and methods for
 * iterating through all currently opened documents.
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    /**
     * Returns the graphical component responsible for displaying the entire user interface of the model
     *
     * @return graphical component responsible for displaying the entire user interface of the model
     */
    JComponent getVisualComponent();

    /**
     * Creates a new document, adds it to the model, and returns it.
     *
     * @return newly created document
     */
    SingleDocumentModel createNewDocument();

    /**
     * Returns the current document.
     *
     * @return current document
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Loads a document from the given path, adds it to the model, and returns it.
     * If the document is already opened, it is set as the current document.
     *
     * @param path path of the document
     * @return loaded document
     * @throws NullPointerException if given path is <code>null</code>
     */
    SingleDocumentModel loadDocument(String path);

    /**
     * Saves the given document to the given path, or to the path from which it was loaded if the path is <code>null</code>.
     *
     * @param model document to be saved
     * @param newPath path to save the document to, or <code>null</code> if the document should be saved to the path from which it was loaded
     * @throws NullPointerException if given model is <code>null</code>
     */
    void saveDocument(SingleDocumentModel model, String newPath);

    /**
     * Closes the given document.
     *
     * @param model document to be closed
     * @throws NullPointerException if given model is <code>null</code>
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Adds the given listener to the list of listeners.
     *
     * @param l listener to be added
     * @throws NullPointerException if given listener is <code>null</code>
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Removes the given listener from the list of listeners.
     *
     * @param l listener to be removed
     * @throws NullPointerException if given listener is <code>null</code>
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Returns the number of documents currently opened in the model.
     *
     * @return number of documents currently opened in the model
     */
    int getNumberOfDocuments();

    /**
     * Returns the document at the given index.
     *
     * @param index index of the document
     * @return document at the given index
     * @throws IndexOutOfBoundsException if given index is invalid
     */
    SingleDocumentModel getDocument(int index);

    /**
     * Returns the document with given path, or <code>null</code> if not found.
     *
     * @param path path of the document
     * @return document with given path, or <code>null</code> if not found
     * @throws NullPointerException if given path is <code>null</code>
     */
    SingleDocumentModel findForPath(Path path);

    /**
     * Returns the index of the given document, or -1 if not found.
     *
     * @param doc document
     * @return index of the given document, or -1 if not found
     * @throws NullPointerException if given document is <code>null</code>
     */
    int getIndexOfDocument(SingleDocumentModel doc);
}
