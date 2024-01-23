package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * Localizable {@link JMenu}, tracks changes in localization and updates its text accordingly.
 *
 * @see ILocalizationProvider
 * @see ILocalizationListener
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class LJMenu extends JMenu {

    /**
     * Constructs a new {@link LJMenu} with the given key and localization provider.
     *
     * @param key key for the localization provider
     * @param localizationProvider localization provider
     */
    public LJMenu(String key, ILocalizationProvider localizationProvider) {
        super(localizationProvider.getString(key));
        ILocalizationListener listener = () -> setText(localizationProvider.getString(key));
        localizationProvider.addLocalizationListener(listener);
    }
}
