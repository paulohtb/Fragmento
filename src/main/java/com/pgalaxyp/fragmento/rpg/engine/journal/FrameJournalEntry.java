package com.pgalaxyp.fragmento.rpg.engine.journal;

import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import java.util.List;

public record FrameJournalEntry(
        FrameContext frame,
        List<IntentEnvelope> intents,
        List<DomainResolution> resolutions,
        List<StateDelta> deltas,
        GameSnapshot snapshot
) {
    public FrameJournalEntry {
        if (frame == null || intents == null || resolutions == null || deltas == null || snapshot == null) {
            throw new IllegalArgumentException();
        }
        intents = List.copyOf(intents);
        resolutions = List.copyOf(resolutions);
        deltas = List.copyOf(deltas);
    }
}