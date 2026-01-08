package com.pgalaxyp.fragmento.rpg.core.state.snapshot;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import java.util.Map;

public record GameSnapshot(
        Map<ActorId, ActorState> actors
) {}