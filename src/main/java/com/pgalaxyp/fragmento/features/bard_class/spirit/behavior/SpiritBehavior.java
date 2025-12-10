package com.pgalaxyp.fragmento.features.bard_class.spirit.behavior;

import com.pgalaxyp.fragmento.core.controller.EntityController;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import net.minecraft.world.entity.LivingEntity;

public abstract class SpiritBehavior extends EntityController<CastedSpiritBase> {

    protected SpiritBehavior(CastedSpiritBase spirit) {
        super(spirit);
    }

    protected CastedSpiritBase spirit() {
        return entity;
    }

    @Override
    public abstract void tick();

    public void onHit(LivingEntity target) {
    }
}
