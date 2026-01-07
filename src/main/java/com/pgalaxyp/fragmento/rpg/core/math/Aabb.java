package com.pgalaxyp.fragmento.rpg.core.math;

public record Aabb(Vec3 min, Vec3 max) {

    public Vec3 center() {
        return new Vec3(
                (min.x() + max.x()) * 0.5,
                (min.y() + max.y()) * 0.5,
                (min.z() + max.z()) * 0.5
        );
    }

    public Vec3 extent() {
        return new Vec3(
                (max.x() + Math.copySign(min.x(), Double.NEGATIVE_INFINITY)) * 0.5,
                (max.y() + Math.copySign(min.y(), Double.NEGATIVE_INFINITY)) * 0.5,
                (max.z() + Math.copySign(min.z(), Double.NEGATIVE_INFINITY)) * 0.5
        );
    }

    public boolean contains(Vec3 p) {
        return p.x() >= min.x() && p.x() <= max.x()
                && p.y() >= min.y() && p.y() <= max.y()
                && p.z() >= min.z() && p.z() <= max.z();
    }
}