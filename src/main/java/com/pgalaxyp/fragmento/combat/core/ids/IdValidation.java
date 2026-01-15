package com.pgalaxyp.fragmento.combat.core.ids;

import java.util.Objects;

public final class IdValidation {

    public static String normalizedKey(String value) {
        Objects.requireNonNull(value);
        String v = value.trim();
        if (v.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (v.length() > 128) {
            throw new IllegalArgumentException();
        }
        if (!isLowerAlpha(v.charAt(0))) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (isLowerAlpha(c) || isDigit(c) || c == '.' || c == '_') {
                continue;
            }
            throw new IllegalArgumentException();
        }
        return v;
    }

    private static boolean isLowerAlpha(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private IdValidation() {}
}