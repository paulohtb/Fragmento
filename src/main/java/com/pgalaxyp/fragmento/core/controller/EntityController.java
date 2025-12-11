package com.pgalaxyp.fragmento.core.controller;

import com.pgalaxyp.fragmento.core.debug.ModLogger;
import net.minecraft.world.entity.Entity;

public abstract class EntityController<T extends Entity> {

    protected final T entity;

    protected EntityController(T entity) {
        this.entity = entity;
    }

    public void tick() {


        //LOGGUER AQUI
        ModLogger.controllerTick(entity, this.getClass().getSimpleName());
        //LOGGUER AQUI


        this.onTick();
    }


    //LOGGUER AQUI
    protected abstract void onTick();
    //LOGGUER AQUI


}
