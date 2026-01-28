package com.pgalaxyp.fragmento.combat.engineModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import java.util.Objects;

public record GameSnapshot(FrameContext frame, ActorView actors, AbilityViewSnapshot abilities) {
    public GameSnapshot {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
        Objects.requireNonNull(abilities);
    }
}