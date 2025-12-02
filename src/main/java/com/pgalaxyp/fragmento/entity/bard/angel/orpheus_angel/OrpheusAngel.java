package com.pgalaxyp.fragmento.entity.bard.angel.orpheus_angel;

import com.pgalaxyp.fragmento.entity.bard.angel.AbstractAngel;
import com.pgalaxyp.fragmento.NEW.EntitiesRegistry;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class OrpheusAngel extends AbstractAngel implements GeoEntity {

    public OrpheusAngel(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    public OrpheusAngel(Level level, LivingEntity owner) {
        super(EntitiesRegistry.ORPHEUS_ANGEL.get(), level, owner);
    }

    private final AnimatableInstanceCache ANIMATION_CACHE = GeckoLibUtil.createInstanceCache(this);
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.ANIMATION_CACHE; }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
}