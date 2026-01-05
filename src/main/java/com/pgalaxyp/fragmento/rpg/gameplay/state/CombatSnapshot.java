package com.pgalaxyp.fragmento.rpg.gameplay.state;

import com.pgalaxyp.fragmento.rpg_old.state.snapshot.ComboSnapshot;
import java.util.List;

public record CombatSnapshot(
        long actorId,
        long version,
        ComboSnapshot combo,
        List<EffectSnapshot> effects
) {
    public CombatSnapshot {
        effects = List.copyOf(effects);
    }
}