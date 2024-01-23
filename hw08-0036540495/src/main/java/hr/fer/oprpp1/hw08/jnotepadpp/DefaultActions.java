package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.listeners.ActiveDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.listeners.PasteActionListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Class containing all actions used in {@link JNotepadPP}.
 * Actions are:
 * <ul>
 *     <li>new/open/save/saveAs/close document</li>
 *     <li>show document statistics</li>
 *     <li>exit application</li>
 *     <li>cut/copy/paste text</li>
 * </ul>
 *
 * @version 1.0
 * @author Marko Šelendić
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
     * Reference to {@link Timer} instance.
     */
    private final Timer timer;

    /**
     * Reference to {@link ILocalizationProvider} instance.
     */
    private final ILocalizationProvider provider;

    /**
     * List of actions subscribed to the selected text listener.
     */
    private final List<Action> selectedTextSubscribedActions;

    /**
     * Getter for the list of actions subscribed to the selected text listener.
     *
     * @return list of actions subscribed to the selected text listener
     */
    public List<Action> getSelectedTextSubscribedActions() {
        return selectedTextSubscribedActions;
    }

    /**
     * Helper method used to subscribe an action to the selected text listener.
     *
     * @param action action to subscribe
     */
    private void subscribeToSelectedTextListener(Action action) {
        selectedTextSubscribedActions.add(action);
        action.setEnabled(false);
    }

    /**
     * Constructor for {@link DefaultActions} class.
     *
     * @param jnp   reference to {@link JNotepadPP} instance
     * @param localizationProvider reference to {@link ILocalizationProvider} instance
     * @param model reference to {@link DefaultMultipleDocumentModel} instance
     * @param timer reference to {@link Timer} instance
     * @throws NullPointerException if any of the arguments is null
     */
    public DefaultActions(JNotepadPP jnp, ILocalizationProvider localizationProvider, DefaultMultipleDocumentModel model, Timer timer) {
        if (jnp == null || localizationProvider == null || model == null || timer == null) {
            throw new NullPointerException("None of the arguments can be null!");
        }
        this.jnp = jnp;
        this.model = model;
        this.timer = timer;
        this.provider = localizationProvider;
        this.selectedTextSubscribedActions = new ArrayList<>();

        initActions();
        initListeners();
    }


    /**
     * Action used to create new document.
     */
    public Action newDocumentAction;

    /**
     * Action used to open existing document.
     */
    public Action openDocumentAction;

    /**
     * Action used to save active document.
     */
    public Action saveDocumentAction;

    /**
     * Action used to save active document as.
     */
    public Action saveAsDocumentAction;

    /**
     * Action used to close active document.
     */
    public Action closeDocumentAction;

    /**
     * Action used to show statistics about active document.
     * <p>
     * Statistics are: number of characters, number of non-blank characters and number of lines.
     */
    public Action statisticsAction;

    /**
     * Action used to exit the application.
     */
    public Action exitAction;


    /**
     * Action used to cut selected text from active document to clipboard.
     */
    public Action cutAction;

    /**
     * Action used to copy selected text to clipboard.
     */
    public Action copyAction;

    /**
     * Action used to paste text from clipboard.
     */
    public Action pasteAction;


    /**
     * Action used to change language to English.
     */
    public Action changeLanguageToEnglishAction;

    /**
     * Action used to change language to Croatian.
     */
    public Action changeLanguageToCroatianAction;

    /**
     * Action used to change language to Italian.
     */
    public Action changeLanguageToItalianAction;


    /**
     * Action used to switch selected text to upper case.
     */
    public Action toUpperCaseAction;

    /**
     * Action used to switch selected text to lower case.
     */
    public Action toLowerCaseAction;

    /**
     * Action used to invert case of selected text.
     */
    public Action invertCaseAction;

    /**
     * Action used to sort selected lines in ascending order.
     */
    public Action ascendingSortAction;

    /**
     * Action used to sort selected lines in descending order.
     */
    public Action descendingSortAction;

    /**
     * Action used to remove duplicate lines among selected text in the active document.
     */
    public Action removeDuplicateLinesAction;

    /**
     * Helper method used to initialize actions, disable appropriate ones
     * and add enabling-disabling listeners to them.
     */
    private void initListeners() {
        ActiveDocumentListener activeDocumentListener = new ActiveDocumentListener(model);
        model.addMultipleDocumentListener(activeDocumentListener);

        activeDocumentListener.subscribe(saveDocumentAction);
        activeDocumentListener.subscribe(saveAsDocumentAction);
        activeDocumentListener.subscribe(closeDocumentAction);

        activeDocumentListener.subscribe(statisticsAction);

        subscribeToSelectedTextListener(cutAction);
        subscribeToSelectedTextListener(copyAction);
        model.addMultipleDocumentListener(new PasteActionListener(model, pasteAction));

        subscribeToSelectedTextListener(toUpperCaseAction);
        subscribeToSelectedTextListener(toLowerCaseAction);
        subscribeToSelectedTextListener(invertCaseAction);

        subscribeToSelectedTextListener(ascendingSortAction);
        subscribeToSelectedTextListener(descendingSortAction);

        subscribeToSelectedTextListener(removeDuplicateLinesAction);
    }

    private void initActions() {
        newDocumentAction = new LocalizableAction("new", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.createNewDocument();
            }
        };

        openDocumentAction = new LocalizableAction("open", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle(provider.getString("open.title"));
                if(fc.showOpenDialog(jnp) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File fileName = fc.getSelectedFile();
                String filePath = fileName.getAbsolutePath();
                model.loadDocument(filePath);
            }
        };

        saveDocumentAction = new LocalizableAction("save", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                if (model.getCurrentDocument().getFilePath() == null) {
                    if (userCancelsSavingAs()) return;
                }
                model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath().toString());
            }
        };

        saveAsDocumentAction = new LocalizableAction("saveAs", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                // warn user if file already exists
                if (model.getCurrentDocument().getFilePath() != null) {
                    String fileName = model.getCurrentDocument().getFilePath().getFileName().toString();
                    JOptionPane.showMessageDialog(
                            jnp,
                            provider.getString("saveAs.messageIfAlreadyExists").replace("$fileName", fileName),
                            provider.getString("warning"),
                            JOptionPane.WARNING_MESSAGE
                    );
                }

                if (userCancelsSavingAs()) return;
                model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath().toString());
            }
        };

        statisticsAction = new LocalizableAction("statistics", provider) {
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
                        provider.getString("statistics.message")
                                .replace("$numOfChars", String.valueOf(numOfChars))
                                .replace("$numOfNonBlankChars", String.valueOf(numOfNonBlankChars))
                                .replace("$numOfLines", String.valueOf(numOfLines)),
                        provider.getString("statistics.name"),
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        };

        closeDocumentAction = new LocalizableAction("close", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                if (model.getCurrentDocument().isModified()) {
                    if (userCancelsSaving()) return;
                }
                model.closeDocument(model.getCurrentDocument());
            }
        };

        exitAction = new LocalizableAction("exit", provider) {
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
                    timer.stop();
                    jnp.dispose();
                }
            }
        };

        cutAction = new LocalizableAction("cut", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                JTextArea editor = model.getCurrentDocument().getTextComponent();
                editor.cut();
            }
        };

        copyAction = new LocalizableAction("copy", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                JTextArea editor = model.getCurrentDocument().getTextComponent();
                editor.copy();
            }
        };

        pasteAction = new LocalizableAction("paste", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                JTextArea editor = model.getCurrentDocument().getTextComponent();
                editor.paste();
            }
        };


        changeLanguageToEnglishAction = new LocalizableAction("english", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("en");
            }
        };

        changeLanguageToCroatianAction = new LocalizableAction("croatian", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("hr");
            }
        };

        changeLanguageToItalianAction = new LocalizableAction("italian", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalizationProvider.getInstance().setLanguage("it");
            }
        };


        toUpperCaseAction = new LocalizableAction("toUpperCase", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                JTextArea editor = model.getCurrentDocument().getTextComponent();
                int selectionStart = editor.getSelectionStart();
                int selectionEnd = editor.getSelectionEnd();
                editor.replaceSelection(getSelectedText(editor).toUpperCase());
                editor.setSelectionStart(selectionStart);
                editor.setSelectionEnd(selectionEnd);
            }
        };

        toLowerCaseAction = new LocalizableAction("toLowerCase", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                JTextArea editor = model.getCurrentDocument().getTextComponent();
                int selectionStart = editor.getSelectionStart();
                int selectionEnd = editor.getSelectionEnd();
                editor.replaceSelection(getSelectedText(editor).toLowerCase());
                editor.setSelectionStart(selectionStart);
                editor.setSelectionEnd(selectionEnd);
            }
        };

        invertCaseAction = new LocalizableAction("invertCase", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentDocument() == null) return;
                JTextArea editor = model.getCurrentDocument().getTextComponent();
                int selectionStart = editor.getSelectionStart();
                int selectionEnd = editor.getSelectionEnd();
                char[] chars = getSelectedText(editor).toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (Character.isLowerCase(chars[i])) {
                        chars[i] = Character.toUpperCase(chars[i]);
                    } else if (Character.isUpperCase(chars[i])) {
                        chars[i] = Character.toLowerCase(chars[i]);
                    }
                }
                editor.replaceSelection(new String(chars));
                editor.setSelectionStart(selectionStart);
                editor.setSelectionEnd(selectionEnd);
            }
        };

        ascendingSortAction = new LocalizableAction("sortAsc", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortLines(true);
            }
        };

        descendingSortAction = new LocalizableAction("sortDesc", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortLines(false);
            }
        };

        removeDuplicateLinesAction = new LocalizableAction("unique", provider) {
            @Override
            public void actionPerformed(ActionEvent e) {
                processLines(lines -> {
                    List<String> uniqueLines = new ArrayList<>();
                    for (String line : lines) {
                        if (!uniqueLines.contains(line)) {
                            uniqueLines.add(line);
                        }
                    }
                    lines.clear();
                    lines.addAll(uniqueLines);
                });
            }
        };
    }

    /**
     * Helper method prompting the user to choose where to save the file.
     *
     * @return true if user cancels saving as, false otherwise
     */
    private boolean userCancelsSavingAs() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(provider.getString("saveAs.title"));
        if(fc.showSaveDialog(jnp) != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(
                    jnp,
                    provider.getString("saveAs.messageIfCanceled"),
                    provider.getString("warning"),
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
     * Helper method prompting the user to save the file before closing it.
     *
     * @return true if the user cancels saving, false otherwise
     */
    private boolean userCancelsSaving() {
        Path filePath = model.getCurrentDocument().getFilePath();
        String fileName = filePath == null ? provider.getString("unnamed") : filePath.getFileName().toString();
        int result = JOptionPane.showOptionDialog(
                jnp,
                provider.getString("save.messageIfNotSaved").replace("$fileName", fileName),
                provider.getString("warning"),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new String[]{provider.getString("yes"), provider.getString("no"), provider.getString("cancel")},
                provider.getString("cancel")
        );
        if (result == JOptionPane.YES_OPTION) {
            if (filePath == null) {
                if (userCancelsSavingAs()) return true;
            }
            model.saveDocument(model.getCurrentDocument(), model.getCurrentDocument().getFilePath().toString());
        } else return result == JOptionPane.CANCEL_OPTION;
        return false;
    }

    /**
     * Helper function used to get selected text from {@link JTextArea}.
     *
     * @param editor {@link JTextArea} from which to get selected text
     * @return selected text
     */
    private String getSelectedText(JTextArea editor) {
        int start = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
        int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
        return editor.getText().substring(start, start + len);
    }

    /**
     * Helper function used to sort lines among selected text in ascending or descending order.
     *
     * @param ascending true if the lines should be sorted in ascending order, false otherwise
     */
    private void sortLines(boolean ascending) {
        processLines(lines -> {
            Locale locale = new Locale(provider.getCurrentLanguage());
            Collator collator = Collator.getInstance(locale);
            lines.sort(ascending ? collator : collator.reversed());
        });
    }

    /**
     * Helper method used to process selected lines in the active document via the given processor.
     *
     * @param processor function that processes selected lines
     */
    private void processLines(Consumer<List<String>> processor) {
        if (model.getCurrentDocument() == null) return;
        // get selected lines
        JTextArea editor = model.getCurrentDocument().getTextComponent();
        int dot = editor.getCaret().getDot();
        int mark = editor.getCaret().getMark();
        int start = 0;
        int end = 0;
        String text;
        boolean endsWithNewLine = false;
        List<String> lines = new ArrayList<>();
        try {
            start = editor.getLineStartOffset(editor.getLineOfOffset(Math.min(dot, mark)));
            end = editor.getLineEndOffset(editor.getLineOfOffset(Math.max(dot, mark)));
            text = editor.getText(start, end - start);
            if (text.endsWith("\n")) {
                text = text.substring(0, text.length() - 1);
                endsWithNewLine = true;
            }
            lines.addAll(List.of(text.split("\n", -1)));
        } catch (BadLocationException ignored) {
        }

        // process selected lines
        processor.accept(lines);

        // replace selected lines with processed lines
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        if (!endsWithNewLine) {
            sb.deleteCharAt(sb.length() - 1);
        }
        editor.replaceRange(sb.toString(), start, end);
    }
}
