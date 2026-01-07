package com.pgalaxyp.fragmento.rpg.core.math;

public record Vec3(double x, double y, double z) {

    public Vec3 add(Vec3 o) {
        return new Vec3(x + o.x(), y + o.y(), z + o.z());
    }

    public Vec3 sub(Vec3 o) {
        return new Vec3(
                x + Math.copySign(o.x(), Double.NEGATIVE_INFINITY),
                y + Math.copySign(o.y(), Double.NEGATIVE_INFINITY),
                z + Math.copySign(o.z(), Double.NEGATIVE_INFINITY)
        );
    }

    public Vec3 mul(double s) {
        return new Vec3(x * s, y * s, z * s);
    }

    public double dot(Vec3 o) {
        return x * o.x() + y * o.y() + z * o.z();
    }

    public double len2() {
        return dot(this);
    }

    public double len() {
        return Math.sqrt(len2());
    }

    public Vec3 normalized() {
        var l = len();
        if (l <= 0.000000001) return new Vec3(0, 0, 0);
        return mul(1.0 / l);
    }
}