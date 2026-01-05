package com.pgalaxyp.fragmento.rpg.client.state;

import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshot;

public final class ClientViewState {

    private CombatSnapshot last;

    public void apply(CombatSnapshot snapshot) {
        if (snapshot == null || snapshot.version() == null) return;

        if (last == null || snapshot.version().isAfter(last.version())) {
            last = snapshot;
        }
    }

    public CombatSnapshot current() {
        return last;
    }
}