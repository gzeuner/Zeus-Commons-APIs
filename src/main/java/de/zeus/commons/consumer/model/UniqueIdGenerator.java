package de.zeus.commons.consumer.model;

import de.zeus.commons.consumer.model.DynamicJsonObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class UniqueIdGenerator {

    public static String generateUniqueId(DynamicJsonObject jsonObject) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // String-Repräsentation des Objekts für das Hashing
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

            // Limit the length to 255 characters if necessary
            return hexString.length() > 255 ? hexString.substring(0, 255) : hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Fehler beim Generieren des Hashes", e);
        }
    }
}
