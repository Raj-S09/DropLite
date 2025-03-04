package com.droplite.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtils {

    private static final SecureRandom secureRandom = new SecureRandom();
    public static final int BYTE_LENGTH = 32;

    public static String generateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
