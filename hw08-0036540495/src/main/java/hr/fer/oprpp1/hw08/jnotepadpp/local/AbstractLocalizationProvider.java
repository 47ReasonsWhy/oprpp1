package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that implements ILocalizationProvider.
 * It stores listeners and methods for adding and removing them,
 * as well as a method for notifying all listeners that the localization has changed.
 *
 * @see ILocalizationProvider
 * @see ILocalizationListener
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    /**
     * List of listeners.
     */
    protected final List<ILocalizationListener> listeners;

    /**
     * Default constructor.
     */
    public AbstractLocalizationProvider() {
        listeners = new ArrayList<>();
    }

    /**
     * Adds listener to the list.
     *
     * @param listener listener to be added
     * @throws NullPointerException if listener is null
     */
    public void addLocalizationListener(ILocalizationListener listener) {
        if (listener == null) throw new NullPointerException("Listener can't be null.");
        listeners.add(listener);
    }

    /**
     * Removes listener from the list.
     *
     * @param listener listener to be removed
     * @throws NullPointerException if listener is null
     */
    public void removeLocalizationListener(ILocalizationListener listener) {
        if (listener == null) throw new NullPointerException("Listener can't be null.");
        listeners.remove(listener);
    }

    /**
     * Notifies all listeners that the localization has changed.
     */
    public abstract void fire();
}
