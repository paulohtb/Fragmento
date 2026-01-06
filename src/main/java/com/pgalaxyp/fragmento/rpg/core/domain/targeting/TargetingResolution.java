package com.pgalaxyp.fragmento.rpg.core.domain.targeting;

import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public record TargetingResolution(
        long actorId,
        Vec3 casterPos,
        Vec3 casterLook,
        Target target
) {}