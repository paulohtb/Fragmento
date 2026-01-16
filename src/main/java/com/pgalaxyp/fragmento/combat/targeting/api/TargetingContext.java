package com.pgalaxyp.fragmento.combat.targeting.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.targeting.bridge.*;

public record TargetingContext(ActorId casterId, TargetingSpec spec, WorldRaycastAccess world) {

    public TargetingContext {
        if (casterId == null || spec == null || world == null) {
            throw new IllegalArgumentException();
        }
    }
}