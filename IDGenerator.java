package com.banking.kyc.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * IDGenerator - Generates unique IDs for customers and accounts
 */
public class IDGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Generates a unique Customer ID
     * Format: CUST-YYYYMMDD-XXXXXX
     */
    public static String generateCustomerId() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = generateRandom(6);
        return "CUST-" + datePart + "-" + randomPart;
    }

    /**
     * Generates a unique Account Number
     * Format: 12-digit numeric (similar to Indian bank account numbers)
     */
    public static String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        // First 4 digits: bank code
        sb.append("1234");
        // Next 4 digits: branch code
        sb.append("5678");
        // Last 8 digits: random
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Generates a KYC Reference Number
     * Format: KYC-YYYYMMDDHHMMSS-XXXX
     */
    public static String generateKYCReferenceNumber() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomPart = generateRandom(4);
        return "KYC-" + timePart + "-" + randomPart;
    }

    private static String generateRandom(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}
