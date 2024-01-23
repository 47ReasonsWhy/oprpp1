package hr.fer.oprpp1.hw08.jnotepadpp.listeners;

import hr.fer.oprpp1.hw08.jnotepadpp.DefaultActions;
import hr.fer.oprpp1.hw08.jnotepadpp.models.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Listener used to enable an action when there is at least one active document
 * and disable it when there are no active documents.
 *
 * @see hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentListener
 * @see hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel
 * @see javax.swing.Action
 * @see DefaultActions
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class ActiveDocumentListener implements MultipleDocumentListener {

    /**
     * Model that is being listened to.
     */
    private final DefaultMultipleDocumentModel model;

    /**
     * List of actions subscribed to the listener.
     */
    private final List<Action> actions;

    /**
     * Constructor that initializes the model and the list of actions.
     *
     * @param model model that is being listened to
     */
    public ActiveDocumentListener(DefaultMultipleDocumentModel model) {
        this.model = model;
        actions = new ArrayList<>();
    }

    /**
     * Subscribes the action to the listener.
     *
     * @param action action to be subscribed
     */
    public void subscribe(Action action) {
        actions.add(action);
        action.setEnabled(model.getNumberOfDocuments() > 0);
    }

    /**
     * Unsubscribes the action from the listener.
     *
     * @param action action to be unsubscribed
     */
    public void unsubscribe(Action action) {
        actions.remove(action);
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousDocument, SingleDocumentModel currentDocument) {
        if (currentDocument == null) {
            actions.forEach(action -> action.setEnabled(false));
        } else {
            actions.forEach(action -> action.setEnabled(true));
        }
    }

    @Override
    public void documentAdded(SingleDocumentModel document) {
        actions.forEach(action -> action.setEnabled(true));
    }

    @Override
    public void documentRemoved(SingleDocumentModel document) {
        if (model.getNumberOfDocuments() == 0) {
            actions.forEach(action -> action.setEnabled(false));
        }
    }
}
