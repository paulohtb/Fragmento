package com.pgalaxyp.fragmento.rpg.core.state.snapshot;

import com.pgalaxyp.fragmento.rpg.core.state.action.ActorActionState;
import com.pgalaxyp.fragmento.rpg.core.state.combo.ComboProgressState;

public record CombatSnapshot(
        long version,
        ActorActionState actions,
        ComboProgressState combos
) {}