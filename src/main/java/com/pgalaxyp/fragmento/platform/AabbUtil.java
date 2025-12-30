package com.pgalaxyp.fragmento.platform;

import net.minecraft.world.phys.AABB;

public final class AabbUtil {

    private AabbUtil() {
    }

    public static AABB expand(AABB box, double amount) {
        if (box == null) return null;
        if (amount <= 0.0) return box;

        double a = amount;

        return new AABB(
                box.minX + MathUtil.negate(a),
                box.minY + MathUtil.negate(a),
                box.minZ + MathUtil.negate(a),
                box.maxX + a,
                box.maxY + a,
                box.maxZ + a
        );
    }
}