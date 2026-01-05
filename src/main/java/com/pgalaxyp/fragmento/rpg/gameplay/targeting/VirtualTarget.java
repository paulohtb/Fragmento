package com.pgalaxyp.fragmento.rpg.gameplay.targeting;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Aabb;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public record VirtualTarget(Vec3 position) implements Target {
    @Override
    public Aabb bounds() {
        var p = position;
        return new Aabb(new Vec3(p.x(), p.y(), p.z()), new Vec3(p.x(), p.y(), p.z()));
    }

    @Override
    public boolean isReal() {
        return false;
    }

    @Override
    public long actorIdOrZero() {
        return 0L;
    }
}