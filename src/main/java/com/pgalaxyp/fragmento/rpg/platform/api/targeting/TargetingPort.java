package com.pgalaxyp.fragmento.rpg.platform.api.targeting;

import java.util.Optional;

public interface TargetingPort {
    Optional<TargetResult> resolve(TargetQuery query);
}