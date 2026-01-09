package com.pgalaxyp.fragmento.rpg.port;

import com.pgalaxyp.fragmento.rpg.engine.journal.FrameJournalEntry;

public interface JournalPort {
    void append(FrameJournalEntry entry);
}