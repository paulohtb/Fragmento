package com.pgalaxyp.fragmento.rpg.gameplay.targeting;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

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