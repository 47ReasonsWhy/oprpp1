package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

/**
 * Extension of {@link LocalizationProviderBridge} that connects/disconnects itself from the parent
 * {@link ILocalizationProvider} when the window is opened/closed.
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    /**
     * Creates a new {@link FormLocalizationProvider} with the given parent and frame.
     *
     * @param parent parent {@link ILocalizationProvider}
     * @param frame  frame
     */
    public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
        super(parent);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowOpened(java.awt.event.WindowEvent windowEvent) {
                connect();
            }

            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                disconnect();
            }
        });
    }
}
