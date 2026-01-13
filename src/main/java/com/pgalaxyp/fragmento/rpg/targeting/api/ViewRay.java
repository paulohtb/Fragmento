package com.pgalaxyp.fragmento.rpg.targeting.api;

public record ViewRay(
        Vec3d origin,
        Vec3d direction
) {
    public ViewRay {
        if (origin == null || direction == null) {
            throw new IllegalArgumentException();
        }
        if (direction.lengthSquared() <= 0.0) {
            throw new IllegalArgumentException();
        }
        direction = direction.normalized();
    }

    public Vec3d pointAt(double distance) {
        if (!Double.isFinite(distance) || distance < 0.0) {
            throw new IllegalArgumentException();
        }
        return origin.add(direction.mul(distance));
    }
}