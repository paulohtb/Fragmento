package com.pgalaxyp.fragmento.cosmetics.api;

import java.util.Objects;

public final class CosmeticTransform {
    private static final float NEG_ONE = Float.intBitsToFloat(0xBF800000);
    private static final float MAX_OFFSET = 64.0F;
    private static final float MIN_OFFSET = MAX_OFFSET * NEG_ONE;
    private static final float MAX_ROT = 360.0F;
    private static final float MIN_ROT = MAX_ROT * NEG_ONE;
    private static final float MAX_SCALE = 16.0F;
    private static final float MIN_SCALE = 0.001F;

    public static final CosmeticTransform IDENTITY = new CosmeticTransform(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F);

    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float rotXDeg;
    private final float rotYDeg;
    private final float rotZDeg;
    private final float scale;

    public CosmeticTransform(float offsetX, float offsetY, float offsetZ, float rotXDeg, float rotYDeg, float rotZDeg, float scale) {
        float ox = finiteOrZero(offsetX);
        float oy = finiteOrZero(offsetY);
        float oz = finiteOrZero(offsetZ);
        float rx = finiteOrZero(rotXDeg);
        float ry = finiteOrZero(rotYDeg);
        float rz = finiteOrZero(rotZDeg);
        float sc = finiteOrZero(scale);

        this.offsetX = clamp(ox, MIN_OFFSET, MAX_OFFSET);
        this.offsetY = clamp(oy, MIN_OFFSET, MAX_OFFSET);
        this.offsetZ = clamp(oz, MIN_OFFSET, MAX_OFFSET);
        this.rotXDeg = clamp(rx, MIN_ROT, MAX_ROT);
        this.rotYDeg = clamp(ry, MIN_ROT, MAX_ROT);
        this.rotZDeg = clamp(rz, MIN_ROT, MAX_ROT);
        this.scale = clamp(sc <= 0.0F ? 1.0F : sc, MIN_SCALE, MAX_SCALE);
    }

    public float offsetX() {
        return offsetX;
    }

    public float offsetY() {
        return offsetY;
    }

    public float offsetZ() {
        return offsetZ;
    }

    public float rotXDeg() {
        return rotXDeg;
    }

    public float rotYDeg() {
        return rotYDeg;
    }

    public float rotZDeg() {
        return rotZDeg;
    }

    public float scale() {
        return scale;
    }

    public CosmeticTransform withOffset(float x, float y, float z) {
        return new CosmeticTransform(x, y, z, rotXDeg, rotYDeg, rotZDeg, scale);
    }

    public CosmeticTransform withRotation(float xDeg, float yDeg, float zDeg) {
        return new CosmeticTransform(offsetX, offsetY, offsetZ, xDeg, yDeg, zDeg, scale);
    }

    public CosmeticTransform withScale(float s) {
        return new CosmeticTransform(offsetX, offsetY, offsetZ, rotXDeg, rotYDeg, rotZDeg, s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CosmeticTransform other)) return false;
        return Float.compare(offsetX, other.offsetX) == 0
                && Float.compare(offsetY, other.offsetY) == 0
                && Float.compare(offsetZ, other.offsetZ) == 0
                && Float.compare(rotXDeg, other.rotXDeg) == 0
                && Float.compare(rotYDeg, other.rotYDeg) == 0
                && Float.compare(rotZDeg, other.rotZDeg) == 0
                && Float.compare(scale, other.scale) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Float.valueOf(offsetX), Float.valueOf(offsetY), Float.valueOf(offsetZ), Float.valueOf(rotXDeg), Float.valueOf(rotYDeg), Float.valueOf(rotZDeg), Float.valueOf(scale));
    }

    @Override
    public String toString() {
        return "CosmeticTransform{off=" + offsetX + "," + offsetY + "," + offsetZ + ", rot=" + rotXDeg + "," + rotYDeg + "," + rotZDeg + ", s=" + scale + "}";
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    private static float finiteOrZero(float v) {
        if (Float.isNaN(v)) return 0.0F;
        if (Float.isInfinite(v)) return 0.0F;
        return v;
    }
}