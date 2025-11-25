package com.pgalaxyp.fragmento.NEW;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CountdownMap<K> {

    private final Map<K, Integer> timers = new HashMap<>();

    public void start(K key, int ticks) {
        if (ticks <= 0) {
            timers.remove(key);
        } else {
            timers.put(key, ticks);
        }
    }

    public boolean isActive(K key) {
        return timers.containsKey(key);
    }

    public void tick() {
        Iterator<Map.Entry<K, Integer>> it = timers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, Integer> entry = it.next();
            int value = entry.getValue() - 1;
            if (value <= 0) {
                it.remove();
            } else {
                entry.setValue(value);
            }
        }
    }
}
