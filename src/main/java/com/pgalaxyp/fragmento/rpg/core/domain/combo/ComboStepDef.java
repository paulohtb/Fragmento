package com.pgalaxyp.fragmento.rpg.core.domain.combo;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionTimeline;
import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;
import java.util.Set;

public record ComboStepDef(
        String stepId,
        ActionTimeline timeline,
        Set<InterruptMask> interruptMask
) {
    public ComboStepDef {
        if (stepId == null || stepId.isBlank()) throw new IllegalArgumentException("ComboStepDef.stepId");
        if (timeline == null) throw new IllegalArgumentException("ComboStepDef.timeline");
        interruptMask = interruptMask == null ? Set.of() : Set.copyOf(interruptMask);
    }
}