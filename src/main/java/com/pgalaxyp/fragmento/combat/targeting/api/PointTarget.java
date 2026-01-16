package com.pgalaxyp.fragmento.combat.targeting.api;

public record PointTarget(Vec3d point) implements Target {
    public PointTarget {
        if (point == null) throw new IllegalArgumentException();
    }
}