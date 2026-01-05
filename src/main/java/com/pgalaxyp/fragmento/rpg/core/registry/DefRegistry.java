package com.pgalaxyp.fragmento.rpg.core.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class DefRegistry<T> {
    private final Map<String, T> defs = new HashMap<>();

    public void register(String id, T def) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(def);
        if (defs.putIfAbsent(id, def) != null) {
            throw new IllegalStateException("Duplicate id: " + id);
        }
    }

    public Optional<T> find(String id) {
        return Optional.ofNullable(defs.get(id));
    }

    public T get(String id) {
        var v = defs.get(id);
        if (v == null) throw new IllegalStateException("Missing id: " + id);
        return v;
    }
}