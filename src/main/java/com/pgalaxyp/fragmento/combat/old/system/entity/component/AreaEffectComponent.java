package com.pgalaxyp.fragmento.combat.old.system.entity.component;

import net.minecraft.world.phys.AABB;

public final class AreaEffectComponent {

    private final AABB area;
    private final int intervalTicks;
    private final int maxTargets;

    public AreaEffectComponent(
            AABB area,
            int intervalTicks,
            int maxTargets
    ) {
        this.area = area;
        this.intervalTicks = Math.max(1, intervalTicks);
        this.maxTargets = Math.max(1, maxTargets);
    }

    public AABB area() {
        return area;
    }

    public int intervalTicks() {
        return intervalTicks;
    }

    public int maxTargets() {
        return maxTargets;
    }
}