package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;

public record ComboStepDef(
        ComboStepId stepId,
        TargetingId targetingId,
        EffectSpec effect
) {}