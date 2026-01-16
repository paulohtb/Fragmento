package com.pgalaxyp.fragmento.combat.targeting.api;

public record PointTarget(Vec3d position) implements Target {

    public PointTarget {
        if (position == null) {
            throw new IllegalArgumentException();
        }
    }
}