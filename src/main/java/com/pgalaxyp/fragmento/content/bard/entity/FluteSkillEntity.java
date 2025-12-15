package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.gameplay.skill.SkillMode;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class FluteSkillEntity extends BardSkillEntityBase implements GeoEntity {

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    public FluteSkillEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected SkillEntity createBehavior(SkillMode mode) {
        if (mode == null) return new FluteSkillEntityBasic(this);

        return switch (mode) {
            case BASIC -> new FluteSkillEntityBasic(this);
            case CHARGED -> new FluteSkillEntityCharged(this);
            case SPECIAL -> new FluteSkillEntitySpecial(this);
        };
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        com.pgalaxyp.fragmento.core.controller.BehaviorAnimationController<FluteSkillEntity> anim =
                new com.pgalaxyp.fragmento.core.controller.BehaviorAnimationController<>(this, FluteSkillEntity::getAnimKey);

        anim.registerState(
                BardAnimKeys.SPAWN,
                RawAnimation.begin().thenPlay("spawn")
        );
        anim.registerState(
                BardAnimKeys.TRAVEL,
                RawAnimation.begin().thenLoop("travel")
        );
        anim.registerState(
                BardAnimKeys.DESPAWN,
                RawAnimation.begin().thenPlay("despawn")
        );

        anim.register(controllers, "main");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object o) {
        return tickCount;
    }
}
