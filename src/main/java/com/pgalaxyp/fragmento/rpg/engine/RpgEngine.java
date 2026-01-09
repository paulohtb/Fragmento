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
import com.pgalaxyp.fragmento.rpg.engine.intent.IntentSource;
import com.pgalaxyp.fragmento.rpg.engine.journal.FrameJournalEntry;
import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.port.JournalPort;
import com.pgalaxyp.fragmento.rpg.port.SnapshotPort;
import com.pgalaxyp.fragmento.rpg.port.WorldQueryPort;
import java.util.ArrayList;
import java.util.List;

public final class RpgEngine {

    private final IntentSource intents;
    private final WorldQueryPort worldQueries;
    private final JournalPort journal;
    private final SnapshotPort snapshots;
    private final RpgContent content;

    private GameState state;
    private long nextFrameId;

    public RpgEngine(
            IntentSource intents,
            WorldQueryPort worldQueries,
            JournalPort journal,
            SnapshotPort snapshots,
            RpgContent content,
            GameState initialState
    ) {
        if (intents == null || worldQueries == null || journal == null || snapshots == null || content == null || initialState == null) {
            throw new IllegalArgumentException();
        }
        this.intents = intents;
        this.worldQueries = worldQueries;
        this.journal = journal;
        this.snapshots = snapshots;
        this.content = content;
        this.state = initialState;
        this.nextFrameId = Math.addExact(initialState.frame().frameId(), 1L);
    }

    public EngineFrameOutput step(int tickIndex) {
        FrameContext frame = new FrameContext(nextFrameId, tickIndex);
        nextFrameId = Math.addExact(nextFrameId, 1L);

        List<IntentEnvelope> drained = intents.drain();
        RuleResult a = RpgRules.passA(frame, state, content, drained);

        List<DomainResolution> resolutions = worldQueries.resolve(frame, state, content, a.queries());
        RuleResult b = RpgRules.passB(frame, state, content, drained, resolutions);

        List<StateDelta> merged = StateDeltaMerger.mergeStableDistinct(a.deltas(), b.deltas());

        GameState committed = StateDeltaApplier.applyAll(new GameState(frame, state.actors()), merged);
        state = committed;

        GameSnapshot snapshot = new GameSnapshot(frame, committed.actors());
        snapshots.publish(snapshot);

        List<DomainEvent> events = new ArrayList<>();
        events.addAll(a.events());
        events.addAll(b.events());

        journal.append(new FrameJournalEntry(frame, drained, resolutions, merged, snapshot));

        return new EngineFrameOutput(frame, snapshot, events);
    }
}