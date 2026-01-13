package com.pgalaxyp.fragmento.rpg.targeting.system;

import com.pgalaxyp.fragmento.rpg.targeting.api.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.*;

public record TargetingContext(ActorId casterId, TargetingSpec spec, WorldRaycastAccess world) {

    public TargetingContext {
        if (casterId == null || spec == null || world == null) {
            throw new IllegalArgumentException();
        }
    }
}