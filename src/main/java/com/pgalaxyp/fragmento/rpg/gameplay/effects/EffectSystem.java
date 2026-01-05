package com.pgalaxyp.fragmento.rpg.gameplay.effects;

import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.Updatable;
import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.platform.api.visual.VisualWorld;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;
import java.util.Objects;
import java.util.Optional;

public final class EffectSystem implements Updatable {
    private final EffectRepository repo;
    private final WorldView world;
    private final Optional<VisualWorld> visuals;
    private final ActorRepository actors;

    public EffectSystem(EffectRepository repo, WorldView world, Optional<VisualWorld> visuals, ActorRepository actors) {
        this.repo = Objects.requireNonNull(repo);
        this.world = Objects.requireNonNull(world);
        this.visuals = Objects.requireNonNull(visuals);
        this.actors = Objects.requireNonNull(actors);
    }

    public void add(Effect e, GameTick tick, TickBus bus) {
        repo.put(e);
        e.start(new EffectWorld(tick, bus, world, visuals, actors));
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
        var snapshot = repo.snapshot();
        for (var entry : snapshot.entrySet()) {
            var e = entry.getValue();
            var alive = e.update(new EffectWorld(tick, bus, world, visuals, actors));
            if (!alive) {
                repo.remove(entry.getKey());
                e.end(new EffectWorld(tick, bus, world, visuals, actors));
            }
        }
    }
}