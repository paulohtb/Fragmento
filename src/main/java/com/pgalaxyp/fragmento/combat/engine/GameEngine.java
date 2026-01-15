package com.pgalaxyp.fragmento.combat.engine;

import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.cycle.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.ports.dto.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.core.rules.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.engine.commit.*;
import com.pgalaxyp.fragmento.combat.core.events.delta.*;
import com.pgalaxyp.fragmento.combat.core.events.event.*;
import com.pgalaxyp.fragmento.combat.core.events.intent.*;
import java.util.*;

public final class GameEngine {

    private final IntentSourcePort intents;
    private final ComboService combo;
    private final ActionCycleService cycles;
    private final ActionService actions;
    private final EffectService effects;
    private final WorldCommandPort worldCommands;
    private final EventSinkPort eventSink;
    private final JournalPort journal;
    private final SnapshotPort snapshots;
    private final GameContent content;
    private GameState state;
    private long nextFrameId;

    public GameEngine(IntentSourcePort intents, ComboService combo, ActionCycleService cycles, ActionService actions, EffectService effects, WorldCommandPort worldCommands, EventSinkPort eventSink, JournalPort journal, SnapshotPort snapshots, GameContent content, GameState initialState) {
        this.intents = Objects.requireNonNull(intents);
        this.combo = Objects.requireNonNull(combo);
        this.cycles = Objects.requireNonNull(cycles);
        this.actions = Objects.requireNonNull(actions);
        this.effects = Objects.requireNonNull(effects);
        this.worldCommands = Objects.requireNonNull(worldCommands);
        this.eventSink = Objects.requireNonNull(eventSink);
        this.journal = Objects.requireNonNull(journal);
        this.snapshots = Objects.requireNonNull(snapshots);
        this.content = Objects.requireNonNull(content);
        this.state = Objects.requireNonNull(initialState);
        this.nextFrameId = Math.addExact(initialState.frame().frameId(), 1L);
    }

    public FrameOutput step(int tickIndex) {
        FrameContext frame = new FrameContext(nextFrameId, tickIndex);
        nextFrameId = Math.addExact(nextFrameId, 1L);

        long frameSeed = seedForFrame(frame.frameId());
        List<IntentEnvelope> drained = intents.drain();

        RuleResult r = GameRules.pass(frame, state, drained, combo, cycles, actions, effects);
        List<StateDelta> merged = StateDeltaMerger.mergeStable(r.deltas(), List.of());

        GameState committed = StateDeltaApplier.applyAll(new GameState(frame, state.actors()), merged);
        state = committed;

        worldCommands.apply(frame, committed, content, merged);

        GameSnapshot snapshot = new GameSnapshot(frame, committed.actors());
        snapshots.publish(snapshot);

        List<DomainEvent> events = new ArrayList<>(r.events());
        eventSink.publish(frame, events);

        journal.append(new FrameJournalEntry(frame, frameSeed, drained, merged, snapshot));

        return new FrameOutput(frame, snapshot, events);
    }

    private static long seedForFrame(long frameId) {
        long z = Math.addExact(frameId, 0x9e3779b97f4a7c15L);
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }
}