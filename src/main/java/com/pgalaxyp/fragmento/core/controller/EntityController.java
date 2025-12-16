package com.pgalaxyp.fragmento.core.controller;

public abstract class EntityController<T extends net.minecraft.world.entity.Entity> {

    protected final T entity;

    protected EntityController(T entity) {
        this.entity = entity;
    }

    public final void tick() {
        onTick();
    }

    protected abstract void onTick();
}
