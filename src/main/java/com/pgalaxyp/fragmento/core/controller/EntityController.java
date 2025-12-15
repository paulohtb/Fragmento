package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;

public abstract class EntityController<T extends Entity> {

    protected final T entity;

    protected EntityController(T entity) {
        this.entity = entity;
    }

    public final void tick() {
        onTick();
    }

    protected abstract void onTick();
}
