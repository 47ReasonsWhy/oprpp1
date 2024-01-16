package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
     * Model for this text editor.
     */
    private DefaultMultipleDocumentModel model;

    /**
     * Actions for this text editor.
     */
    private DefaultActions actions;

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
        setSize(800, 600);
        setTitle("JNotepad++");
        initGUI();
    }

    /**
     * Initializes the graphical user interface along with the model and the actions.
     */
    private void initGUI() {
        model = new DefaultMultipleDocumentModel();
        actions = new DefaultActions(this, model);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(model, BorderLayout.CENTER);

        initActions();
        initMenus();
        initToolbar();
        initListeners();
    }

    /**
     * Initializes the actions.
     */
    private void initActions() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        Action newDocumentAction = actions.newDocumentAction;
        newDocumentAction.putValue(Action.NAME, "New");
        newDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to create new file.");
        newDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/New24.gif"));
        newDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/New16.gif"));
        newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        im.put(KeyStroke.getKeyStroke("control N"), "new");
        am.put("new", newDocumentAction);

        Action openDocumentAction = actions.openDocumentAction;
        openDocumentAction.putValue(Action.NAME, "Open");
        openDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open existing file from disk.");
        openDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Open24.gif"));
        openDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Open16.gif"));
        openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        im.put(KeyStroke.getKeyStroke("control O"), "open");
        am.put("open", openDocumentAction);

        Action saveDocumentAction = actions.saveDocumentAction;
        saveDocumentAction.putValue(Action.NAME, "Save");
        saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to save file to disk.");
        saveDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Save24.gif"));
        saveDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Save16.gif"));
        saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        im.put(KeyStroke.getKeyStroke("control S"), "save");
        am.put("save", saveDocumentAction);

        Action saveAsDocumentAction = actions.saveAsDocumentAction;
        saveAsDocumentAction.putValue(Action.NAME, "Save As");
        saveAsDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to save file to disk.");
        saveAsDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/SaveAs24.gif"));
        saveAsDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/SaveAs16.gif"));
        saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
        im.put(KeyStroke.getKeyStroke("control alt S"), "saveAs");
        am.put("saveAs", saveAsDocumentAction);

        Action closeDocumentAction = actions.closeDocumentAction;
        closeDocumentAction.putValue(Action.NAME, "Close");
        closeDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to close file.");
        closeDocumentAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Close24.gif"));
        closeDocumentAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Close16.gif"));
        closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
        im.put(KeyStroke.getKeyStroke("control W"), "close");
        am.put("close", closeDocumentAction);

        Action statisticsAction = actions.statisticsAction;
        statisticsAction.putValue(Action.NAME, "Statistics");
        statisticsAction.putValue(Action.SHORT_DESCRIPTION, "Used to show statistics about current document.");
        statisticsAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Statistics24.gif"));
        statisticsAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Statistics16.gif"));
        statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
        im.put(KeyStroke.getKeyStroke("control I"), "statistics");
        am.put("statistics", statisticsAction);

        Action exitAction = actions.exitAction;
        exitAction.putValue(Action.NAME, "Exit");
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Used to exit application.");
        exitAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Down24.gif"));
        exitAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Down16.gif"));
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
        im.put(KeyStroke.getKeyStroke("control Q"), "exit");
        am.put("exit", exitAction);


        Action cutAction = actions.cutAction;
        cutAction.putValue(Action.NAME, "Cut");
        cutAction.putValue(Action.SHORT_DESCRIPTION, "Used to cut selected text.");
        cutAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Cut24.gif"));
        cutAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Cut16.gif"));
        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        im.put(KeyStroke.getKeyStroke("control X"), DefaultEditorKit.cutAction);
        am.put(DefaultEditorKit.cutAction, cutAction);

        Action copyAction = actions.copyAction;
        copyAction.putValue(Action.NAME, "Copy");
        copyAction.putValue(Action.SHORT_DESCRIPTION, "Used to copy selected text.");
        copyAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Copy24.gif"));
        copyAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Copy16.gif"));
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        im.put(KeyStroke.getKeyStroke("control C"), DefaultEditorKit.copyAction);
        am.put(DefaultEditorKit.copyAction, copyAction);

        Action pasteAction = actions.pasteAction;
        pasteAction.putValue(Action.NAME, "Paste");
        pasteAction.putValue(Action.SHORT_DESCRIPTION, "Used to paste selected text.");
        pasteAction.putValue(Action.LARGE_ICON_KEY, Util.loadImageIcon("icons/Paste24.gif"));
        pasteAction.putValue(Action.SMALL_ICON, Util.loadImageIcon("icons/Paste16.gif"));
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        im.put(KeyStroke.getKeyStroke("control V"), DefaultEditorKit.pasteAction);
        am.put(DefaultEditorKit.pasteAction, pasteAction);
    }

    /**
     * Initializes the menu bar and its menus.
     */
    private void initMenus() {
        JMenuBar mb = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        mb.add(fileMenu);
        fileMenu.add(new JMenuItem(actions.newDocumentAction));
        fileMenu.add(new JMenuItem(actions.openDocumentAction));
        fileMenu.add(new JMenuItem(actions.saveDocumentAction));
        fileMenu.add(new JMenuItem(actions.saveAsDocumentAction));
        fileMenu.add(new JMenuItem(actions.closeDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(actions.statisticsAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(actions.exitAction));


        JMenu edit = new JMenu("Edit");
        mb.add(edit);
        edit.add(new JMenuItem(actions.cutAction));
        edit.add(new JMenuItem(actions.copyAction));
        edit.add(new JMenuItem(actions.pasteAction));

        JMenu languages = new JMenu("Languages");
        mb.add(languages);

        JMenu tools = new JMenu("Tools");
        mb.add(tools);

        JMenu help = new JMenu("Help");
        mb.add(help);

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
     * Initializes the listeners:
     * <ul>
     *     <li>for updating the title of the window</li>
     *     <li>for saving the documents when closing the window</li>
     * </ul>
     */
    private void initListeners() {
        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if (currentModel == null) {
                    setTitle("JNotepad++");
                } else if (currentModel.getFilePath() == null) {
                    setTitle("(unnamed) - JNotepad++");
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                actions.exitAction.actionPerformed(null);
            }
        });
    }
}
