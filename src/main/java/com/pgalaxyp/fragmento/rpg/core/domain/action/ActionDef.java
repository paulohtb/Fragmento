package com.pgalaxyp.fragmento.rpg.core.domain.action;

import java.util.List;

public record ActionDef(
        ActionId id,
        ActionType type,
        ActionTimeline timeline,
        CancelPolicy cancelPolicy,
        List<InterruptMask> interruptMask
) {}