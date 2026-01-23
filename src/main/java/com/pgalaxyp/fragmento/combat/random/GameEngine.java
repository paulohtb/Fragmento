package com.pgalaxyp.fragmento.combat.random;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import java.util.List;
import java.util.Objects;

public final class GameEngine {
    private final com.pgalaxyp.fragmento.combat.inputModule.port.IntentSourcePort intents;
    private final FlowPipeline pipeline;
    private final WorldCommandPort world;
    private final SnapshotPort snapshots;
    private GameState state;
    private long nextFrame;

    public GameEngine(com.pgalaxyp.fragmento.combat.inputModule.port.IntentSourcePort intents, FlowPipeline pipeline, WorldCommandPort world, SnapshotPort snapshots, GameState initial) {
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

        var events = bus.events();

        ActorView committedActors = bus.viewOpt(ActorView.class).orElse(state.actors());
        GameState committed = new GameState(frame, committedActors);

        state = committed;
        world.apply(frame, committed, events);

        AbilityViewSnapshot abilities = bus.viewOpt(AbilityViewSnapshot.class).orElseGet(() -> new AbilityViewSnapshot(List.of()));
        snapshots.publish(new GameSnapshot(frame, committedActors, abilities));
    }
}