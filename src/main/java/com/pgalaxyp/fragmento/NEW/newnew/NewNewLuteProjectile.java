package com.pgalaxyp.fragmento.NEW.newnew;

import com.pgalaxyp.fragmento.NEW.EntitiesRegistry;
import com.pgalaxyp.fragmento.NEW.NewAbstractProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NewNewLuteProjectile extends NewAbstractProjectile {

    private LivingEntity trackedTarget;

    public NewNewLuteProjectile(EntityType<? extends NewNewLuteProjectile> type, Level level) {
        super(type, level);
    }

    public NewNewLuteProjectile(Level level, Player owner) {
        super(EntitiesRegistry.NEW_NEW_LUTE_PROJECTILE.get(), level, owner, false);
    }

    public void setTrackedTarget(LivingEntity t) {
        this.trackedTarget = t;
    }

    @Override
    protected int getMaxLifetimeInTicks() {
        return 25;
    }

    @Override
    public void tick() {

        int delay = this.getSpawnDelayTicks();
        if (delay > 0) {
            this.setSpawnDelayTicks(delay - 1);
            super.tick();
            return;
        }

        if (trackedTarget != null && trackedTarget.isAlive()) {

            Vec3 targetPoint = new Vec3(
                    trackedTarget.getX(),
                    trackedTarget.getY() + 0.9,
                    trackedTarget.getZ()
            );

            double yWobble = (this.level().getRandom().nextDouble() * 1.0) - 0.5;
            targetPoint = targetPoint.add(0, yWobble * 0.15, 0);

            Vec3 dir = targetPoint.subtract(this.position()).normalize();
            double speed = this.getDeltaMovement().length();

            this.setDeltaMovement(dir.scale(speed));
        }

        Vec3 mov = this.getDeltaMovement();
        this.setPos(this.getX() + mov.x, this.getY() + mov.y, this.getZ() + mov.z);

        if (this.tickCount > getMaxLifetimeInTicks()) {
            this.discard();
        }

        super.tick();
    }

    @Override
    protected void onHit(net.minecraft.world.phys.HitResult result) {
    }

    @Override
    protected boolean shouldDealDamage(LivingEntity target) {
        return false;
    }
}