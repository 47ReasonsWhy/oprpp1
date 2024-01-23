package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Localizable {@link AbstractAction}, tracks changes in localization and updates its text accordingly.
 * <p>
 * It is expected that the user overrides {@link #actionPerformed(ActionEvent)} method.
 * Also, the properties file should contain the following keys (for each action using its own 'idn'):
 * <ul>
 *     <li>idn.name - name of the action</li>
 *     <li>idn.desc - description of the action</li>
 * </ul>
 *
 * @see ILocalizationProvider
 * @see ILocalizationListener
 * @see AbstractAction
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class LocalizableAction extends AbstractAction {

    /**
     * Constructs a new {@link LocalizableAction} with the given key and localization provider.
     *
     * @param idn action identifier
     * @param provider localization provider
     */
    public LocalizableAction(String idn, ILocalizationProvider provider) {
        ILocalizationListener listener = () -> {
            putValue(NAME, provider.getString(idn + ".name"));
            putValue(SHORT_DESCRIPTION, provider.getString(idn + ".desc"));
        };
        provider.addLocalizationListener(listener);
        listener.localizationChanged();
    }

    /**
     * Needs to be overridden by the user!
     *
     * @param e action event
     * @throws UnsupportedOperationException if the method is not overridden
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("actionPerformed() not implemented");
    }
}
