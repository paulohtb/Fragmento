package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.phys.Vec3;

public final class ImpulseController<T extends net.minecraft.world.entity.Entity>
        extends EntityController<T> {

    private Vec3 impulse;

    public ImpulseController(T entity) {
        super(entity);
    }

    public void addImpulse(Vec3 impulse) {
        if (impulse == null) return;
        this.impulse = this.impulse == null ? impulse : this.impulse.add(impulse);
    }

    @Override
    protected void onTick() {
        if (impulse == null) return;

        Vec3 pos = entity.position().add(impulse);
        entity.setPos(pos.x, pos.y, pos.z);
        impulse = null;
    }
}
