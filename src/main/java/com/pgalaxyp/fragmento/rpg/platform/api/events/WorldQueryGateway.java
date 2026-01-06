package com.pgalaxyp.fragmento.rpg.platform.api.events;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.TargetingResolution;
import com.pgalaxyp.fragmento.rpg.core.rule.command.RequestTargeting;

public interface WorldQueryGateway {
    TargetingResolution collect(RequestTargeting request);
}