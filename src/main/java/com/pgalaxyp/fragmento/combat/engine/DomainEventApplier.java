package com.pgalaxyp.fragmento.combat.engine;


import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.actor.api.ActorState;
import com.pgalaxyp.fragmento.combat.actor.event.ActorRemoved;
import com.pgalaxyp.fragmento.combat.actor.event.ActorUpserted;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.damage.DamageApplied;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public final class DomainEventApplier {

    public static GameState applyAll(GameState base, Iterable<DomainEvent> events) {
        Objects.requireNonNull(base);
        Objects.requireNonNull(events);

        NavigableMap<ActorId, ActorState> actors = new TreeMap<>(base.actors());

        for (DomainEvent e : events) {
            if (e != null) applyOne(actors, e);
        }

        return new GameState(base.frame(), actors);
    }

    private static void applyOne(Map<ActorId, ActorState> actors, DomainEvent event) {

        if (event instanceof ActorUpserted up) {
            actors.put(up.actorId(), up.state());
            return;
        }

        if (event instanceof ActorRemoved removed) {
            actors.remove(removed.actorId());
            return;
        }

        if (event instanceof DamageApplied da) {
            ActorId targetId = da.targetActorId();
            ActorState prev = actors.get(targetId);
            if (prev == null) return;

            int cur = prev.healthHearts();
            int dmg = da.hearts();
            int next;
            if (cur <= 0) {
                next = 0;
            } else if (dmg >= cur) {
                next = 0;
            } else {
                next = Math.subtractExact(cur, dmg);
            }

            actors.put(targetId, new ActorState(prev.classId(), prev.equippedWeaponId(), next, prev.maxHealthHearts()));
        }
    }

    private DomainEventApplier() {}
}