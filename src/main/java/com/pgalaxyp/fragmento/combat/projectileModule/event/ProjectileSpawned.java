package com.pgalaxyp.fragmento.combat.projectileModule.event;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileId;
import com.pgalaxyp.fragmento.combat.targetingModule.api.Target;
import java.util.Objects;

public record ProjectileSpawned(ActorId sourceActorId, ProjectileId projectileId, Target target) implements FrameEvent {
    public ProjectileSpawned {
        Objects.requireNonNull(sourceActorId);
        Objects.requireNonNull(projectileId);
        Objects.requireNonNull(target);
    }
}
