package com.c1se22.publiclaundsmartsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorCode {
    // Basic errors (1000-1099)
    RESOURCE_NOT_FOUND(1000, "%s with id %s not found"),
    RESOURCE_ALREADY_EXISTS(1001, "%s with id %s already exists"),
    RESOURCE_NOT_AVAILABLE(1002, "%s with id %s is not available"),
    RESOURCE_NOT_ACTIVE(1003, "%s with id %s is not active"),
    USER_BANNED(1004, "User %s is banned"),
    USER_DELETED(1005, "User %s is deleted"),

    // Authentication errors (1100-1199)
    UNAUTHORIZED(1100, "Unauthorized access"),
    INVALID_CREDENTIALS(1101, "Invalid username or password"),
    TOKEN_EXPIRED(1102, "Authentication token expired"),

    // Payment errors (1200-1299)
    INSUFFICIENT_BALANCE(1200, "Insufficient balance. Required: %s, Available: %s"),
    PAYMENT_PROCESSING_ERROR(1201, "Payment processing failed: %s"),

    // Application errors (1300-1399)
    USER_ALREADY_HAS_PENDING_RESERVATION(1300, "User %s already has a pending reservation."),
    NO_PENDING_RESERVATION(1301, "No pending reservation found for user %s."),
    MACHINE_NOT_AVAILABLE(1301, "Machine %s is not available."),
    EXISTING_USERNAME_OR_EMAIL(1301, "Username or email already exists."),
    EXISTING_USERNAME(1302, "Username %s already exists."),
    EXISTING_EMAIL(1303, "Email %s already exists."),
    EXISTING_PHONE(1304, "Phone %s already exists."),
    PASSWORD_DOES_NOT_MATCH(1305, "Password does not match."),
    OTP_EXPIRED(1306, "OTP has expired."),
    INVALID_OTP(1307, "Invalid OTP."),
    RESET_TOKEN_EXPIRED(1308, "Password reset token has expired."),

    // Validation errors (2000-2999)
    FIELD_REQUIRED(2000, "Field is required"),
    FIELD_INVALID_FORMAT(2001, "Invalid format"),
    FIELD_MIN_LENGTH(2002, "Minimum length is %d characters"),
    FIELD_MAX_LENGTH(2003, "Maximum length is %d characters"),
    FIELD_MIN_VALUE(2004, "Minimum value is %d"),
    FIELD_MAX_VALUE(2005, "Maximum value is %d"),

    // Custom validation errors
    FIELD_INVALID_EMAIL(2200, "Invalid email format"),
    FIELD_INVALID_PHONE(2201, "Invalid phone number format"),
    FIELD_INVALID_DATE(2202, "Invalid date format"),
    FIELD_INVALID(2203, "Invalid %s format"),

    // API errors
    INVALID_JWT(2300, "Invalid JWT token"),
    EXPIRED_JWT(2301, "Expired JWT token"),
    INVALID_REFRESH_TOKEN(2302, "Invalid refresh token"),
    UNSUPPORTED_JWT(2303, "Unsupported JWT token"),
    INVALID_JWT_CLAIMS(2304, "JWT claim error"),

    // System errors (5000-5999)
    INTERNAL_ERROR(5000, "Internal server error"),
    SERVICE_UNAVAILABLE(5001, "Service temporarily unavailable"),
    DATABASE_ERROR(5002, "Database operation failed");

    @Getter
    private final int code;
    private final String messageTemplate;
    public String getMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}
