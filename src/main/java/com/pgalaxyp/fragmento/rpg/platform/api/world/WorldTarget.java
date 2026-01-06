package com.pgalaxyp.fragmento.rpg.platform.api.world;

import com.pgalaxyp.fragmento.rpg.core.domain.targeting.Target;
import com.pgalaxyp.fragmento.rpg.core.math.Aabb;
import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public record WorldTarget(
        Vec3 position,
        Aabb bounds,
        long actorIdOrZero
) implements Target {

    @Override
    public boolean isReal() {
        return true;
    }
}