package com.pgalaxyp.fragmento.combat.engine;

import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.delta.StateDelta;
import com.pgalaxyp.fragmento.combat.flow.FlowPipeline;
import com.pgalaxyp.fragmento.combat.flow.FrameBus;
import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.transport.GameSnapshot;
import com.pgalaxyp.fragmento.combat.ability.api.AbilityFrameView;
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
        List<StateDelta> deltas = bus.deltas();

        GameState committed = StateDeltaApplier.applyAll(new GameState(frame, state.actors()), deltas);
        state = committed;

        world.apply(frame, committed, deltas);

        AbilityFrameView abilities = bus.viewOpt(AbilityFrameView.class).orElseGet(() -> new AbilityFrameView(List.of()));

        snapshots.publish(new GameSnapshot(frame, committed.actors(), abilities));
    }
}