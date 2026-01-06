package com.pgalaxyp.fragmento.rpg.gameplay.zone;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;
import java.util.Objects;
import java.util.Random;

public final class SpawnResolverService {

    private final Random rng;
    private final WorldView world;

    public SpawnResolverService(Random rng, WorldView world) {
        this.rng = Objects.requireNonNull(rng);
        this.world = Objects.requireNonNull(world);
    }

    public SpawnResult resolve(SpawnQuery q) {
        var t = q.target();
        var c = t.bounds().center();
        var e = t.bounds().extent();

        var side = choose(q.rule(), q.lastSide());
        var pos = switch (side) {
            case LEFT -> new Vec3(c.x() - e.x() - q.distance(), c.y(), c.z());
            case RIGHT -> new Vec3(c.x() + e.x() + q.distance(), c.y(), c.z());
            case TOP -> new Vec3(c.x(), c.y() + e.y() + q.distance(), c.z());
        };

        if (world.isSolidAt(pos)) return new SpawnResult(false, pos, side);
        return new SpawnResult(true, pos, side);
    }

    private SpawnSide choose(SpawnRule rule, SpawnSide last) {
        return switch (rule) {
            case TOP -> SpawnSide.TOP;
            case AUTO -> rng.nextBoolean() ? SpawnSide.LEFT : SpawnSide.RIGHT;
            case OPPOSITE -> last == SpawnSide.LEFT ? SpawnSide.RIGHT
                    : last == SpawnSide.RIGHT ? SpawnSide.LEFT
                    : rng.nextBoolean() ? SpawnSide.LEFT : SpawnSide.RIGHT;
        };
    }
}