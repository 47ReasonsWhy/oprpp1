package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Singleton class, loads the language from the bundle and notifies all listeners when the language is changed.
 * <p>
 * It is recommended to use {@link LocalizationProviderBridge} and {@link FormLocalizationProvider} instead of this class,
 * and only connect/disconnect them to the {@link LocalizationProvider}.
 *
 * @see AbstractLocalizationProvider
 * @see ILocalizationListener
 * @see ILocalizationProvider
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * Only instance of this class.
     */
    private static final LocalizationProvider instance = new LocalizationProvider();

    /**
     * Current language.
     */
    private String language;

    /**
     * Currently loaded translation bundle.
     */
    private ResourceBundle bundle;

    /**
     * Constructs a new {@link LocalizationProvider} object.
     * <p>
     * Sets the language to english and loads the bundle.
     */
    private LocalizationProvider() {
        language = "en";
        bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.translations", new Locale(language));
    }

    /**
     * Returns the only instance of this class.
     *
     * @return class instance
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * Sets the language to the given language and loads the bundle.
     * <p>
     * Notifies all listeners that the language has changed.
     *
     * @param language language to be set
     * @throws NullPointerException if the given language is null
     * @throws IllegalArgumentException if the given language is not supported
     */
    public void setLanguage(String language) {
        if (language == null) {
            throw new NullPointerException("Language can't be null");
        }
        if (language.equals(this.language)) {
            return;
        }
        this.language = language;
        try {
            bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.translations", new Locale(language));
        } catch (MissingResourceException e) {
            throw new IllegalArgumentException("Language not supported");
        }
        fire();
    }

    /**
     * Returns the current language.
     *
     * @return current language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Returns the translation for the given string.
     *
     * @param string string to be translated
     * @return translated string
     */
    public String getString(String string) {
        try {
            return bundle.getString(string);
        } catch (NullPointerException e) {
            throw new NullPointerException("String can't be null");
        } catch (MissingResourceException e) {
            throw new IllegalArgumentException("String not found");
        } catch (ClassCastException e) {
            throw new ClassCastException("Found value for the given key is not string");
        }
    }

    @Override
    public String getCurrentLanguage() {
        return language;
    }

    @Override
    public void fire() {
        for (ILocalizationListener listener : listeners) {
            listener.localizationChanged();
        }
    }
}
