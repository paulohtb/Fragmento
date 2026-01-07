package com.pgalaxyp.fragmento.rpg.core.domain.action;

import java.util.Set;

public record ActionDef(
        ActionId id,
        ActionType type,
        ActionPriority priority,
        ActionTimeline timeline,
        CancelPolicy cancelPolicy,
        Set<InterruptMask> interruptMask,
        ActionPayload payload
) {
    public ActionDef {
        if (id == null) throw new IllegalArgumentException("ActionDef.id");
        if (type == null) throw new IllegalArgumentException("ActionDef.type");
        if (priority == null) throw new IllegalArgumentException("ActionDef.priority");
        if (timeline == null) throw new IllegalArgumentException("ActionDef.timeline");
        if (cancelPolicy == null) throw new IllegalArgumentException("ActionDef.cancelPolicy");
        interruptMask = interruptMask == null ? Set.of() : Set.copyOf(interruptMask);
        payload = payload == null ? ActionPayload.none() : payload;
    }
}