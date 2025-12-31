package com.pgalaxyp.fragmento.combat.domain.infusion;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.id.InfusionId;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;

public record InfusionSpec(
        InfusionId id,
        ActionDefinition infusedAction,
        Duration expiresAfter
) {}