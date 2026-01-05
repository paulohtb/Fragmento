package com.pgalaxyp.fragmento.rpg_old.util;

public final class IdUtil {

    public static int clamp(int v, int min, int max) {
        if (v < min) return min;
        return Math.min(v, max);
    }

    private IdUtil() {}
}