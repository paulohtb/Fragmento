package com.pgalaxyp.fragmento.combat.state.runtime.infusion;

import com.pgalaxyp.fragmento.combat.domain.id.InfusionId;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

import java.util.Objects;

public record ArmedInfusion(
        SkillId armedBySkillId,
        InfusionId infusionId,
        CombatTime expiresAtTick
) {
    public ArmedInfusion {
        armedBySkillId = Objects.requireNonNull(armedBySkillId, "armedBySkillId");
        infusionId = Objects.requireNonNull(infusionId, "infusionId");
        expiresAtTick = Objects.requireNonNull(expiresAtTick, "expiresAtTick");
    }
}