package com.pgalaxyp.fragmento.rpg.ports;

import com.pgalaxyp.fragmento.rpg.ports.dto.FrameJournalEntry;

public interface JournalPort {
    void append(FrameJournalEntry entry);
}