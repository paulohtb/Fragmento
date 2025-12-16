package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.phys.Vec3;

public final class ImpulseController<T extends net.minecraft.world.entity.Entity> extends EntityController<T> {

    private Vec3 pending;

    public ImpulseController(T entity) {
        super(entity);
    }

    public void addImpulse(Vec3 impulse) {
        if (impulse == null) return;
        pending = pending == null ? impulse : pending.add(impulse);
    }

    public void clear() {
        pending = null;
    }

    @Override
    protected void onTick() {
        if (entity.level().isClientSide()) return;
        if (pending == null) return;

        Vec3 cur = entity.getDeltaMovement();
        Vec3 next = cur.add(pending);
        entity.setDeltaMovement(next);

        pending = null;
    }
}
