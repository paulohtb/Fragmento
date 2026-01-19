package com.pgalaxyp.fragmento.combat.engine;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.engine.commit.*;
import com.pgalaxyp.fragmento.combat.engine.system.*;
import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.*;
import java.util.*;

public final class GameEngine {

    private final IntentSourcePort intents;
    private final GameContent content;
    private final CombatFlowProcessor flow;
    private final WorldCommandPort world;
    private final SnapshotPort snapshots;

    private GameState state;
    private long nextFrame;

    public GameEngine(IntentSourcePort intents, GameContent content, CombatFlowProcessor flow, WorldCommandPort world, SnapshotPort snapshots, GameState initial) {
        this.intents = Objects.requireNonNull(intents);
        this.content = Objects.requireNonNull(content);
        this.flow = Objects.requireNonNull(flow);
        this.world = Objects.requireNonNull(world);
        this.snapshots = Objects.requireNonNull(snapshots);
        this.state = Objects.requireNonNull(initial);
        this.nextFrame = initial.frame().frameId() + 1;
    }

    public FrameOutput step(int tickIndex) {
        FrameContext frame = new FrameContext(nextFrame++, tickIndex);

        CombatFlowProcessor.FlowOutput fo = flow.process(frame, state, intents.drain());
        List<StateDelta> deltas = fo.deltas();

        GameState committed = StateDeltaApplier.applyAll(new GameState(frame, state.actors(), state.buffs()), deltas);
        state = committed;

        world.apply(frame, committed, content, deltas);

        GameSnapshot snap = new GameSnapshot(frame, committed.actors(), fo.activeAbilities());
        snapshots.publish(snap);

        return new FrameOutput(frame, snap);
    }
}
