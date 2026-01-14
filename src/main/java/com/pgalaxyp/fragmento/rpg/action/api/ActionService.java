package com.pgalaxyp.fragmento.rpg.action.api;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public interface ActionService {
    ActionOutcome handle(ActionContext context, ActionRequest request);
    boolean hasActive(ActorId actorId);
    void clear(ActorId actorId);
    void clearAll();
}