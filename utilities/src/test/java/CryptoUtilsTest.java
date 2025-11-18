import cat.uvic.teknos.dam.aeroadmin.utilities.security.CryptoUtils;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CryptoUtils} class.
 * Verifies hashing behavior including determinism and edge case handling.
 */
class CryptoUtilsTest {

    /**
     * Tests that the hash output is deterministic for the same input.
     */
    @Test
    void testDeterministicOutput() {
        String input = "hello world";
        String hash1 = CryptoUtils.hash(input);
        String hash2 = CryptoUtils.hash(input);
        assertEquals(hash1, hash2, "Hash must be deterministic for the same input.");
    }

    /**
     * Tests that different inputs produce different hash outputs.
     */
    @Test
    void testDifferentOutputForDifferentInput() {
        String hash1 = CryptoUtils.hash("hello world");
        String hash2 = CryptoUtils.hash("hello world!");
        assertNotEquals(hash1, hash2, "Different inputs should produce different hashes.");
    }

    /**
     * Verifies that the overloaded hash(String) and hash(byte[]) methods
     * produce the same result for equivalent inputs.
     */
    @Test
    void testOverloadedMethodGivesSameResult() {
        String text = "test bytes";
        String hash1 = CryptoUtils.hash(text);
        String hash2 = CryptoUtils.hash(text.getBytes(StandardCharsets.UTF_8));
        assertEquals(hash1, hash2, "Hash(String) and Hash(byte[]) must be identical for the same content.");
    }

    /**
     * Tests the handling of null input for both String and byte[] methods.
     * A null input should be treated as an empty input.
     */
    @Test
    void testHandleNullInput() {
        String hash1 = CryptoUtils.hash((String) null);
        String hash2 = CryptoUtils.hash((byte[]) null);
        String hash3 = CryptoUtils.hash(new byte[0]);

        assertNotNull(hash1, "Hash of null String should not be null.");
        assertNotNull(hash2, "Hash of null byte[] should not be null.");
        assertEquals(hash1, hash3, "Hash of null String should equal hash of empty byte[].");
        assertEquals(hash2, hash3, "Hash of null byte[] should equal hash of empty byte[].");
    }

    /**
     * Tests the handling of empty string and empty byte array inputs.
     */
    @Test
    void testHandleEmptyInput() {
        String hash1 = CryptoUtils.hash("");
        String hash2 = CryptoUtils.hash(new byte[0]);
        assertNotNull(hash1, "Hash of empty string should not be null.");
        assertEquals(hash1, hash2, "Hash of empty string should equal hash of empty byte[].");
    }
}