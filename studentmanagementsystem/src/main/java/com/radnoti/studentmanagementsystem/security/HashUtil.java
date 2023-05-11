package com.radnoti.studentmanagementsystem.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
@Component
public class HashUtil {

    /**
     * Generates a SHA-256 hash for the given text.
     *
     * @param text The text to be hashed.
     * @return The SHA-256 hash of the text.
     * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
     */
    public String getSHA256Hash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Generates a random string of the specified length.
     *
     * @param length The length of the random string.
     * @return The generated random string.
     */
    public String generateRandomString(int length) {
        String randomString = UUID.randomUUID().toString().replaceAll("-", "");
        return randomString.substring(0, length);
    }
}
