package com.pgalaxyp.fragmento.rpg.core.domain.intent;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record ComboAdvanceIntent(
        ActorId actorId,
        ActionId actionId
) implements DomainIntent {}