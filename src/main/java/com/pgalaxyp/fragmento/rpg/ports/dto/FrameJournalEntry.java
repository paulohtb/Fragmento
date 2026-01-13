package com.pgalaxyp.fragmento.rpg.ports.dto;

import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import java.util.List;

public record FrameJournalEntry(
        FrameContext frame,
        long frameSeed,
        List<IntentEnvelope> intents,
        List<StateDelta> deltas,
        GameSnapshot snapshot
) {
    public FrameJournalEntry {
        if (frame == null || intents == null || deltas == null || snapshot == null) {
            throw new IllegalArgumentException();
        }
        intents = List.copyOf(intents);
        deltas = List.copyOf(deltas);
    }
}