package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.ports.*;
import com.pgalaxyp.fragmento.rpg.ports.dto.*;
import com.pgalaxyp.fragmento.rpg.damage.api.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.core.rules.*;
import com.pgalaxyp.fragmento.rpg.core.content.*;
import com.pgalaxyp.fragmento.rpg.engine.commit.*;
import com.pgalaxyp.fragmento.rpg.targeting.api.*;
import com.pgalaxyp.fragmento.rpg.action.runtime.*;
import com.pgalaxyp.fragmento.rpg.damage.snapshot.*;
import com.pgalaxyp.fragmento.rpg.core.domain.time.*;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.*;
import com.pgalaxyp.fragmento.rpg.core.events.delta.*;
import com.pgalaxyp.fragmento.rpg.core.events.event.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import java.util.*;

public final class RpgEngine {

    private final IntentSourcePort intents;
    private final ActionRuntimeStore actions;
    private final TargetingService targetingService;
    private final WorldRaycastAccess worldRaycast;
    private final DamageService damageService;
    private final DamageSnapshotProvider damageSnapshots;
    private final WorldCommandPort worldCommands;
    private final EventSinkPort eventSink;
    private final JournalPort journal;
    private final SnapshotPort snapshots;
    private final RpgContent content;
    private GameState state;
    private long nextFrameId;

    public RpgEngine(IntentSourcePort intents, ActionRuntimeStore actions, TargetingService targetingService, WorldRaycastAccess worldRaycast, DamageService damageService, DamageSnapshotProvider damageSnapshots, WorldCommandPort worldCommands, EventSinkPort eventSink, JournalPort journal, SnapshotPort snapshots, RpgContent content, GameState initialState) {
        if (intents == null || actions == null || targetingService == null || worldRaycast == null || damageService == null || damageSnapshots == null || worldCommands == null || eventSink == null || journal == null || snapshots == null || content == null || initialState == null) {
            throw new IllegalArgumentException();
        }
        this.intents = intents;
        this.actions = actions;
        this.targetingService = targetingService;
        this.worldRaycast = worldRaycast;
        this.damageService = damageService;
        this.damageSnapshots = damageSnapshots;
        this.worldCommands = worldCommands;
        this.eventSink = eventSink;
        this.journal = journal;
        this.snapshots = snapshots;
        this.content = content;
        this.state = initialState;
        this.nextFrameId = Math.addExact(initialState.frame().frameId(), 1L);
    }

    public EngineFrameOutput step(int tickIndex) {
        FrameContext frame = new FrameContext(nextFrameId, tickIndex);
        nextFrameId = Math.addExact(nextFrameId, 1L);

        long frameSeed = seedForFrame(frame.frameId());

        List<IntentEnvelope> drained = intents.drain();

        RuleResult r = RpgRules.pass(frame, state, content, drained, actions, targetingService, worldRaycast, damageService, damageSnapshots);

        List<StateDelta> merged = StateDeltaMerger.mergeStable(r.deltas(), List.of());

        GameState committed = StateDeltaApplier.applyAll(new GameState(frame, state.actors()), merged);
        state = committed;

        worldCommands.apply(frame, committed, content, merged);

        GameSnapshot snapshot = new GameSnapshot(frame, committed.actors());
        snapshots.publish(snapshot);

        List<DomainEvent> events = new ArrayList<>(r.events());
        eventSink.publish(frame, events);

        journal.append(new FrameJournalEntry(frame, frameSeed, drained, merged, snapshot));

        return new EngineFrameOutput(frame, snapshot, events);
    }

    private static long seedForFrame(long frameId) {
        long z = Math.addExact(frameId, 0x9e3779b97f4a7c15L);
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }
}