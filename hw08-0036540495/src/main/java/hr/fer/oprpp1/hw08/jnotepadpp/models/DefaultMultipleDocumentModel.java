package hr.fer.oprpp1.hw08.jnotepadpp.models;

import hr.fer.oprpp1.hw08.jnotepadpp.Util;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link MultipleDocumentModel} interface used in {@link hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP}.
 *
 * @see MultipleDocumentModel
 * @see SingleDocumentModel
 *
 * @see DefaultSingleDocumentModel
 * @see JTabbedPane
 * @see JScrollPane
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
    /**
     * List of documents.
     */
    private final List<SingleDocumentModel> documents;

    /**
     * Index of current document.
     */
    private int currentDocumentIndex;

    /**
     * List of listeners.
     */
    private final List<MultipleDocumentListener> listeners;

    /**
     * Icon used for unsaved documents.
     */
    private final ImageIcon unsavedIcon;

    /**
     * Icon used for saved documents.
     */
    private final ImageIcon savedIcon;

    /**
     * Constructs a new {@link DefaultMultipleDocumentModel} object.
     * Loads the icons used for saved and unsaved document
     * and adds a listener for keeping track of current document.
     */
    public DefaultMultipleDocumentModel() {
        super();
        documents = new ArrayList<>();
        currentDocumentIndex = -1;
        listeners = new ArrayList<>();

        unsavedIcon = Util.loadImageIcon("icons/Stop16.gif");
        savedIcon = Util.loadImageIcon("icons/Save16.gif");

        addChangeListener(e -> {
            SingleDocumentModel previousDocument = getCurrentDocument();
            currentDocumentIndex = getSelectedIndex();
            listeners.forEach(l -> l.currentDocumentChanged(previousDocument, getCurrentDocument()));
        });
    }

    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        if (currentDocumentIndex == -1) {
            return null;
        }
        return documents.get(currentDocumentIndex);
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel previousDocument = getCurrentDocument();
        DefaultSingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, "");
        documents.add(newDocument);
        newDocument.setModified(true);
        addSingleDocumentListener(newDocument);

        addTab("(unnamed)", unsavedIcon, new JScrollPane(newDocument.getTextComponent()), "(unnamed)");
        setSelectedIndex(documents.size() - 1);

        listeners.forEach(l -> {
            l.documentAdded(newDocument);
            l.currentDocumentChanged(previousDocument, newDocument);
        });
        return newDocument;
    }

    @Override
    public SingleDocumentModel loadDocument(String path) {
        if (path == null) {
            throw new NullPointerException("Path can't be null.");
        }

        SingleDocumentModel previousDocument = getCurrentDocument();
        for (SingleDocumentModel document : documents) {
            if (document.getFilePath() != null && document.getFilePath().toString().equals(path)) {
                if (!document.equals(previousDocument)) {
                    setSelectedIndex(documents.indexOf(document));
                    listeners.forEach(l -> l.currentDocumentChanged(previousDocument, document));
                }
                return document;
            }
        }

        byte[] bytes;
        Path pathPath = Path.of(path);
        try {
            bytes = Files.readAllBytes(pathPath);
        } catch(Exception ex) {
            throw new RuntimeException("Error while reading file!");
        }
        String text = new String(bytes, StandardCharsets.UTF_8);

        DefaultSingleDocumentModel newDocument = new DefaultSingleDocumentModel(pathPath, text);
        documents.add(newDocument);
        addSingleDocumentListener(newDocument);

        addTab(pathPath.getFileName().toString(), savedIcon, new JScrollPane(newDocument.getTextComponent()), pathPath.toString());
        setSelectedIndex(documents.size() - 1);

        listeners.forEach(l -> {
            l.documentAdded(newDocument);
            l.currentDocumentChanged(previousDocument, newDocument);
        });
        return newDocument;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, String newPath) {
        if (model == null) {
            throw new NullPointerException("Model can't be null.");
        }
        if (newPath == null) {
            newPath = model.getFilePath().toString();
        } else {
            model.setFilePath(Path.of(newPath));
        }
        try {
            Files.writeString(Path.of(newPath), model.getTextComponent().getText());
        } catch (IOException ex) {
            throw new RuntimeException("Error while writing file!");
        }
        model.setModified(false);
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        if (model == null) {
            throw new NullPointerException("Model can't be null.");
        }
        SingleDocumentModel previousDocument = getCurrentDocument();

        int index = documents.indexOf(model);
        removeTabAt(index);
        documents.remove(index);

        if (currentDocumentIndex > documents.size() - 1) {
            setSelectedIndex(documents.size() - 1);
        }

        listeners.forEach(l -> {
            l.documentRemoved(model);
            l.currentDocumentChanged(previousDocument, getCurrentDocument());
        });
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        if (l == null) {
            throw new NullPointerException("Listener can't be null.");
        }
        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        if (l == null) {
            throw new NullPointerException("Listener can't be null.");
        }
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

    @Override
    public SingleDocumentModel findForPath(Path path) {
        if (path == null) {
            throw new NullPointerException("Path can't be null!");
        }
        for (SingleDocumentModel document : documents) {
            if (document.getFilePath() != null && document.getFilePath().equals(path)) {
                return document;
            }
        }
        return null;
    }

    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        return documents.indexOf(doc);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

    /**
     * Adds a {@link SingleDocumentListener} to the given {@link SingleDocumentModel}
     * which updates the tab title and icon when needed.
     *
     * @param model model to which the listener is added
     */
    private void addSingleDocumentListener(SingleDocumentModel model) {
        model.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                int index = documents.indexOf(model);
                if (model.isModified()) {
                    setIconAt(index, unsavedIcon);
                } else {
                    setIconAt(index, savedIcon);
                }
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                int index = documents.indexOf(model);
                setTitleAt(index, model.getFilePath().getFileName().toString());
                setToolTipTextAt(index, model.getFilePath().toString());
            }
        });
    }
}
