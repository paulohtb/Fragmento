package com.pgalaxyp.fragmento.features.bard_class.spirit.type;

import com.pgalaxyp.fragmento.core.controller.BehaviorAnimationController;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase.Mode;
import com.pgalaxyp.fragmento.features.bard_class.spirit.behavior.FluteBasicBehavior;
import com.pgalaxyp.fragmento.features.bard_class.spirit.behavior.FluteChargedBehavior;
import com.pgalaxyp.fragmento.features.bard_class.spirit.behavior.FluteSpecialBehavior;
import com.pgalaxyp.fragmento.features.bard_class.spirit.behavior.SpiritBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FluteSpirit extends CastedSpiritBase implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public final BehaviorAnimationController<FluteSpirit> animation =
            new BehaviorAnimationController<>(this, CastedSpiritBase::getAnimKey);

    public FluteSpirit(EntityType<? extends CastedSpiritBase> type, Level level) {
        super(type, level);
    }

    @Override
    public SpiritBehavior createBehavior(Mode mode) {
        return switch (mode) {
            case BASIC -> new FluteBasicBehavior(this);
            case CHARGED -> new FluteChargedBehavior(this);
            case SPECIAL -> new FluteSpecialBehavior(this);
        };
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        animation.registerState("spawn", RawAnimation.begin().thenPlay("spawn"));
        animation.registerState("travel", RawAnimation.begin().thenLoop("travel"));
        animation.registerState("despawn", RawAnimation.begin().thenPlay("despawn"));

        animation.registerState("spawn_charged", RawAnimation.begin().thenPlay("spawn_charged"));
        animation.registerState("dash", RawAnimation.begin().thenLoop("dash"));
        animation.registerState("ascend", RawAnimation.begin().thenLoop("ascend"));
        animation.registerState("hover", RawAnimation.begin().thenLoop("hover"));
        animation.registerState("despawn_charged", RawAnimation.begin().thenPlay("despawn_charged"));

        animation.register(controllers, "anim");
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
