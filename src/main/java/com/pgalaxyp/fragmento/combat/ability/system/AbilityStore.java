package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class AbilityStore {

    private final Map<ActorId, AbilityInstance> active = new HashMap<>();

    public boolean hasActive(ActorId actorId, long frame) {
        AbilityInstance a = active.get(actorId);
        return a != null && a.activeAt(frame);
    }

    public boolean hasLocked(ActorId actorId, long frame) {
        AbilityInstance a = active.get(actorId);
        return a != null && a.lockedAt(frame);
    }

    public Optional<AbilityInstance> activeOf(ActorId actorId, long frame) {
        AbilityInstance a = active.get(actorId);
        return a != null && a.activeAt(frame) ? Optional.of(a) : Optional.empty();
    }

    public void put(AbilityInstance instance) { active.put(instance.actorId(), instance); }

    public void evictExpired(long frame) { active.entrySet().removeIf(e -> frame > e.getValue().endFrame()); }

    public List<AbilityInstance> activeAll(long frame) {
        if (active.isEmpty()) return List.of();
        List<AbilityInstance> out = new ArrayList<>(active.size());
        for (AbilityInstance a : active.values()) if (a.activeAt(frame)) out.add(a);
        return List.copyOf(out);
    }

    public List<AbilityInstance> endedAt(long frame) {
        if (active.isEmpty()) return List.of();
        List<AbilityInstance> out = new ArrayList<>();
        for (AbilityInstance a : active.values()) if (a.endFrame() == frame) out.add(a);
        return List.copyOf(out);
    }
}