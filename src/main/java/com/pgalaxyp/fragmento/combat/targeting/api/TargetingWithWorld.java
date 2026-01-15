package com.pgalaxyp.fragmento.combat.targeting.api;

import com.pgalaxyp.fragmento.combat.targeting.bridge.*;

public interface TargetingWithWorld extends TargetingService {
    WorldRaycastAccess world();
}