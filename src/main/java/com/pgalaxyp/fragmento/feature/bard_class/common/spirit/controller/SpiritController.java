package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller;


import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;

public abstract class SpiritController<T extends SpiritBase> {

    protected final T spirit;

    protected SpiritController(T spirit) {
        this.spirit = spirit;
    }

    public void tick() {
    }
}
