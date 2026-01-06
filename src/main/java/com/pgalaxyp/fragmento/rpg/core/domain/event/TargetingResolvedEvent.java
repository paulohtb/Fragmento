package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.Target;
import java.util.Optional;

public record TargetingResolvedEvent(
        TargetingRequestedEvent request,
        Optional<Target> target
) implements RpgEvent {}