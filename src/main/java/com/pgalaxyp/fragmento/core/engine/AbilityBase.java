package com.pgalaxyp.fragmento.core.engine;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class AbilityBase {

    private final double range;

    protected AbilityBase(double range) {
        this.range = range;
    }

    public double getRange() {
        return this.range;
    }

    public final boolean execute(LivingEntity caster) {
        if (caster.level().isClientSide()) {
            return false;
        }

        RaycastBase.Result result = RaycastBase.perform(caster, this.range);
        LivingEntity target = result.hitEntity();
        Vec3 hitPos = result.hitPosition();

        if (target == null) {
            return false;
        }

        AABB box = caster.getBoundingBox().inflate(2.0);
        if (box.intersects(target.getBoundingBox())) {
            return false;
        }

        if (caster.distanceTo(target) < 3.0) {
            return false;
        }

        this.applyToTarget(caster, target, hitPos);
        return true;
    }

    protected abstract void applyToTarget(LivingEntity caster, LivingEntity target, Vec3 hitPos);
}
