package com.pgalaxyp.fragmento.rpg.targeting.api;

import com.pgalaxyp.fragmento.rpg.targeting.system.TargetingContext;

public interface TargetingService {
    TargetResult resolve(TargetingContext context);
}