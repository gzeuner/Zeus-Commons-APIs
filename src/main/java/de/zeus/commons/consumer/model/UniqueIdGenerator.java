package de.zeus.commons.consumer.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 * The UniqueIdGenerator class is responsible for generating unique identifiers for DynamicJsonObject instances.
 * It uses SHA-256 hashing to ensure a unique and consistent ID based on the object's content and a timestamp.
 */
public class UniqueIdGenerator {

    private static final Log LOG = LogFactory.getLog(UniqueIdGenerator.class);

    /**
     * Generates a unique identifier based on the hash of the DynamicJsonObject and the current timestamp.
     *
     * @param jsonObject The DynamicJsonObject for which the unique ID is to be generated.
     * @return A unique identifier as a String.
     */
    public static String generateUniqueId(DynamicJsonObject jsonObject) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // String representation of the object for hashing
            String source = jsonObject.toString() + Instant.now().toString();

            // Generate the hash
            byte[] encodedHash = digest.digest(source.getBytes(StandardCharsets.UTF_8));

            // Convert the hash into a hexadecimal string representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            LOG.info("Unique ID generated for DynamicJsonObject.");
            // Limit the length to 255 characters if necessary
            return hexString.length() > 255 ? hexString.substring(0, 255) : hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error generating hash for Unique ID.", e);
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
