package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingId;

public record Intent(
        long actorId,
        ActionDef actionDef,
        TargetingId targetingId
) {}