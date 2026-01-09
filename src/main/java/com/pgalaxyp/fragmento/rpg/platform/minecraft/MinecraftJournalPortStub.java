package com.pgalaxyp.fragmento.rpg.platform.minecraft;

import com.pgalaxyp.fragmento.rpg.engine.journal.FrameJournalEntry;
import com.pgalaxyp.fragmento.rpg.port.JournalPort;
import java.util.ArrayList;
import java.util.List;

public final class MinecraftJournalPortStub implements JournalPort {

    private final List<FrameJournalEntry> entries = new ArrayList<>();

    @Override
    public void append(FrameJournalEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException();
        }
        entries.add(entry);
    }

    public List<FrameJournalEntry> entries() {
        return List.copyOf(entries);
    }
}