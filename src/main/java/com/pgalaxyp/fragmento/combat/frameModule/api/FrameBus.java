package com.pgalaxyp.fragmento.combat.frameModule.api;

import java.util.*;

public final class FrameBus {
    private final List<Object> intents;
    private final List<FrameEvent> events = new ArrayList<>();
    private final Map<Class<?>, Object> views = new HashMap<>();

    public FrameBus(List<?> intents) { this.intents = List.copyOf(Objects.requireNonNull(intents)); }
    public List<?> intents() { return intents; }

    public <T> List<T> intents(Class<T> type) {
        Objects.requireNonNull(type);
        if (intents.isEmpty()) return List.of();
        ArrayList<T> out = null;
        for (Object o : intents) {
            if (!type.isInstance(o)) continue;
            if (out == null) out = new ArrayList<>();
            out.add(type.cast(o));
        }
        return out == null ? List.of() : List.copyOf(out);
    }

    public void publish(FrameEvent event) { events.add(Objects.requireNonNull(event)); }
    public List<FrameEvent> events() { return List.copyOf(events); }

    public <T extends FrameEvent> List<T> events(Class<T> type) {
        Objects.requireNonNull(type);
        if (events.isEmpty()) return List.of();
        ArrayList<T> out = null;
        for (FrameEvent e : events) {
            if (!type.isInstance(e)) continue;
            if (out == null) out = new ArrayList<>();
            out.add(type.cast(e));
        }
        return out == null ? List.of() : List.copyOf(out);
    }

    public <T> void view(Class<T> type, T value) {
        views.put(Objects.requireNonNull(type), Objects.requireNonNull(value));
    }

    public <T> Optional<T> viewOpt(Class<T> type) {
        Object v = views.get(Objects.requireNonNull(type));
        return v == null ? Optional.empty() : Optional.of(type.cast(v));
    }

    public Map<Class<?>, Object> views() {
        return views.isEmpty() ? Map.of() : Map.copyOf(views);
    }
}