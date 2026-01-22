package com.pgalaxyp.fragmento.combat.engine;

import com.pgalaxyp.fragmento.combat.actor.*;
import com.pgalaxyp.fragmento.combat.core.ids.ClassId;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.damage.DamageApplied;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.*;

public final class DomainEventApplier {
    public static GameState applyAll(GameState base, Iterable<DomainEvent> events) {
        Objects.requireNonNull(base);
        Objects.requireNonNull(events);

        NavigableMap<ActorId, ActorState> actors = new TreeMap<>(base.actors());

        for (DomainEvent e : events) { applyOne(actors, e); }

        return new GameState(base.frame(), actors);
    }

    private static void applyOne(Map<ActorId, ActorState> actors, DomainEvent event) {
        if (event instanceof ActorSpawned(ActorId actorId, ClassId classId, WeaponId equippedWeaponId, int healthHearts, int maxHealthHearts)) {
            if (actors.containsKey(actorId)) return;
            actors.put(actorId, ActorState.withEquippedWeapon(classId, equippedWeaponId, healthHearts, maxHealthHearts));
            return;
        }
        if (event instanceof DamageApplied(ActorId targetActorId, int hearts)) {
            ActorState prev = actors.get(targetActorId);
            if (prev == null) return;

            int next = Math.max(0, prev.healthHearts() - hearts);
            actors.put(targetActorId, new ActorState(prev.classId(), prev.equippedWeaponId(), next, prev.maxHealthHearts()));
        }
    }

    private DomainEventApplier() {}
}