package com.pgalaxyp.fragmento.combat.random;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import java.util.Objects;

public record GameSnapshot(
        FrameContext frame,
        ActorView actors,
        AbilityViewSnapshot abilities
) {
    public GameSnapshot {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
        Objects.requireNonNull(abilities);
    }
}