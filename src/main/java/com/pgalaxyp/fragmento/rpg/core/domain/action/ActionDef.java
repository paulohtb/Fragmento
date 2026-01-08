package com.pgalaxyp.fragmento.rpg.core.domain.action;

public record ActionDef(
        ActionId id,
        ActionType type,
        ActionTimeline timeline,
        CancelPolicy cancelPolicy,
        InterruptMask interruptMask
) {}