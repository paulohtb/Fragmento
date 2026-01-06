package com.pgalaxyp.fragmento.rpg.core.domain.targeting;

import com.pgalaxyp.fragmento.rpg.core.math.Aabb;
import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public record EntityTarget(long actorId, Vec3 position, Aabb bounds) implements Target {
    @Override
    public boolean isReal() {
        return true;
    }

    @Override
    public long actorIdOrZero() {
        return actorId;
    }
}