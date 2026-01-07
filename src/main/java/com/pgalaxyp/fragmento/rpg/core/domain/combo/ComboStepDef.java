package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionTimeline;
import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;
import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingRequest;
import java.util.Set;

public record ComboStepDef(
        String id,
        ActionTimeline timeline,
        EffectId effect,
        TargetingRequest targeting,
        Set<InterruptMask> interruptMask
) {
    public ComboStepDef {
        interruptMask = interruptMask == null ? Set.of() : Set.copyOf(interruptMask);
    }
}