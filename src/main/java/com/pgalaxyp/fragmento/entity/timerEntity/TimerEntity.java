package com.pgalaxyp.fragmento.entity.timerEntity;

import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import com.pgalaxyp.fragmento.util.TimerHandler;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TimerEntity extends Projectile implements GeoEntity {

    private LivingEntity target;

    public TimerEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public TimerEntity(ServerLevel level, LivingEntity target) {
        this(EntitiesRegistry.TIMER_INDICATOR.get(), level);
        this.target = target;
        this.setPos(target.getX(), target.getY() + target.getBbHeight() + 0.5, target.getZ());
    }

    public void setTarget(LivingEntity target) { this.target = target; }

    public static void spawnFor(ServerLevel level, LivingEntity target) {
        if (TimerHandler.hasTimer(target)) {
            TimerEntity indicator = new TimerEntity(EntitiesRegistry.TIMER_INDICATOR.get(), level);
            indicator.setTarget(target);
            indicator.setPos(target.getX(), target.getY() + target.getBbHeight() + 0.5, target.getZ());
            level.addFreshEntity(indicator);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        if (target == null || !target.isAlive() || !TimerHandler.hasTimer(target)) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    private final AnimatableInstanceCache ANIMATION_CACHE = GeckoLibUtil.createInstanceCache(this);
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.ANIMATION_CACHE; }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
}