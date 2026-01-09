package com.pgalaxyp.fragmento.rpg.platform.minecraft;

import com.pgalaxyp.fragmento.rpg.engine.journal.FrameJournalEntry;
import com.pgalaxyp.fragmento.rpg.port.JournalPort;

public final class MinecraftJournalPortStub implements JournalPort {

    @Override
    public void append(FrameJournalEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException();
        }
        throw new UnsupportedOperationException();
    }
}