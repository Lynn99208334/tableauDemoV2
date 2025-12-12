package com.example.tableaudemov2.enums;

public enum ErrorCode {

    VALIDATION_ERROR("E40001", "Validation Failed"),
    BUSINESS_ERROR("E40002", "Business Error"),
    JWT_EXPIRED("E40101", "JWT Token Expired"),
    JWT_INVALID("E40102", "Invalid JWT Token"),
    ACCESS_DENIED("E40301", "Access Denied"),
    NOT_FOUND("E40401", "Resource Not Found"),
    INTERNAL_ERROR("E50001", "Internal Server Error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}