package com.pgalaxyp.fragmento.combat.action.api;

import com.pgalaxyp.fragmento.combat.core.domain.ids.*;

public interface ActionService {
    ActionOutcome handle(ActionContext context, ActionRequest request);
    boolean hasActive(ActorId actorId);
    void clear(ActorId actorId);
    void clearAll();
}