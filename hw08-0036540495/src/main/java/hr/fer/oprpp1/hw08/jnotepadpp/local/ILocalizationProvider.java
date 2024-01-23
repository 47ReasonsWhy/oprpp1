package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Interface ILocalizationProvider represents a localization provider.
 * It is used to get the current language and to get the string for the given key.
 *
 * @see ILocalizationListener
 * @see AbstractLocalizationProvider
 * @see LocalizationProviderBridge
 * @see LocalizationProvider
 * @see FormLocalizationProvider
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface ILocalizationProvider {

    /**
     * Adds a localization listener.
     *
     * @param listener listener to add
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Removes a localization listener.
     *
     * @param listener listener to remove
     */
    void removeLocalizationListener(ILocalizationListener listener);

    /**
     * Gets the string for the given key.
     *
     * @param string key
     * @return string for the given key
     */
    String getString(String string);

    /**
     * Gets the current language.
     *
     * @return current language
     */
    String getCurrentLanguage();
}
