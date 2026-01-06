package com.pgalaxyp.fragmento.rpg.platform.api.targeting;

import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public record TargetQuery(
        long sourceActorId,
        Vec3 origin,
        Vec3 direction,
        double minDistance,
        double maxDistance
) {}