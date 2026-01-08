package com.pgalaxyp.fragmento.rpg.core.state.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;

public record ComboStarted(
        ActorId actorId,
        ActionId actionId,
        int stepsTotal
) implements StateDelta {}