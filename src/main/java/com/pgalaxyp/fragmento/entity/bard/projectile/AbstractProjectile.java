package com.pgalaxyp.fragmento.entity.bard.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractProjectile extends Projectile {

    public AbstractProjectile(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    public void tick() {

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            onHit(hitResult);
            if (shouldDiscardAfterHit()) { return; }
        }

        setPos(position().add(getDeltaMovement()));
    }

    protected boolean shouldDiscardAfterHit() {
        return true;
    }

    public void shoot(Vec3 direction) {
        setDeltaMovement(direction.scale(1.0f));
    }
}