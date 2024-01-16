package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class containing utility methods for {@link hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP}.
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Util {
    /**
     * Loads an image icon from the given path.
     *
     * @param path path to the icon
     * @return image icon
     * @throws RuntimeException if an error occurs while loading the icon
     * @throws NullPointerException if the given path is null
     */
    public static ImageIcon loadImageIcon(String path) {
        if (path == null) {
            throw new NullPointerException("Path cannot be null.");
        }
        try (InputStream is = Util.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Error while loading icon.");
            }
            byte[] bytes;
            bytes = is.readAllBytes();
            return new ImageIcon(bytes);
        } catch (IOException ex) {
            throw new RuntimeException("Error while loading icon.");
        }
    }
}
