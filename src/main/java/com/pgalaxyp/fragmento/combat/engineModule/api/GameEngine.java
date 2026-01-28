package com.pgalaxyp.fragmento.combat.engineModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.port.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.inputModule.port.IntentSourcePort;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import java.util.*;

public final class GameEngine {
    private final IntentSourcePort intents;
    private final FlowPipeline pipeline;
    private final WorldCommandPort world;
    private final SnapshotPort snapshots;
    private GameState state;
    private long nextFrame;

    public GameEngine(IntentSourcePort intents, FlowPipeline pipeline, WorldCommandPort world, SnapshotPort snapshots, GameState initial) {
        this.intents = Objects.requireNonNull(intents);
        this.pipeline = Objects.requireNonNull(pipeline);
        this.world = Objects.requireNonNull(world);
        this.snapshots = Objects.requireNonNull(snapshots);
        this.state = Objects.requireNonNull(initial);
        this.nextFrame = initial.frame().frameId() + 1;
    }

    public void step(int tickIndex) {
        FrameContext frame = new FrameContext(nextFrame++, tickIndex);
        FrameBus bus = pipeline.run(frame, state, intents.drain());

        List<FrameEvent> events = bus.events();
        ActorView committedActors = bus.viewOpt(ActorView.class).orElse(state.actors());
        GameState committed = new GameState(frame, committedActors);

        state = committed;
        world.apply(frame, committed, events);

        AbilityViewSnapshot abilities = bus.viewOpt(AbilityViewSnapshot.class).orElseGet(() -> new AbilityViewSnapshot(List.of()));
        snapshots.publish(new GameSnapshot(frame, committedActors, abilities));
    }
}