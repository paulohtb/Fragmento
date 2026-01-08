package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record Intent(
        long actorId,
        ActionId requestedAction
) {}