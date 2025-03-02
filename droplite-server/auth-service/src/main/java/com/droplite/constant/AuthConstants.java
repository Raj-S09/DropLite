package com.droplite.constant;

public class AuthConstants {

    private AuthConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SUCCESS_MSG_LOGIN_SUCCESSFUL = "Logged in successfully";
    public static final String SUCCESS_MSG_LOGOUT_SUCCESSFUL = "Logged out successfully";
    public static final String ERROR_MSG_LOGIN_FAILED = "Invalid credentials provided";
    public static final String ERROR_MSG_REGISTER_FAILED = "Can't register user";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_CLAIMS_AUTHORITY = "role";
}
