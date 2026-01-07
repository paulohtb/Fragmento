package com.pgalaxyp.fragmento.rpg.engine.store;

import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;

import java.util.List;

public final class SnapshotStore {

    private long version;

    public CombatSnapshot nextSnapshot(
            CombatStateStore state,
            List<EffectState> effects
    ) {
        version++;
        return new CombatSnapshot(
                version,
                state.snapshot(),
                List.copyOf(effects)
        );
    }
}