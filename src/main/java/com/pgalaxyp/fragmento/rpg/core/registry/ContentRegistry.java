package com.pgalaxyp.fragmento.rpg.core.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public final class ContentRegistry {
    private final Map<String, Object> defs = new HashMap<>();

    public <T> void register(String id, T def) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(def);
        if (defs.putIfAbsent(id, def) != null) {
            throw new IllegalStateException("Duplicate id: " + id);
        }
    }

    public <T> T get(String id, Class<T> type) {
        Objects.requireNonNull(type);
        var v = defs.get(id);
        if (v == null) throw new IllegalStateException("Missing id: " + id);
        return type.cast(v);
    }

    public void withAll(Consumer<Object> consumer) {
        Objects.requireNonNull(consumer);
        for (var v : defs.values()) consumer.accept(v);
    }
}