package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;

/**
 * Class containing all actions used in {@link JNotepadPP}.
 * Actions are:
 * <ul>
 *     <li>new/open/save/saveAs/close document</li>
 *     <li>show document statistics</li>
 *     <li>exit application</li>
 *     <li>cut/copy/paste text</li>
 * </ul>
 */
public class DefaultActions {
    /**
     * Reference to {@link JNotepadPP} instance.
     */
    private final JNotepadPP jnp;

    /**
     * Reference to {@link DefaultMultipleDocumentModel} instance.
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * Constructor for {@link DefaultActions} class.
     *
     * @param jnp   reference to {@link JNotepadPP} instance
     * @param model reference to {@link DefaultMultipleDocumentModel} instance
     */
    public DefaultActions(JNotepadPP jnp, DefaultMultipleDocumentModel model) {
        this.jnp = jnp;
        this.model = model;

        initActions();
    }

    /**
     * Helper method used to initialize actions, disable appropriate ones
     * and add enabling-disabling listeners to them.
     */
    private void initActions() {
        saveDocumentAction.setEnabled(false);
        model.addMultipleDocumentListener(new EnablingDisablingListener(saveDocumentAction));
        saveAsDocumentAction.setEnabled(false);
        model.addMultipleDocumentListener(new EnablingDisablingListener(saveAsDocumentAction));

        closeDocumentAction.setEnabled(false);
        model.addMultipleDocumentListener(new EnablingDisablingListener(closeDocumentAction));

        statisticsAction.setEnabled(false);
        model.addMultipleDocumentListener(new EnablingDisablingListener(statisticsAction));

        cutAction.setEnabled(false);
        model.addMultipleDocumentListener(new EnablingDisablingListener(cutAction));
        copyAction.setEnabled(false);
        model.addMultipleDocumentListener(new EnablingDisablingListener(copyAction));
        pasteAction.setEnabled(false);
        model.addMultipleDocumentListener(new EnablingDisablingListener(pasteAction));
    }

    /**
     * Listener used to enable/disable appropriate actions.
     */
    private class EnablingDisablingListener implements MultipleDocumentListener {
        private final Action action;

        public EnablingDisablingListener(Action action) {
            this.action = action;
        }

        @Override
        public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
            // do nothing
        }

        @Override
        public void documentAdded(SingleDocumentModel model) {
            action.setEnabled(true);
        }

        @Override
        public void documentRemoved(SingleDocumentModel model) {
            if (DefaultActions.this.model.getNumberOfDocuments() == 0) {
                action.setEnabled(false);
            }
        }
    }

    /**
     * Action used to create new document.
     */
    public final Action newDocumentAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.createNewDocument();
        }
    };

    /**
     * Action used to open existing document.
     */
    public final Action openDocumentAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open file");
            if(fc.showOpenDialog(jnp) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File fileName = fc.getSelectedFile();
            String filePath = fileName.getAbsolutePath();
            model.loadDocument(filePath);
        }
    };

    /**
     * Action used to save active document.
     */
    public final Action saveDocumentAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentDocument() == null) return;
            if (model.getCurrentDocument().getFilePath() == null) {
                if (userCancelsSavingAs()) return;
            }
            model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath().toString());
        }
    };

    /**
     * Action used to save active document as.
     */
    public final Action saveAsDocumentAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentDocument() == null) return;
            // warn user if file already exists
            if (model.getCurrentDocument().getFilePath() != null) {
                JOptionPane.showMessageDialog(
                        jnp,
                        "File already exists!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            if (userCancelsSavingAs()) return;
            model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath().toString());
        }
    };

    /**
     * Helper method prompting the user to choose where to save the file.
     *
     * @return true if user cancels saving as, false otherwise
     */
    private boolean userCancelsSavingAs() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save file");
        if(fc.showSaveDialog(jnp) != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(
                    jnp,
                    "Ništa nije snimljeno",
                    "Upozorenje",
                    JOptionPane.WARNING_MESSAGE
            );
            return true;
        }
        File fileName = fc.getSelectedFile();
        String filePath = fileName.getAbsolutePath();
        model.getCurrentDocument().setFilePath(Path.of(filePath));
        return false;
    }

    /**
     * Action used to close active document.
     */
    public final Action closeDocumentAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentDocument() == null) return;
            if (model.getCurrentDocument().isModified()) {
                if (userCancelsSaving()) return;
            }
            model.closeDocument(model.getCurrentDocument());
        }
    };

    /**
     * Helper method prompting the user to save the file before closing it.
     *
     * @return true if the user cancels saving, false otherwise
     */
    private boolean userCancelsSaving() {
        int result = JOptionPane.showConfirmDialog(
                jnp,
                "File is not saved. Do you want to save it?",
                "Warning",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            if (model.getCurrentDocument().getFilePath() == null) {
                if (userCancelsSavingAs()) return true;
            }
            model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath().toString());
        } else return result == JOptionPane.CANCEL_OPTION;
        return false;
    }

    /**
     * Action used to show statistics about active document.
     * Statistics are: number of characters, number of non-blank characters and number of lines.
     */
    public final Action statisticsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentDocument() == null) return;
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            String text = editor.getText();
            int numOfChars = text.length();
            int numOfNonBlankChars = text.replaceAll("\\s+", "").length();
            int numOfLines = editor.getLineCount();
            JOptionPane.showMessageDialog(
                    jnp,
                    "Your document has " + numOfChars + " characters, " + numOfNonBlankChars + " non-blank characters and " + numOfLines + " lines.",
                    "Statistics",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    /**
     * Action used to exit the application.
     */
    public final Action exitAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean close = true;
            for (int i = 0; i < model.getNumberOfDocuments(); i++) {
                SingleDocumentModel document = model.getDocument(i);
                if (document.isModified()) {
                    model.setSelectedIndex(i);
                    model.repaint();
                    if (userCancelsSaving()) {
                        close = false;
                    }
                }
            }
            if (close) {
                jnp.dispose();
            }
        }
    };


    /**
     * Action used to cut selected text from active document to clipboard.
     */
    public final Action cutAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentDocument() == null) return;
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            editor.cut();
        }
    };

    /**
     * Action used to copy selected text to clipboard.
     */
    public final Action copyAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentDocument() == null) return;
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            editor.copy();
        }
    };

    /**
     * Action used to paste text from clipboard.
     */
    public final Action pasteAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentDocument() == null) return;
            JTextArea editor = model.getCurrentDocument().getTextComponent();
            editor.paste();
        }
    };
}
