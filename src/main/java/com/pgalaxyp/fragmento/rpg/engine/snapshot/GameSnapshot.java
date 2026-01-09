package com.pgalaxyp.fragmento.rpg.engine.snapshot;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import java.util.Map;

public record GameSnapshot(
        FrameContext frame,
        Map<ActorId, ActorState> actors
) {
    public GameSnapshot {
        if (frame == null || actors == null) {
            throw new IllegalArgumentException();
        }
        actors = Map.copyOf(actors);
    }
}