package com.pgalaxyp.fragmento.rpg.platform.api.world;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.Target;
import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public record TargetingResult(
        long actorId,
        Vec3 casterPos,
        Vec3 casterLook,
        Target target,
        boolean virtual
) {}