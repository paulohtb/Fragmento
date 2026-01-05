package com.pgalaxyp.fragmento.rpg.gameplay.zone;

import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.SpawnRule;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;
import java.util.Objects;
import java.util.Random;

public final class SpawnResolverService {
    private final WorldView world;
    private final Random rng;

    public SpawnResolverService(WorldView world, Random rng) {
        this.world = Objects.requireNonNull(world);
        this.rng = Objects.requireNonNull(rng);
    }

    public SpawnResult resolve(SpawnQuery q) {
        var target = q.target();
        var aabb = target.bounds();
        var center = aabb.center();
        var ext = aabb.extent();

        var side = chooseSide(q.rule(), q.lastSide());
        var pos = switch (side) {
            case LEFT -> new Vec3(center.x() - ext.x() - q.distance(), center.y(), center.z());
            case RIGHT -> new Vec3(center.x() + ext.x() + q.distance(), center.y(), center.z());
            case TOP -> new Vec3(center.x(), center.y() + ext.y() + q.distance(), center.z());
        };

        if (!isValid(pos)) {
            return new SpawnResult(false, pos, side);
        }
        return new SpawnResult(true, pos, side);
    }

    private SpawnSide chooseSide(SpawnRule rule, SpawnSide last) {
        return switch (rule) {
            case TOP -> SpawnSide.TOP;
            case OPPOSITE -> {
                if (last == SpawnSide.LEFT) yield SpawnSide.RIGHT;
                if (last == SpawnSide.RIGHT) yield SpawnSide.LEFT;
                yield rng.nextBoolean() ? SpawnSide.LEFT : SpawnSide.RIGHT;
            }
            case AUTO -> rng.nextBoolean() ? SpawnSide.LEFT : SpawnSide.RIGHT;
        };
    }

    private boolean isValid(Vec3 p) {
        return !world.isSolidAt(p);
    }
}