package com.pgalaxyp.fragmento.rpg.core.state.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ActionSetDelta(
        long actorId,
        ActionId actionId
) {}