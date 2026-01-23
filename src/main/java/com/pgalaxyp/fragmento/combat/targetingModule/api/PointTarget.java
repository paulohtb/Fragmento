package com.pgalaxyp.fragmento.combat.targetingModule.api;

import com.pgalaxyp.fragmento.combat.util.Vec3d;

public record PointTarget(Vec3d point) implements Target {
    public PointTarget { if (point == null) throw new IllegalArgumentException(); }
}