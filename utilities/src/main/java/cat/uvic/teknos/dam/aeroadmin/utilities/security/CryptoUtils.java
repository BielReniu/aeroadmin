package cat.uvic.teknos.dam.aeroadmin.utilities.security;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * Provides cryptographic hashing utilities for message integrity verification.
 * <p>
 * This class reads a hashing algorithm and a salt from a {@code crypto.properties}
 * file located in the classpath. It is designed as a static utility class.
 */
public class CryptoUtils {

    private static final String ALGORITHM;
    private static final byte[] SALT;
    private static final String PROPERTIES_FILE = "/crypto.properties";

    // Static initializer to load configuration once.
    static {
        try (InputStream input = CryptoUtils.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Cannot find " + PROPERTIES_FILE + " in classpath.");
            }
            Properties props = new Properties();
            props.load(input);

            ALGORITHM = props.getProperty("hash.algorithm");
            String saltStr = props.getProperty("hash.salt");

            if (ALGORITHM == null || saltStr == null) {
                throw new RuntimeException("hash.algorithm or hash.salt not defined in " + PROPERTIES_FILE);
            }

            SALT = saltStr.getBytes(StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error loading " + PROPERTIES_FILE, e);
        }
    }

    /**
     * Hashes a given plainText String after converting it to UTF-8 bytes.
     *
     * @param plainText The input string to hash. If null, it is treated as an empty input.
     * @return The resulting hash as a hexadecimal string.
     */
    public static String hash(String plainText) {
        if (plainText == null) {
            return hash((byte[]) null);
        }
        return hash(plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Hashes a given byte array using the configured algorithm and salt.
     * <p>
     * This is the core hashing method. It prepends the configured salt to the input
     * data before digesting.
     *
     * @param bytes The input data (e.g., message body). If null or empty,
     * the hash is computed on the salt alone.
     * @return The resulting hash as a hexadecimal string.
     * @throws RuntimeException if the configured hash algorithm is not available.
     */
    public static String hash(byte[] bytes) {
        if (bytes == null) {
            bytes = new byte[0];
        }

        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);

            // 1. Apply salt
            md.update(SALT);

            // 2. Apply input data
            md.update(bytes);

            // 3. Calculate digest
            byte[] digest = md.digest();

            // 4. Convert to Hexadecimal
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // This should not happen if the algorithm in crypto.properties is valid (e.g., SHA-256)
            throw new RuntimeException("Configured hash algorithm is invalid: " + ALGORITHM, e);
        }
    }

    /**
     * Utility method to convert a byte array into a hexadecimal string.
     *
     * @param hash The byte array to convert.
     * @return A lowercase hexadecimal string representation.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}