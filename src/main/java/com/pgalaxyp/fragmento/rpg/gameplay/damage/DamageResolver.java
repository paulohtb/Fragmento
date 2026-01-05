package com.pgalaxyp.fragmento.rpg.gameplay.damage;

import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.Updatable;
import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public final class DamageResolver implements Updatable {
    private final ActorRepository actors;
    private final Queue<DamageRequest> queue = new ArrayDeque<>();

    public DamageResolver(ActorRepository actors, TickBus bus) {
        this.actors = Objects.requireNonNull(actors);

        bus.subscribe(DamageRequest.class, queue::add);
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
        while (!queue.isEmpty()) {
            var r = queue.poll();
            var targetHealth = actors.health(r.targetActorId());
            if (!targetHealth.alive()) continue;
            targetHealth.apply(r.amount());
        }
    }
}