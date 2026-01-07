package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;

public record EngineTargetingInput(
        long actorId,
        ComboStepId nextStep,
        int nextIndex,
        TargetingId targetingId
) {}