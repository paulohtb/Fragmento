package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class ImpulseController<T extends Entity> extends EntityController<T> {

    private Vec3 pendingImpulse;

    public ImpulseController(T entity) {
        super(entity);
    }

    public void applyImpulse(Vec3 impulse) {
        if (impulse == null) return;
        if (impulse.lengthSqr() < 1.0E-8) return;

        if (pendingImpulse == null) {
            pendingImpulse = impulse;
            return;
        }

        pendingImpulse = pendingImpulse.add(impulse);
    }

    public void clear() {
        pendingImpulse = null;
    }

    @Override
    protected void onTick() {
        if (pendingImpulse == null) return;

        entity.setDeltaMovement(entity.getDeltaMovement().add(pendingImpulse));
        entity.hurtMarked = true;
        pendingImpulse = null;
    }
}
