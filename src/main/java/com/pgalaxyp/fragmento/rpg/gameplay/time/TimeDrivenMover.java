package com.pgalaxyp.fragmento.rpg.gameplay.time;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public final class TimeDrivenMover {
    public TemporalProgress progress(double elapsedSeconds, double totalSeconds) {
        if (totalSeconds <= 1.0e-9) return new TemporalProgress(1.0);
        return new TemporalProgress(elapsedSeconds / totalSeconds);
    }

    public Vec3 position(Vec3 origin, Vec3 destination, TemporalProgress p) {
        return Vec3.lerp(origin, destination, p.t01());
    }

    public Vec3 forward(Vec3 from, Vec3 to) {
        return to.sub(from).normalized();
    }
}