package com.pgalaxyp.fragmento.combat.core.state;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import java.util.*;

public record GameState(
        FrameContext frame,
        NavigableMap<ActorId, ActorState> actors,
        NavigableMap<ActorId, NavigableMap<AbilityId, Long>> cooldowns
) {
    public GameState {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
        Objects.requireNonNull(cooldowns);

        for (var e : actors.entrySet()) if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();

        var cd = new TreeMap<ActorId, NavigableMap<AbilityId, Long>>();
        for (var e : cooldowns.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) throw new IllegalArgumentException();
            var inner = new TreeMap<AbilityId, Long>();
            for (var ce : e.getValue().entrySet()) {
                if (ce.getKey() == null || ce.getValue() == null) throw new IllegalArgumentException();
                long end = ce.getValue();
                if (end < 0) throw new IllegalArgumentException();
                inner.put(ce.getKey(), end);
            }
            cd.put(e.getKey(), Collections.unmodifiableNavigableMap(inner));
        }

        actors = Collections.unmodifiableNavigableMap(new TreeMap<>(actors));
        cooldowns = Collections.unmodifiableNavigableMap(cd);
    }

    public Optional<ActorState> findActor(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        return Optional.ofNullable(actors.get(actorId));
    }

    public ActorState actor(ActorId actorId) {
        ActorState state = actors.get(actorId);
        if (state == null) throw new IllegalArgumentException();
        return state;
    }

    public Optional<Long> cooldownEnd(ActorId actorId, AbilityId abilityId) {
        if (actorId == null || abilityId == null) throw new IllegalArgumentException();
        var map = cooldowns.get(actorId);
        if (map == null) return Optional.empty();
        return Optional.ofNullable(map.get(abilityId));
    }

    public boolean cooldownActive(ActorId actorId, AbilityId abilityId, long frameId) {
        if (actorId == null || abilityId == null) throw new IllegalArgumentException();
        if (frameId < 0) throw new IllegalArgumentException();
        Long end = cooldownEnd(actorId, abilityId).orElse(null);
        return end != null && frameId < end;
    }

    public static GameState empty(FrameContext frame) { return new GameState(frame, new TreeMap<>(), new TreeMap<>()); }
}