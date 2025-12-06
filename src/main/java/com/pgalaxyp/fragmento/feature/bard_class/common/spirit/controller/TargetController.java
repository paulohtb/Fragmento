package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;
import net.minecraft.world.entity.LivingEntity;

public final class TargetController<T extends SpiritBase> extends SpiritController<T> {

    private LivingEntity target;

    public TargetController(T spirit) {
        super(spirit);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public boolean hasValidTarget() {
        return this.target != null && this.target.isAlive();
    }
}
