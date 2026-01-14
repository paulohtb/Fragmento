package com.pgalaxyp.fragmento.rpg.ports.dto;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

public record GameSnapshot(
        FrameContext frame,
        NavigableMap<ActorId, ActorState> actors
) {
    public GameSnapshot {
        if (frame == null || actors == null) {
            throw new IllegalArgumentException();
        }
        actors = Collections.unmodifiableNavigableMap(new TreeMap<>(actors));
    }
}