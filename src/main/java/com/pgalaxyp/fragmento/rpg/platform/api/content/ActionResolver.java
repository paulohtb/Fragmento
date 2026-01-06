package com.pgalaxyp.fragmento.rpg.platform.api.content;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionId;
import java.util.Optional;

public interface ActionResolver {
    Optional<ActionDef> resolve(long actorId, ActionId actionId);
}