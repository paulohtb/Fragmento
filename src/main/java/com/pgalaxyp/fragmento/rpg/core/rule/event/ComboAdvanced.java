package com.pgalaxyp.fragmento.rpg.core.rule.event;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;

public record ComboAdvanced(
        long actorId,
        ActionId actionId,
        int nextIndex
) implements DeclaredEvent {}