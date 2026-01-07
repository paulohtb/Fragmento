package com.pgalaxyp.fragmento.rpg.core.rule.event;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;

public record EffectDeclared(
        long actorId,
        EffectId effectId
) implements DeclaredEvent {}