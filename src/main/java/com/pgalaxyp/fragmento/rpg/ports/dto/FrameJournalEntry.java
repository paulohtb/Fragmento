package com.pgalaxyp.fragmento.rpg.ports.dto;

import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.events.resolution.DomainResolution;
import java.util.List;

public record FrameJournalEntry(
        FrameContext frame,
        long frameSeed,
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