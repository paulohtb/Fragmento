package com.pgalaxyp.fragmento.rpg.core.state.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record ComboEnded(
        ActorId actorId
) implements StateDelta {}