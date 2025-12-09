package com.pgalaxyp.fragmento.core.util;

import net.minecraft.world.phys.Vec3;

public final class MathUtil {

    private MathUtil() {}

    public static Vec3 lerp(Vec3 a, Vec3 b, double t) {
        if (t <= 0) return a;
        if (t >= 1) return b;
        return new Vec3(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t,
                a.z + (b.z - a.z) * t
        );
    }

    public static double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }
}
