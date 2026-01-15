package com.pgalaxyp.fragmento.combat.platform.neoforge.persist;

import com.pgalaxyp.fragmento.combat.ports.JournalPort;
import com.pgalaxyp.fragmento.combat.ports.dto.FrameJournalEntry;
import java.util.ArrayList;
import java.util.List;

public final class NeoForgeJournalPortStub implements JournalPort {

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