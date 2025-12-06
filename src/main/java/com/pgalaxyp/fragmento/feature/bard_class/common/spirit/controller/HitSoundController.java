package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;
import net.minecraft.world.entity.LivingEntity;

public abstract class HitSoundController<T extends SpiritBase> extends SpiritController<T> {

    public HitSoundController(T spirit) {
        super(spirit);
    }

    public void playOnHit(LivingEntity target) {
        play(target);
    }

    protected abstract void play(LivingEntity target);
}
