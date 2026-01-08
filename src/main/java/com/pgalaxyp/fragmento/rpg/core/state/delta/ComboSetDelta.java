package com.pgalaxyp.fragmento.rpg.core.state.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepId;

public record ComboSetDelta(
        long actorId,
        ComboStepId stepId,
        int index
) {}