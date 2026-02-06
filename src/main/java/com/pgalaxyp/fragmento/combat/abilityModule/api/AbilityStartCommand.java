package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameCommand;
import java.util.Objects;

public record AbilityStartCommand(ActorId actorId, AbilityId abilityId) implements FrameCommand {
    public AbilityStartCommand {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(abilityId);
    }
}