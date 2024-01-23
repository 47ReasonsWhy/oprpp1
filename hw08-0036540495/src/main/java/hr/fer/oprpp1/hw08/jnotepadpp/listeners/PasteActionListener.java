package hr.fer.oprpp1.hw08.jnotepadpp.listeners;

import hr.fer.oprpp1.hw08.jnotepadpp.DefaultActions;
import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Listener used for enabling the paste action when there is at least one active document and the clipboard is not empty.
 *
 * @see hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener
 * @see DefaultMultipleDocumentModel
 * @see hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel
 * @see DefaultActions
 * @see javax.swing.Action
 * @see Clipboard
 *
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class PasteActionListener implements MultipleDocumentListener {

    /**
     * Model that is being listened to.
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Action subscribed to the listener.
     */
    private final Action action;

    /**
     * Constructor that initializes the model.
     *
     * @param model model that is being listened to
     * @param action action to be subscribed to the listener
     */
    public PasteActionListener(DefaultMultipleDocumentModel model, Action action) {
        this.model = model;
        this.action = action;
        action.setEnabled(model.getNumberOfDocuments() > 0 && isClipboardNonEmpty());

        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(e ->
                action.setEnabled(model.getNumberOfDocuments() > 0 && isClipboardNonEmpty())
        );
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousDocument, SingleDocumentModel currentDocument) {
        if (currentDocument == null) {
            action.setEnabled(false);
        } else {
            action.setEnabled(isClipboardNonEmpty());
        }
    }

    @Override
    public void documentAdded(SingleDocumentModel document) {
        action.setEnabled(isClipboardNonEmpty());
    }

    @Override
    public void documentRemoved(SingleDocumentModel document) {
        if (model.getNumberOfDocuments() == 0) {
            action.setEnabled(false);
        }
    }

    private boolean isClipboardNonEmpty() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        boolean stringContentsAvailable = clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);
        if (!stringContentsAvailable) {
            return false;
        }
        String clipBoardContent = "";
        try {
            clipBoardContent = (String) clipboard.getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException ignored) {
        }
        return !clipBoardContent.isEmpty();
    }
}
