package com.pgalaxyp.fragmento.rpg.core.domain.targeting;

import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public record TargetingResolution(
        long actorId,
        Vec3 casterPosition,
        Vec3 casterDirection,
        Target resolvedTarget
) {}