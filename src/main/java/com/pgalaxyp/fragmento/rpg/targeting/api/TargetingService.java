package com.pgalaxyp.fragmento.rpg.targeting.api;

import com.pgalaxyp.fragmento.rpg.targeting.system.*;

public interface TargetingService {
    TargetResult resolve(TargetingContext context);
}