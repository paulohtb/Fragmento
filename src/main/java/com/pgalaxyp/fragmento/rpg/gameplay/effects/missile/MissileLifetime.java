package com.pgalaxyp.fragmento.rpg.gameplay.effects.missile;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.Target;

public final class MissileLifetime {
    public boolean shouldEnd(double elapsedSeconds, double lifetimeSeconds, Vec3 missilePos, Target target, Vec3 aimPoint) {
        if (elapsedSeconds >= lifetimeSeconds) return true;

        if (target.isReal()) {
            if (target.bounds().contains(missilePos)) return true;
            return missilePos.sub(aimPoint).len2() <= 0.2 * 0.2;
        }

        return missilePos.sub(aimPoint).len2() <= 0.2 * 0.2;
    }
}