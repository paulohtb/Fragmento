package com.pgalaxyp.fragmento.combat.client.state;

import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;

public final class ClientCombatViewState {

    private CombatSnapshot last;

    public void apply(CombatSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }
        if (last == null || snapshot.version().isAfter(last.version())) {
            last = snapshot;
        }
    }

    public CombatSnapshot current() {
        return last;
    }
}