package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;

public abstract class SpawnSoundController<T extends SpiritBase> extends SpiritController<T> {

    private boolean played;

    public SpawnSoundController(T spirit) {
        super(spirit);
    }

    @Override
    public void tick() {
        if (!played) {
            played = true;
            play();
        }
    }

    protected abstract void play();
}
