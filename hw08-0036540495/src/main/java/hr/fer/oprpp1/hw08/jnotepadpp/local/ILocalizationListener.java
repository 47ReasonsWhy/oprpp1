package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Interface that represents a localization listener.
 * It has one method that is called when localization changes.
 *
 * @see ILocalizationProvider
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public interface ILocalizationListener {

    /**
     * Method called when localization changes.
     */
    void localizationChanged();
}
