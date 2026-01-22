package com.pgalaxyp.fragmento.combat.targeting.api;

public record Vec3d(double x, double y, double z) {
    public Vec3d {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) throw new IllegalArgumentException();
    }

    public Vec3d add(Vec3d o) { if (o == null) throw new IllegalArgumentException(); return new Vec3d(x + o.x, y + o.y, z + o.z); }
    public Vec3d mul(double k) { if (!Double.isFinite(k)) throw new IllegalArgumentException(); return new Vec3d(x * k, y * k, z * k); }
    public double lengthSquared() { return x * x + y * y + z * z; }
    public double length() { return Math.sqrt(lengthSquared()); }

    public Vec3d normalized() {
        double len = length();
        if (!Double.isFinite(len) || len <= 0.0) throw new IllegalArgumentException();
        return new Vec3d(x / len, y / len, z / len);
    }
}