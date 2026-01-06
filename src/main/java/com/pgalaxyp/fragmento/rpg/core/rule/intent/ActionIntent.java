package com.pgalaxyp.fragmento.rpg.core.rule.intent;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ActionIntent(
        long actorId,
        ActionId actionId
) {}