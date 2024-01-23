package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Bridge to {@link ILocalizationProvider} parent, able to connect to it and disconnect from it.
 * <p>
 * When connected, it will forward all translation requests to its parent.
 * When disconnected, it will not forward any localization requests.
 *
 * @see ILocalizationProvider
 * @see AbstractLocalizationProvider
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    /**
     * Flag indicating whether this bridge is connected to its parent.
     */
    private boolean connected;

    /**
     * Listener that will be added to parent when connected.
     */
    private final ILocalizationListener listener;

    /**
     * Parent localization provider.
     */
    private final ILocalizationProvider parent;

    /**
     * Current language.
     */
    private String currentLanguage;

    /**
     * Creates new {@link LocalizationProviderBridge} with given parent.
     *
     * @param parent parent localization provider
     * @throws NullPointerException if parent is null
     */
    public LocalizationProviderBridge(ILocalizationProvider parent) {
        if (parent == null) throw new NullPointerException("Parent cannot be null!");
        this.parent = parent;
        this.connected = false;
        this.listener = this::fire;
        this.currentLanguage = parent.getCurrentLanguage();
    }

    /**
     * Connects the bridge to its parent.
     * <p>
     * If already connected, does nothing.
     * <p>
     * Fires the localization change event if current language is different from parent's current language.
     */
    public void connect() {
        if (connected) return;
        connected = true;
        parent.addLocalizationListener(listener);
        if (!currentLanguage.equals(parent.getCurrentLanguage())) {
            fire();
        }
    }

    /**
     * Disconnects the bridge from its parent.
     * <p>
     * If already disconnected, does nothing.
     */
    public void disconnect() {
        if (!connected) return;
        connected = false;
        parent.removeLocalizationListener(listener);
    }

    @Override
    public String getString(String string) {
        return parent.getString(string);
    }

    @Override
    public String getCurrentLanguage() {
        return parent.getCurrentLanguage();
    }

    @Override
    public void fire() {
        for (ILocalizationListener listener : listeners) {
            listener.localizationChanged();
        }
        this.currentLanguage = parent.getCurrentLanguage();
    }
}
