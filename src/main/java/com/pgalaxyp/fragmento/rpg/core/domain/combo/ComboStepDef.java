package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionTimeline;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingRequest;

public record ComboStepDef(
        String id,
        ActionTimeline timeline,
        EffectId effect,
        TargetingRequest targeting
) {}