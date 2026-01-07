package com.pgalaxyp.fragmento.rpg.core.rule.intent;

import com.pgalaxyp.fragmento.rpg.core.domain.action.InterruptMask;

public record InterruptIntent(
        long actorId,
        InterruptMask cause
) {}