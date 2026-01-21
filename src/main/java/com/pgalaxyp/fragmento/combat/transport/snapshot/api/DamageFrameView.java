package com.pgalaxyp.fragmento.combat.transport.snapshot.api;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import java.util.*;

public record DamageFrameView(List<ActorId> damagedActors) {
    public DamageFrameView {
        damagedActors = List.copyOf(Objects.requireNonNull(damagedActors));
    }

    public static DamageFrameView empty() {
        return new DamageFrameView(List.of());
    }
}