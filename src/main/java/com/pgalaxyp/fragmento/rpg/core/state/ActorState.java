package com.pgalaxyp.fragmento.rpg.core.state;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record ActorState(
        ActorId actorId,
        ComboState combo
) {}