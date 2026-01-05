package com.pgalaxyp.fragmento.rpg.gameplay.math;

public record Vec3(double x, double y, double z) {
    public Vec3 add(Vec3 o) {
        return new Vec3(x + o.x, y + o.y, z + o.z);
    }

    public Vec3 sub(Vec3 o) {
        return new Vec3(x - o.x, y - o.y, z - o.z);
    }

    public Vec3 mul(double s) {
        return new Vec3(x * s, y * s, z * s);
    }

    public double dot(Vec3 o) {
        return x * o.x + y * o.y + z * o.z;
    }

    public double len2() {
        return dot(this);
    }

    public double len() {
        return Math.sqrt(len2());
    }

    public Vec3 normalized() {
        var l = len();
        if (l <= 1.0e-9) return new Vec3(0, 0, 0);
        return mul(1.0 / l);
    }

    public static Vec3 lerp(Vec3 a, Vec3 b, double t) {
        var tt = clamp01(t);
        return new Vec3(
                a.x + (b.x - a.x) * tt,
                a.y + (b.y - a.y) * tt,
                a.z + (b.z - a.z) * tt
        );
    }

    public static double clamp01(double v) {
        if (v <= 0.0) return 0.0;
        if (v >= 1.0) return 1.0;
        return v;
    }
}