package com.pgalaxyp.fragmento.rpg.core.state.effect;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.targeting.Target;

public record EffectState(
        long actorId,
        EffectId effect,
        Target target,
        long createdAt
) {}