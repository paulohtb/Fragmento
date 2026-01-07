package com.pgalaxyp.fragmento.rpg.platform;

import com.pgalaxyp.fragmento.rpg.core.domain.combo.ComboStepId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;

public record TargetingResolved(
        long actorId,
        ComboStepId nextStep,
        int nextIndex,
        TargetingId targetingId
) {}