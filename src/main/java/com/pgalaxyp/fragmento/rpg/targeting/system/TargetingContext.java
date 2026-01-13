package com.pgalaxyp.fragmento.rpg.targeting.system;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingSpec;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.WorldRaycastAccess;

public record TargetingContext(
        ActorId casterId,
        TargetingSpec spec,
        WorldRaycastAccess world
) {
    public TargetingContext {
        if (casterId == null || spec == null || world == null) {
            throw new IllegalArgumentException();
        }
    }
}