package com.bidmaster.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern CONTACT_NO_PATTERN = Pattern.compile("^[0-9+\\-\\s]{7,15}$");
    
    /**
     * Checks if a string is not null and not empty
     * 
     * @param str The string to check
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validates a username
     * 
     * @param username The username to validate
     * @return true if the username is valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        return isNotEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Validates an email address
     * 
     * @param email The email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates a password
     * 
     * @param password The password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return isNotEmpty(password) && password.length() >= 8;
    }
    
    /**
     * Validates a contact number
     * 
     * @param contactNo The contact number to validate
     * @return true if the contact number is valid, false otherwise
     */
    public static boolean isValidContactNo(String contactNo) {
        return isNotEmpty(contactNo) && CONTACT_NO_PATTERN.matcher(contactNo).matches();
    }
    
    /**
     * Validates a number is positive
     * 
     * @param number The number to validate
     * @return true if the number is positive, false otherwise
     */
    public static boolean isPositive(double number) {
        return number > 0;
    }
    
    /**
     * Validates a number is non-negative
     * 
     * @param number The number to validate
     * @return true if the number is non-negative, false otherwise
     */
    public static boolean isNonNegative(double number) {
        return number >= 0;
    }
}
