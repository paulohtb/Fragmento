package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.core.rules.RpgRules;
import com.pgalaxyp.fragmento.rpg.core.rules.RuleResult;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.engine.commit.StateDeltaApplier;
import com.pgalaxyp.fragmento.rpg.engine.commit.StateDeltaMerger;
import com.pgalaxyp.fragmento.rpg.engine.journal.FrameJournalEntry;
import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.port.EventSinkPort;
import com.pgalaxyp.fragmento.rpg.port.IntentSourcePort;
import com.pgalaxyp.fragmento.rpg.port.JournalPort;
import com.pgalaxyp.fragmento.rpg.port.SnapshotPort;
import com.pgalaxyp.fragmento.rpg.port.WorldCommandPort;
import com.pgalaxyp.fragmento.rpg.port.WorldQueryPort;
import java.util.ArrayList;
import java.util.List;

public final class RpgEngine {

    private final IntentSourcePort intents;
    private final WorldQueryPort worldQueries;
    private final WorldCommandPort worldCommands;
    private final EventSinkPort eventSink;
    private final JournalPort journal;
    private final SnapshotPort snapshots;
    private final RpgContent content;

    private GameState state;
    private long nextFrameId;

    public RpgEngine(
            IntentSourcePort intents,
            WorldQueryPort worldQueries,
            WorldCommandPort worldCommands,
            EventSinkPort eventSink,
            JournalPort journal,
            SnapshotPort snapshots,
            RpgContent content,
            GameState initialState
    ) {
        if (intents == null || worldQueries == null || worldCommands == null || eventSink == null || journal == null || snapshots == null || content == null || initialState == null) {
            throw new IllegalArgumentException();
        }
        this.intents = intents;
        this.worldQueries = worldQueries;
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
        RuleResult a = RpgRules.passA(frame, state, content, drained);

        List<DomainResolution> resolutions = worldQueries.resolve(frame, state, content, a.queries());
        RuleResult b = RpgRules.passB(frame, state, content, a.queries(), resolutions);

        List<StateDelta> merged = StateDeltaMerger.mergeStable(a.deltas(), b.deltas());

        GameState committed = StateDeltaApplier.applyAll(new GameState(frame, state.actors()), merged);
        state = committed;

        worldCommands.apply(frame, committed, content, merged);

        GameSnapshot snapshot = new GameSnapshot(frame, committed.actors());
        snapshots.publish(snapshot);

        List<DomainEvent> events = new ArrayList<>();
        events.addAll(a.events());
        events.addAll(b.events());

        eventSink.publish(frame, events);

        journal.append(new FrameJournalEntry(frame, frameSeed, drained, resolutions, merged, snapshot));

        return new EngineFrameOutput(frame, snapshot, events);
    }

    private static long seedForFrame(long frameId) {
        long z = Math.addExact(frameId, 0x9e3779b97f4a7c15L);
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        return z ^ (z >>> 31);
    }
}