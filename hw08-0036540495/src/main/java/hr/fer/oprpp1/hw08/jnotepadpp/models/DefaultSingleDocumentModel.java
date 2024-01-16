package hr.fer.oprpp1.hw08.jnotepadpp.models;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link SingleDocumentModel} interface used in {@link DefaultMultipleDocumentModel}.
 *
 * @see hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
    /**
     * Path to the document
     */
    private Path path;

    /**
     * Flag that tells if the document is modified
     */
    private boolean modified;

    /**
     * Text area of the document
     */
    private final JTextArea textArea;

    /**
     * List of listeners
     */
    private final List<SingleDocumentListener> listeners;

    /**
     * Constructs a new {@link DefaultSingleDocumentModel} object
     * and adds a new {@link DocumentListener} to the text area
     * to keep track of the modification status.
     *
     * @param path path to the document
     * @param text text of the document
     */
    public DefaultSingleDocumentModel(Path path, String text) {
        this.path = path;
        this.modified = false;
        this.textArea = new JTextArea(text);
        this.textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        this.textArea.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
        this.listeners = new ArrayList<>();
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                modified = true;
                for (SingleDocumentListener l : listeners) {
                    l.documentModifyStatusUpdated(DefaultSingleDocumentModel.this);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modified = true;
                for (SingleDocumentListener l : listeners) {
                    l.documentModifyStatusUpdated(DefaultSingleDocumentModel.this);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modified = true;
                for (SingleDocumentListener l : listeners) {
                    l.documentModifyStatusUpdated(DefaultSingleDocumentModel.this);
                }
            }
        });
    }

    @Override
    public JTextArea getTextComponent() {
        return textArea;
    }

    @Override
    public Path getFilePath() {
        return path;
    }

    @Override
    public void setFilePath(Path path) {
        this.path = path;
        for (SingleDocumentListener l : listeners) {
            l.documentFilePathUpdated(this);
        }
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        for (SingleDocumentListener l : listeners) {
            l.documentModifyStatusUpdated(this);
        }
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }
}
