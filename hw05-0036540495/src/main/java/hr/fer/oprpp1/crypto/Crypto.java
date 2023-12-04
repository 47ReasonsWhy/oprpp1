package hr.fer.oprpp1.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import static hr.fer.oprpp1.crypto.Util.hexToByte;

/**
 * A program that can be used for checking SHA-256 file digest and encrypting/decrypting files using the AES crypto-algorithm.
 * <p>
 * The first argument is the command that should be executed. The program supports 3 commands: checksha, encrypt and decrypt.
 * <p>
 * The checksha command expects 1 additional argument: the path to the file whose digest should be checked.
 * Then, the program will ask the user to provide the expected digest.
 * <p>
 * The encrypt and decrypt commands expect 2 additional arguments: the path to the file that should be encrypted/decrypted
 * and the path to the file where the result should be stored.
 * Then, the program will ask the user to provide the password and initialization vector.
 * The password must be a 32-character hex-encoded string.
 * The initialization vector must be a 32-character hex-encoded string.
 * <p>
 * The program will print the result of the operation to the standard output, or an error message if an error occurs.
 *
 * @see Util
 * @see MessageDigest
 * @see Cipher
 * @see SecretKeySpec
 * @see IvParameterSpec
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Crypto {
    public static void main(String[] args) {
        // check arguments
        if (args.length == 0) {
            System.out.println("No arguments provided.");
            return;
        }
        switch (args[0]) {
            case "checksha" -> {
                if (args.length != 2) {
                    System.out.println("Invalid number of arguments.");
                    return;
                }
                Path file = Path.of(args[1]);
                if (!Files.exists(file)) {
                    System.out.println("The file does not exist.");
                    return;
                }
                if (!Files.isRegularFile(file)) {
                    System.out.println("The file is not a regular file.");
                    return;
                }
                if (!Files.isReadable(file)) {
                    System.out.println("The file is not readable.");
                    return;
                }
            }
            case "encrypt", "decrypt" -> {
                if (args.length != 3) {
                    System.out.println("Invalid number of arguments.");
                    return;
                }
                Path file = Path.of(args[1]);
                try {
                    if (!Files.exists(file)) {
                        System.out.println("File '" + file + "' does not exist.");
                        return;
                    }
                    if (Files.isDirectory(file)) {
                        System.out.println("File '" + file + "' is a directory.");
                        return;
                    }
                    if (!Files.isRegularFile(file)) {
                        System.out.println("File '" + file + "' is not a regular file.");
                        return;
                    }
                    if (!Files.isReadable(file)) {
                        System.out.println("File '" + file + "' is not readable.");
                        return;
                    }
                } catch (SecurityException e) {
                    System.out.println("Security manager denies access to file '" + file + "': " + e.getMessage());
                    return;
                }
            }
            default -> {
                System.out.println("Invalid command.");
                return;
            }
        }

        switch (args[0]) {
            case "checksha" -> {
                System.out.print("Please provide expected sha-256 digest for " + args[1] + ":\n> ");
                Scanner sc = new Scanner(System.in);
                String expectedDigest = sc.nextLine();
                String actualDigest = null;
                Path file = Path.of(args[1]);
                try (InputStream is = Files.newInputStream(file)) {
                    MessageDigest sha = MessageDigest.getInstance("SHA-256");
                    byte[] buffer = new byte[4096];
                    while (true) {
                        int r = is.read(buffer);
                        if (r < 1) break;
                        sha.update(buffer, 0, r);
                    }
                    actualDigest = Util.byteToHex(sha.digest());
                } catch (IOException e) {
                    System.err.println("Error while opening file: " + e.getMessage());
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                if (expectedDigest.equals(actualDigest)) {
                    System.out.println("Digesting completed. Digest of " + args[1] + " matches expected digest.");
                } else {
                    System.out.println("Digesting completed. Digest of " + args[1] + " does not match the expected digest. Digest was: " + actualDigest);
                }
            }
            case "encrypt", "decrypt" -> {
                boolean encrypt = args[0].equals("encrypt");
                System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
                Scanner sc = new Scanner(System.in);
                String keyText = sc.nextLine();
                System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
                String ivText = sc.nextLine();
                Path file = Path.of(args[1]);
                Path file2 = Path.of(args[2]);
                try (InputStream is = Files.newInputStream(file);
                     OutputStream os = Files.newOutputStream(file2)) {
                        SecretKeySpec keySpec = new SecretKeySpec(hexToByte(keyText), "AES");
                        AlgorithmParameterSpec paramSpec = new IvParameterSpec(hexToByte(ivText));
                        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
                        byte[] buffer = new byte[4096];
                        while (true) {
                            int r = is.read(buffer);
                            if (r < 1) break;
                            os.write(cipher.update(buffer, 0, r));
                        }
                        os.write(cipher.doFinal());
                        System.out.println((encrypt ? "Encryption" : "Decryption") + " completed. Generated file " + args[2] + " based on file " + args[1] + ".");
                } catch (IOException e) {
                        System.err.println("Error while opening file: " + e.getMessage());
                } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                    System.err.println("Error while creating cipher: " + e.getMessage());
                } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
                        System.err.println("Error while initializing cipher: " + e.getMessage());
                } catch (IllegalStateException e) {
                    System.err.println("Error while " + (encrypt ? "encrypting" : "decrypting") + " file (updating): " + e.getMessage());
                } catch (IllegalBlockSizeException | BadPaddingException e) {
                        System.err.println("Error while " + (encrypt ? "encrypting" : "decrypting") + " file (finalizing): " + e.getMessage());
                }
            }
        }
    }
}
