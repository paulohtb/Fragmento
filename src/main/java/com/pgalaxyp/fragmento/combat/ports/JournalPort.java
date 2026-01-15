package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.ports.dto.FrameJournalEntry;

public interface JournalPort {
    void append(FrameJournalEntry entry);
}