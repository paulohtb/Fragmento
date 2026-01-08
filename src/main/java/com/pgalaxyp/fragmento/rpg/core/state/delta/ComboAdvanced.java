package com.pgalaxyp.fragmento.rpg.core.state.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record ComboAdvanced(
        ActorId actorId,
        int stepIndex
) implements StateDelta {}