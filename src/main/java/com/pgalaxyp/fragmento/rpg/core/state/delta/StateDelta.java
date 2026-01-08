package com.pgalaxyp.fragmento.rpg.core.state.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepId;

public record StateDelta(
        StateDeltaType type,
        long actorId,
        ActionId actionId,
        ComboStepId stepId,
        int index
) {}