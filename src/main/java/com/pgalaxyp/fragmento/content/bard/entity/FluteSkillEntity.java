package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.gameplay.skill.SkillMode;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import com.pgalaxyp.fragmento.core.controller.BehaviorAnimationController;

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
    public void tick() {
        super.tick();
    }

    public Vec3 getSmoothedRenderPos(float partialTick) {
        return position();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        BehaviorAnimationController<FluteSkillEntity> anim =
                new BehaviorAnimationController<>(this, FluteSkillEntity::getAnimKey);

        anim.registerState(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.SPAWN, RawAnimation.begin().thenPlay("spawn"));
        anim.registerState(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.TRAVEL, RawAnimation.begin().thenLoop("travel"));
        anim.registerState(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.DESPAWN, RawAnimation.begin().thenPlay("despawn"));

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
