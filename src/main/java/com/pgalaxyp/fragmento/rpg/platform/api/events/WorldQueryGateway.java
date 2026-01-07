package com.pgalaxyp.fragmento.rpg.platform.api.events;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.rule.action.PendingTargeting;

public interface WorldQueryGateway {
    TargetingResolution resolveTargeting(PendingTargeting request);
}