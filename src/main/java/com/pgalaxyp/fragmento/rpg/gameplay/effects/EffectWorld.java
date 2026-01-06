package com.pgalaxyp.fragmento.rpg.gameplay.effects;

import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.state.ActorRepository;
import com.pgalaxyp.fragmento.rpg.platform.api.visual.VisualWorld;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;
import java.util.Objects;
import java.util.Optional;

public final class EffectWorld {
    private final GameTick tick;
    private final TickBus bus;
    private final WorldView worldView;
    private final Optional<VisualWorld> visualWorld;
    private final ActorRepository actors;

    public EffectWorld(GameTick tick, TickBus bus, WorldView worldView, Optional<VisualWorld> visualWorld, ActorRepository actors) {
        this.tick = Objects.requireNonNull(tick);
        this.bus = Objects.requireNonNull(bus);
        this.worldView = Objects.requireNonNull(worldView);
        this.visualWorld = Objects.requireNonNull(visualWorld);
        this.actors = Objects.requireNonNull(actors);
    }

    public GameTick tick() {
        return tick;
    }

    public TickBus bus() {
        return bus;
    }

    public WorldView world() {
        return worldView;
    }

    public Optional<VisualWorld> visuals() {
        return visualWorld;
    }

    public ActorRepository actors() {
        return actors;
    }
}