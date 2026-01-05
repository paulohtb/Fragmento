package com.pgalaxyp.fragmento.rpg.util;

import java.util.Map;

public final class MapUtil {

    public static <K, V> V getOrNull(Map<K, V> map, K key) {
        if (map == null) return null;
        return map.get(key);
    }

    private MapUtil() {}
}