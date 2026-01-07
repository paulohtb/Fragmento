package com.pgalaxyp.fragmento.rpg.core.rule.event;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ActionStarted(
        long actorId,
        ActionId actionId,
        long startedAt,
        long endsAt
) implements DeclaredEvent {}