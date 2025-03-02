package com.droplite.constant;

public class UserConstants {

    private UserConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SUCCESS_MSG_USER_CREATED = "User created successfully";
    public static final String SUCCESS_MSG_USER_FETCHED = "User fetched successfully";
    public static final String ERROR_MSG_USER_NOT_FOUND = "User not found";
    public static final String ERROR_MSG_USERNAME_TAKEN = "Username already taken";
    public static final String ROLE_USER = "USER";
}
