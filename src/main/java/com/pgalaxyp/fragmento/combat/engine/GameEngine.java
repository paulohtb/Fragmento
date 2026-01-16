package com.pgalaxyp.fragmento.combat.engine;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.skill.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.rules.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.cycle.api.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import com.pgalaxyp.fragmento.combat.effect.api.*;
import com.pgalaxyp.fragmento.combat.engine.commit.*;
import com.pgalaxyp.fragmento.combat.event.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.ports.dto.*;
import java.util.*;

public final class GameEngine {
    private final IntentSourcePort intents;
    private final GameContent content;
    private final ComboTracker comboTracker;
    private final ComboSkillResolver comboSkills;
    private final ComboService combo;
    private final ActionCycleService cycles;
    private final ActionService actions;
    private final EffectService effects;
    private final WorldCommandPort worldCommands;
    private final EventSinkPort eventSink;
    private final SnapshotPort snapshots;
    private GameState state;
    private long nextFrameId;

    public GameEngine(IntentSourcePort intents, GameContent content, ComboTracker comboTracker, ComboSkillResolver comboSkills, ComboService combo, ActionCycleService cycles, ActionService actions, EffectService effects, WorldCommandPort worldCommands, EventSinkPort eventSink, SnapshotPort snapshots, GameState initialState) {
        this.intents = Objects.requireNonNull(intents);
        this.content = Objects.requireNonNull(content);
        this.comboTracker = Objects.requireNonNull(comboTracker);
        this.comboSkills = Objects.requireNonNull(comboSkills);
        this.combo = Objects.requireNonNull(combo);
        this.cycles = Objects.requireNonNull(cycles);
        this.actions = Objects.requireNonNull(actions);
        this.effects = Objects.requireNonNull(effects);
        this.worldCommands = Objects.requireNonNull(worldCommands);
        this.eventSink = Objects.requireNonNull(eventSink);
        this.snapshots = Objects.requireNonNull(snapshots);
        this.state = Objects.requireNonNull(initialState);
        this.nextFrameId = Math.addExact(initialState.frame().frameId(), 1L);
    }

    public FrameOutput step(int tickIndex) {
        FrameContext frame = new FrameContext(nextFrameId, tickIndex);
        nextFrameId = Math.addExact(nextFrameId, 1L);

        List<IntentEnvelope> drained = intents.drain();
        RuleResult r = GameRules.pass(frame, state, drained, content, comboTracker, comboSkills, combo, cycles, actions, effects);

        List<StateDelta> merged = StateDeltaMerger.mergeStable(r.deltas(), List.of());
        GameState committed = StateDeltaApplier.applyAll(new GameState(frame, state.actors()), merged);
        state = committed;

        worldCommands.apply(frame, committed, content, merged);

        GameSnapshot snapshot = new GameSnapshot(frame, committed.actors());
        snapshots.publish(snapshot);

        List<DomainEvent> events = new ArrayList<>(r.events());
        eventSink.publish(frame, events);

        return new FrameOutput(frame, snapshot, events);
    }
}
