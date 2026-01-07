package com.pgalaxyp.fragmento.rpg.core.state;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ActionState(
        long actorId,
        ActionId actionId
) {}