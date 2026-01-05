package com.pgalaxyp.fragmento.rpg.core.loop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class TickBus {
    private final Map<Class<?>, List<Consumer<?>>> subs = new ConcurrentHashMap<>();

    public <T> AutoCloseable subscribe(Class<T> type, Consumer<T> handler) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(handler);
        subs.computeIfAbsent(type, __ -> new ArrayList<>()).add(handler);
        return () -> unsubscribe(type, handler);
    }

    public void publish(Object event) {
        if (event == null) return;

        var type = event.getClass();
        var handlers = subs.get(type);
        if (handlers == null || handlers.isEmpty()) return;

        for (var raw : List.copyOf(handlers)) {
            @SuppressWarnings("unchecked")
            var h = (Consumer<Object>) raw;
            h.accept(event);
        }
    }

    private <T> void unsubscribe(Class<T> type, Consumer<T> handler) {
        var handlers = subs.get(type);
        if (handlers == null) return;
        handlers.remove(handler);
        if (handlers.isEmpty()) subs.remove(type);
    }
}