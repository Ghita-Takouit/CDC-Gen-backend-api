package utils;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 */
public class ValidationUtils {
    
    // Email regex pattern - validates standard email format
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    
    // Password regex pattern - minimum 8 chars, at least one digit, lowercase, uppercase, and special char
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,}$");
    
    // Phone regex pattern - validates standard phone formats
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");
    
    /**
     * Checks if a string is null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates password strength
     */
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Validates phone number format
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}
