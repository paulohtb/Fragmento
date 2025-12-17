package com.pgalaxyp.fragmento.system.entity.movement;

import net.minecraft.world.phys.Vec3;

public final class LookPlan {

    public enum Kind {
        NONE,
        TO_POS
    }

    public Kind kind = Kind.NONE;
    public Vec3 lookAtPos;

    public void clear() {
        kind = Kind.NONE;
        lookAtPos = null;
    }
}