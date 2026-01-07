package com.pgalaxyp.fragmento.rpg.core.domain.targeting;

import com.pgalaxyp.fragmento.rpg.core.math.Aabb;
import com.pgalaxyp.fragmento.rpg.core.math.Vec3;

public record VirtualTarget(
        Vec3 position
) implements Target {

    @Override
    public Aabb bounds() {
        var p = position;
        var eps = 0.01;
        var neg = Double.NEGATIVE_INFINITY;
        return new Aabb(
                new Vec3(
                        p.x() + Math.copySign(eps, neg),
                        p.y() + Math.copySign(eps, neg),
                        p.z() + Math.copySign(eps, neg)
                ),
                new Vec3(
                        p.x() + eps,
                        p.y() + eps,
                        p.z() + eps
                )
        );
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