package com.pgalaxyp.fragmento.combat.frameModule.api;

import java.util.*;
import java.util.function.Predicate;

public final class FrameBus {
    private final List<Object> intents;
    private final List<FrameEvent> events = new ArrayList<>();
    private final Map<Class<?>, Object> views = new HashMap<>();

    public FrameBus(List<?> intents) {
        this.intents = List.copyOf(Objects.requireNonNull(intents));
    }

    public List<?> intents() {
        return intents;
    }

    public <T> List<T> intents(Class<T> type) {
        return intents.stream().filter(type::isInstance).map(type::cast).toList();
    }

    public void publish(FrameEvent event) {
        events.add(Objects.requireNonNull(event));
    }

    public List<FrameEvent> events() {
        return List.copyOf(events);
    }

    public <T extends FrameEvent> List<T> events(Class<T> type) {
        return events.stream().filter(type::isInstance).map(type::cast).toList();
    }

    public void clearEvents(Predicate<FrameEvent> filter) {
        events.removeIf(Objects.requireNonNull(filter));
    }

    public <T> void view(Class<T> type, T value) {
        views.put(Objects.requireNonNull(type), Objects.requireNonNull(value));
    }

    public <T> Optional<T> viewOpt(Class<T> type) {
        Object v = views.get(Objects.requireNonNull(type));
        return v == null ? Optional.empty() : Optional.of(type.cast(v));
    }
}