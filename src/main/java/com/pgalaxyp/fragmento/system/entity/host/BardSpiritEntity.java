package com.pgalaxyp.fragmento.system.entity.host;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.entity.BardSpiritSpawnService;
import com.pgalaxyp.fragmento.system.animation.BehaviorAnimationController;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.controller.NewwSpiritController;
import com.pgalaxyp.fragmento.system.skill.SkillMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class BardSpiritEntity extends NewwSpiritEntityBase implements GeoEntity {

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    private SkillMode mode = SkillMode.BASIC;
    private SpiritBehavior behavior;

    public BardSpiritEntity(EntityType<?> type, Level level) {
        super(type, level);
        setAnimKey(BardAnimKeys.SPAWN);
    }

    public void configure(SpiritBehavior behavior, SkillMode mode) {
        this.behavior = behavior;
        this.mode = mode != null ? mode : SkillMode.BASIC;
    }

    public SpiritBehavior getBehavior() {
        return behavior;
    }

    public SkillMode getMode() {
        return mode;
    }

    @Override
    protected Vec3 resolveSpawnPosition(
            LivingEntity owner,
            LivingEntity target,
            ServerLevel level,
            SkillMode mode
    ) {
        return BardSpiritSpawnService.resolve(level, owner, mode);
    }

    @Override
    protected void onSummoned(SkillMode mode) {
        this.mode = mode != null ? mode : SkillMode.BASIC;

        if (behavior == null) {
            return;
        }

        resetControllers();

        ensureControllerRegistered(
                new NewwSpiritController(
                        this,
                        () -> behavior,
                        this::getMode
                )
        );

        setAnimKey(BardAnimKeys.SPAWN);
    }

    @Override
    public void registerControllers(
            software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar controllers
    ) {
        BehaviorAnimationController<BardSpiritEntity> anim =
                new BehaviorAnimationController<>(this, BardSpiritEntity::getAnimKey);

        anim.registerState(BardAnimKeys.SPAWN, RawAnimation.begin().thenPlay("spawn"));
        anim.registerState(BardAnimKeys.TRAVEL, RawAnimation.begin().thenLoop("travel"));
        anim.registerState(BardAnimKeys.DESPAWN, RawAnimation.begin().thenPlay("despawn"));

        anim.register(controllers, "main");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}