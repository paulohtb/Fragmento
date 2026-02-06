package com.pgalaxyp.fragmento.combat.frameModule.api;

import java.util.*;

public final class FrameBus {
    private final ArrayList<FrameCommand> commands;
    private final ArrayList<FrameEvent> events = new ArrayList<>();
    private final Map<Class<?>, Object> views = new HashMap<>();
    private Map<Class<?>, List<?>> commandCache;
    private Map<Class<?>, List<?>> eventCache;

    public FrameBus(List<? extends FrameCommand> initialCommands) { this(initialCommands, Map.of()); }

    public FrameBus(List<? extends FrameCommand> initialCommands, Map<Class<?>, ?> initialViews) {
        this.commands = new ArrayList<>(Objects.requireNonNull(initialCommands));
        Objects.requireNonNull(initialViews).forEach((k, v) -> views.put(Objects.requireNonNull(k), Objects.requireNonNull(v)));
    }

    public List<FrameCommand> commands() { return Collections.unmodifiableList(commands); }

    public <T extends FrameCommand> List<T> commands(Class<T> type) {
        Objects.requireNonNull(type);
        var cache = commandCache;
        if (cache == null) commandCache = cache = new HashMap<>();
        @SuppressWarnings("unchecked")
        var hit = (List<T>) cache.get(type);
        if (hit != null) return hit;
        var out = filter(type, commands);
        cache.put(type, out);
        return out;
    }

    public void command(FrameCommand command) {
        commands.add(Objects.requireNonNull(command));
        commandCache = null;
    }

    public void publish(FrameEvent event) {
        events.add(Objects.requireNonNull(event));
        eventCache = null;
    }

    public List<FrameEvent> events() { return Collections.unmodifiableList(events); }

    public <T extends FrameEvent> List<T> events(Class<T> type) {
        Objects.requireNonNull(type);
        var cache = eventCache;
        if (cache == null) eventCache = cache = new HashMap<>();
        @SuppressWarnings("unchecked")
        var hit = (List<T>) cache.get(type);
        if (hit != null) return hit;
        var out = filter(type, events);
        cache.put(type, out);
        return out;
    }

    public <T> void view(Class<T> type, T value) { views.put(Objects.requireNonNull(type), Objects.requireNonNull(value)); }

    public <T> Optional<T> viewOpt(Class<T> type) {
        var v = views.get(Objects.requireNonNull(type));
        return v == null ? Optional.empty() : Optional.of(type.cast(v));
    }

    public Map<Class<?>, Object> views() { return views.isEmpty() ? Map.of() : Collections.unmodifiableMap(views); }

    private static <T> List<T> filter(Class<T> type, List<?> src) {
        if (src.isEmpty()) return List.of();
        ArrayList<T> out = null;
        for (var o : src) {
            if (!type.isInstance(o)) continue;
            if (out == null) out = new ArrayList<>();
            out.add(type.cast(o));
        }
        return out == null ? List.of() : Collections.unmodifiableList(out);
    }
}