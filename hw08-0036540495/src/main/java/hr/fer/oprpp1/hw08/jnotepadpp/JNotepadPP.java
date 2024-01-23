package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

/**
 * A simple text editor.
 * It has a menu bar, a toolbar and a tabbed pane.
 * It uses {@link DefaultMultipleDocumentModel} as its model.
 * and {@link DefaultActions} as its actions.
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class JNotepadPP extends JFrame {

    /**
     * Localization provider.
     */
    private FormLocalizationProvider flp;

    /**
     * Model for this text editor.
     */
    private DefaultMultipleDocumentModel model;

    /**
     * Actions for this text editor.
     */
    private DefaultActions actions;

    /**
     * Date format used for displaying the date and time in the status bar.
     */
    private SimpleDateFormat sdf;

    /**
     * Timer used for updating the date and time in the status bar.
     */
    private Timer timer;

    /**
     * Main method from which the program starts.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
    }

    /**
     * Constructs a new text editor.
     */
    private JNotepadPP() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocation(20, 20);
        setSize(870, 600);
        setTitle("JNotepad++");
        initGUI();
    }

    /**
     * Initializes the graphical user interface along with the model and the actions.
     */
    private void initGUI() {
        flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
        model = new DefaultMultipleDocumentModel(flp);
        timer = new Timer(250, null);
        actions = new DefaultActions(this, flp, model, timer);
        sdf = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.add(model, BorderLayout.CENTER);

        cp.add(editorPanel, BorderLayout.CENTER);

        initActions();
        initListeners();

        initMenus();
        initToolbar();
        initStatusBar(editorPanel);
    }

    /**
     * Initializes the actions.
     */
    private void initActions() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        Action newDocumentAction = actions.newDocumentAction;
        newDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/New24.gif"));
        newDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/New16.gif"));
        newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        im.put(KeyStroke.getKeyStroke("control N"), "new");
        am.put("new", newDocumentAction);

        Action openDocumentAction = actions.openDocumentAction;
        openDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Open24.gif"));
        openDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Open16.gif"));
        openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        im.put(KeyStroke.getKeyStroke("control O"), "open");
        am.put("open", openDocumentAction);

        Action saveDocumentAction = actions.saveDocumentAction;
        saveDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Save24.gif"));
        saveDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Save16.gif"));
        saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        im.put(KeyStroke.getKeyStroke("control S"), "save");
        am.put("save", saveDocumentAction);

        Action saveAsDocumentAction = actions.saveAsDocumentAction;
        saveAsDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/SaveAs24.gif"));
        saveAsDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/SaveAs16.gif"));
        saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
        im.put(KeyStroke.getKeyStroke("control alt S"), "saveAs");
        am.put("saveAs", saveAsDocumentAction);

        Action closeDocumentAction = actions.closeDocumentAction;
        closeDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Close24.gif"));
        closeDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Close16.gif"));
        closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
        im.put(KeyStroke.getKeyStroke("control W"), "close");
        am.put("close", closeDocumentAction);

        Action statisticsAction = actions.statisticsAction;
        statisticsAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Statistics24.gif"));
        statisticsAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Statistics16.gif"));
        statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt I"));
        im.put(KeyStroke.getKeyStroke("control alt I"), "statistics");
        am.put("statistics", statisticsAction);

        Action exitAction = actions.exitAction;
        exitAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Stop24.gif"));
        exitAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Stop16.gif"));
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
        im.put(KeyStroke.getKeyStroke("control Q"), "exit");
        am.put("exit", exitAction);


        Action cutAction = actions.cutAction;
        cutAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Cut24.gif"));
        cutAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Cut16.gif"));
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        im.put(KeyStroke.getKeyStroke("control X"), DefaultEditorKit.cutAction);
        am.put(DefaultEditorKit.cutAction, cutAction);

        Action copyAction = actions.copyAction;
        copyAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Copy24.gif"));
        copyAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Copy16.gif"));
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        im.put(KeyStroke.getKeyStroke("control C"), DefaultEditorKit.copyAction);
        am.put(DefaultEditorKit.copyAction, copyAction);

        Action pasteAction = actions.pasteAction;
        pasteAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Paste24.gif"));
        pasteAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Paste16.gif"));
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        im.put(KeyStroke.getKeyStroke("control V"), DefaultEditorKit.pasteAction);
        am.put(DefaultEditorKit.pasteAction, pasteAction);


        Action changeLanguageToEnglishAction = actions.changeLanguageToEnglishAction;
        changeLanguageToEnglishAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
        im.put(KeyStroke.getKeyStroke("control E"), "changeLanguageToEnglish");
        am.put("changeLanguageToEnglish", changeLanguageToEnglishAction);

        Action changeLanguageToCroatianAction = actions.changeLanguageToCroatianAction;
        changeLanguageToCroatianAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control H"));
        im.put(KeyStroke.getKeyStroke("control H"), "changeLanguageToCroatian");
        am.put("changeLanguageToCroatian", changeLanguageToCroatianAction);

        Action changeLanguageToItalianAction = actions.changeLanguageToItalianAction;
        changeLanguageToItalianAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
        im.put(KeyStroke.getKeyStroke("control I"), "changeLanguageToItalian");
        am.put("changeLanguageToItalian", changeLanguageToItalianAction);


        Action toUpperCaseAction = actions.toUpperCaseAction;
        toUpperCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
        im.put(KeyStroke.getKeyStroke("control U"), "toUpperCase");
        am.put("toUpperCase", toUpperCaseAction);

        Action toLowerCaseAction = actions.toLowerCaseAction;
        toLowerCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
        im.put(KeyStroke.getKeyStroke("control L"), "toLowerCase");
        am.put("toLowerCase", toLowerCaseAction);

        Action invertCaseAction = actions.invertCaseAction;
        invertCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control K"));
        im.put(KeyStroke.getKeyStroke("control K"), "invertCase");
        am.put("invertCase", invertCaseAction);

        Action ascendingSortAction = actions.ascendingSortAction;
        ascendingSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
        im.put(KeyStroke.getKeyStroke("control T"), "ascendingSort");
        am.put("ascendingSort", ascendingSortAction);

        Action descendingSortAction = actions.descendingSortAction;
        descendingSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt T"));
        im.put(KeyStroke.getKeyStroke("control alt T"), "descendingSort");
        am.put("descendingSort", descendingSortAction);

        Action removeDuplicateLinesAction = actions.removeDuplicateLinesAction;
        removeDuplicateLinesAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
        im.put(KeyStroke.getKeyStroke("control D"), "unique");
        am.put("unique", removeDuplicateLinesAction);
    }

    /**
     * Initializes the "global" listeners:
     * <ul>
     *     <li>for saving the documents when closing the window</li>
     *     <li>for updating the title of the window when another document is selected as active</li>
     *     <li>for updating the title of the window when the language of the application is changed</li>
     * </ul>
     */
    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                actions.exitAction.actionPerformed(null);
            }
        });

        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (currentModel == null) {
                    setTitle("JNotepad++");
                } else if (currentModel.getFilePath() == null) {
                    setTitle(flp.getString("unnamed") + " - JNotepad++");
                } else {
                    setTitle(currentModel.getFilePath().toString() + " - JNotepad++");
                }
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                // do nothing
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                // do nothing
            }
        });

        flp.addLocalizationListener(() -> {
            if (model.getCurrentDocument() != null && model.getCurrentDocument().getFilePath() == null) {
                setTitle(flp.getString("unnamed") + " - JNotepad++");
            }
        });
    }

    /**
     * Initializes the menu bar and its menus.
     */
    private void initMenus() {
        JMenuBar mb = new JMenuBar();

        JMenu file = new LJMenu("file", flp);
        mb.add(file);
        file.add(new JMenuItem(actions.newDocumentAction));
        file.add(new JMenuItem(actions.openDocumentAction));
        file.add(new JMenuItem(actions.saveDocumentAction));
        file.add(new JMenuItem(actions.saveAsDocumentAction));
        file.add(new JMenuItem(actions.closeDocumentAction));
        file.addSeparator();
        file.add(new JMenuItem(actions.statisticsAction));
        file.addSeparator();
        file.add(new JMenuItem(actions.exitAction));


        JMenu edit = new LJMenu("edit", flp);
        mb.add(edit);
        edit.add(new JMenuItem(actions.cutAction));
        edit.add(new JMenuItem(actions.copyAction));
        edit.add(new JMenuItem(actions.pasteAction));

        JMenu language = new LJMenu("language", flp);
        mb.add(language);
        language.add(new JMenuItem(actions.changeLanguageToEnglishAction));
        language.add(new JMenuItem(actions.changeLanguageToCroatianAction));
        language.add(new JMenuItem(actions.changeLanguageToItalianAction));


        JMenu tools = new LJMenu("tools", flp);
        mb.add(tools);
        JMenu changeCase = new LJMenu("changeCase", flp);
        tools.add(changeCase);
        changeCase.add(new JMenuItem(actions.toUpperCaseAction));
        changeCase.add(new JMenuItem(actions.toLowerCaseAction));
        changeCase.add(new JMenuItem(actions.invertCaseAction));
        JMenu sort = new LJMenu("sort", flp);
        tools.add(sort);
        sort.add(new JMenuItem(actions.ascendingSortAction));
        sort.add(new JMenuItem(actions.descendingSortAction));
        JMenuItem unique = new JMenuItem(actions.removeDuplicateLinesAction);
        tools.add(unique);

        setJMenuBar(mb);
    }

    /**
     * Initializes the toolbar.
     */
    private void initToolbar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(true);
        getContentPane().add(tb, BorderLayout.PAGE_START);

        tb.add(new JButton(actions.newDocumentAction));
        tb.add(new JButton(actions.openDocumentAction));
        tb.add(new JButton(actions.saveDocumentAction));
        tb.add(new JButton(actions.saveAsDocumentAction));
        tb.add(new JButton(actions.closeDocumentAction));
        tb.addSeparator();
        tb.add(new JButton(actions.cutAction));
        tb.add(new JButton(actions.copyAction));
        tb.add(new JButton(actions.pasteAction));
        tb.addSeparator();
        tb.add(new JButton(actions.statisticsAction));
        tb.addSeparator();
        tb.add(new JButton(actions.exitAction));
    }

    /**
     * Initializes the status bar and adds appropriate listeners
     * for keeping track of document length and caret status.
     *
     * @param editorPanel panel in bottom of which the status bar will be placed
     */
    private void initStatusBar(JPanel editorPanel) {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setPreferredSize(new Dimension(0, 20));
        statusBar.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.GRAY));
        editorPanel.add(statusBar, BorderLayout.PAGE_END);

        JPanel lengthAndCaretGrid = new JPanel(new GridLayout(1, 2));
        lengthAndCaretGrid.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        statusBar.add(lengthAndCaretGrid, BorderLayout.LINE_START);
        JLabel dateAndTime = new JLabel();
        dateAndTime.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        statusBar.add(dateAndTime, BorderLayout.LINE_END);

        JLabel length = new JLabel(" ".repeat(30));
        length.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        lengthAndCaretGrid.add(length);
        JLabel caret = new JLabel(" ".repeat(30));
        caret.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
        lengthAndCaretGrid.add(caret);

        timer.addActionListener(e -> dateAndTime.setText(sdf.format(System.currentTimeMillis())));
        timer.start();

        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                updateLengthAndCaret(length, caret, currentModel);

                if (previousModel != null) {
                    previousModel.getTextComponent().getDocument().removeDocumentListener(lengthListenerFor(previousModel, length));
                    previousModel.getTextComponent().getCaret().removeChangeListener(caretListenerFor(previousModel, caret));
                }

                if (currentModel != null) {
                    currentModel.getTextComponent().getDocument().addDocumentListener(lengthListenerFor(currentModel, length));
                    currentModel.getTextComponent().getCaret().addChangeListener(caretListenerFor(currentModel, caret));
                }
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                // do nothing
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                // do nothing
            }
        });

        flp.addLocalizationListener(() -> {
            SingleDocumentModel currentModel = model.getCurrentDocument();
            updateLengthAndCaret(length, caret, currentModel);
        });
    }

    private void updateLengthAndCaret(JLabel length, JLabel caret, SingleDocumentModel currentModel) {
        if (currentModel == null) {
            length.setText(" ".repeat(30));
            caret.setText(" ".repeat(30));
        } else {
            length.setText(flp.getString("length") + " " + currentModel.getTextComponent().getText().length());
            caret.setText(flp.getString("line") + " " + (currentModel.getTextComponent().getCaretPosition() + 1) +
                    "  " + flp.getString("column") + " " + (currentModel.getTextComponent().getCaretPosition() + 1) +
                    "  " + flp.getString("sel") + " " + Math.abs(currentModel.getTextComponent().getCaret().getDot() - currentModel.getTextComponent().getCaret().getMark()));
        }
    }

    /**
     * Creates a new document length listener for the given model and label.
     *
     * @param model model for which the listener will be created
     * @param label label which will be updated
     * @return new document length listener
     * @throws NullPointerException if the given model or label is null
     */
    private DocumentListener lengthListenerFor(SingleDocumentModel model, JLabel label) {
        if (model == null || label == null) {
            throw new NullPointerException("Model and given label must not be null.");
        }
        String length = flp.getString("length") + " ";
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                label.setText(length + model.getTextComponent().getText().length());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                label.setText(length + model.getTextComponent().getText().length());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                label.setText(length + model.getTextComponent().getText().length());
            }
        };
    }

    /**
     * Creates a new caret listener for the given model and label.
     *
     * @param model model for which the listener will be created
     * @param label label which will be updated
     * @return new caret listener
     * @throws NullPointerException if the given model or label is null
     */
    private ChangeListener caretListenerFor(SingleDocumentModel model, JLabel label) {
        if (model == null || label == null) {
            throw new NullPointerException("Model and given label must not be null.");
        }
        return e -> {
            JTextArea textArea = model.getTextComponent();
            int caretPosition = textArea.getCaretPosition();
            int currentLine = 1;
            int currentColumn = 1;
            try {
                currentLine = textArea.getLineOfOffset(caretPosition) + 1;
                currentColumn = caretPosition - textArea.getLineStartOffset(currentLine - 1) + 1;
            } catch (Exception ignored) {
            }
            int selectionLength = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
            label.setText(flp.getString("line") + " " + currentLine +
                    "  " + flp.getString("column") + " " + currentColumn +
                    "  " + flp.getString("sel") + " " + selectionLength);
            actions.getSelectedTextSubscribedActions().forEach(action -> action.setEnabled(selectionLength > 0));
        };
    }
}
