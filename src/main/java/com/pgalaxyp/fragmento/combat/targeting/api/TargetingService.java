package com.pgalaxyp.fragmento.combat.targeting.api;

import com.pgalaxyp.fragmento.combat.targeting.system.*;

public interface TargetingService {
    TargetResult resolve(TargetingContext context);
}