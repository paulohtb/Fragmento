package com.pgalaxyp.fragmento.combat.domain.action;

public record ActionDefinition(
        ActionId id,
        ActionTiming timing,
        ActionLock lock
) {}