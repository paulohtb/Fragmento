package com.pgalaxyp.fragmento.NEW;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NewFluteProjectile extends NewAbstractProjectile {

    public NewFluteProjectile(EntityType<? extends NewFluteProjectile> type, Level level) {
        super(type, level);
    }

    public NewFluteProjectile(Level level, LivingEntity owner, boolean startCharged) {
        super(EntitiesRegistry.NEW_FLUTE_PROJECTILE.get(), level, owner, startCharged);
    }

    @Override
    protected void applyChargedHitEffects(LivingEntity target) {
        if (target instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0));
        } else if (target instanceof Mob) {
            target.setDeltaMovement(
                    target.getDeltaMovement().x,
                    0.4D,
                    target.getDeltaMovement().z
            );
            target.hasImpulse = true;
        }
    }
}
