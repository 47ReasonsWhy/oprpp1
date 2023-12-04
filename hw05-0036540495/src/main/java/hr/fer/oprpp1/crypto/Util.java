package hr.fer.oprpp1.crypto;

/**
 * This class contains two static methods for converting between byte arrays and hexadecimal strings.
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Util {

    /**
     * Converts a hexadecimal string to a byte array.
     *
     * @param keyText hexadecimal string
     * @return byte array representation of the hexadecimal string
     * @throws NullPointerException if the key text is null
     * @throws IllegalArgumentException if the number of characters in the key text is odd
     *                                  or if the key text contains non-hexadecimal characters
     */
    public static byte[] hexToByte(String keyText) {
        if (keyText == null) {
            throw new NullPointerException("The key text must not be null.");
        }
        if (keyText.length() % 2 != 0) {
            throw new IllegalArgumentException("The number of characters in the key text must be even.");
        }
        byte[] key = new byte[keyText.length() / 2];
        for (int i = 0; i < keyText.length(); i += 2) {
            if (Character.digit(keyText.charAt(i), 16) == -1 ||
                Character.digit(keyText.charAt(i + 1), 16) == -1) {
                    throw new IllegalArgumentException("The key text must contain only hexadecimal digits.");
            }
            try {
                key[i / 2] = (byte) Integer.parseInt(keyText.substring(i, i + 2), 16);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("This should never happen.");
            }
        }
        return key;
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param array byte array
     * @return hexadecimal string representation of the byte array
     * @throws NullPointerException if the byte array is null
     */
    public static String byteToHex(byte[] array) {
        if (array == null) {
            throw new NullPointerException("The byte array must not be null.");
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
