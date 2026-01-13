package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.rules.RpgRules;
import com.pgalaxyp.fragmento.rpg.core.rules.RuleResult;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.engine.commit.StateDeltaApplier;
import com.pgalaxyp.fragmento.rpg.engine.commit.StateDeltaMerger;
import com.pgalaxyp.fragmento.rpg.ports.EventSinkPort;
import com.pgalaxyp.fragmento.rpg.ports.IntentSourcePort;
import com.pgalaxyp.fragmento.rpg.ports.JournalPort;
import com.pgalaxyp.fragmento.rpg.ports.SnapshotPort;
import com.pgalaxyp.fragmento.rpg.ports.WorldCommandPort;
import com.pgalaxyp.fragmento.rpg.ports.dto.FrameJournalEntry;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingService;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.WorldRaycastAccess;
import java.util.ArrayList;
import java.util.List;

public final class RpgEngine {

    private final IntentSourcePort intents;
    private final TargetingService targetingService;
    private final WorldRaycastAccess worldRaycast;
    private final WorldCommandPort worldCommands;
    private final EventSinkPort eventSink;
    private final JournalPort journal;
    private final SnapshotPort snapshots;
    private final RpgContent content;

    private GameState state;
    private long nextFrameId;

    public RpgEngine(
            IntentSourcePort intents,
            TargetingService targetingService,
            WorldRaycastAccess worldRaycast,
            WorldCommandPort worldCommands,
            EventSinkPort eventSink,
            JournalPort journal,
            SnapshotPort snapshots,
            RpgContent content,
            GameState initialState
    ) {
        if (intents == null || targetingService == null || worldRaycast == null || worldCommands == null || eventSink == null || journal == null || snapshots == null || content == null || initialState == null) {
            throw new IllegalArgumentException();
        }
        this.intents = intents;
        this.targetingService = targetingService;
        this.worldRaycast = worldRaycast;
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

        RuleResult r = RpgRules.pass(frame, state, content, drained, targetingService, worldRaycast);

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