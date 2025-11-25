package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class NewDrumProjectile extends NewAbstractProjectile {

    public NewDrumProjectile(EntityType<? extends NewDrumProjectile> type, Level level) {
        super(type, level);
    }

    public NewDrumProjectile(Level level, LivingEntity owner, boolean startCharged) {
        super(EntitiesRegistry.NEW_DRUM_PROJECTILE.get(), level, owner, startCharged);
    }

    @Override
    protected void applyChargedHitEffects(LivingEntity target) {
        if (target instanceof Mob mob) {
            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 0));

            Entity source = this.getOwner();
            double strength = 1.0D;
            double dx;
            double dz;

            if (source != null) {
                dx = source.getX() - target.getX();
                dz = source.getZ() - target.getZ();
            } else {
                dx = this.getX() - target.getX();
                dz = this.getZ() - target.getZ();
            }

            target.knockback(strength, dx, dz);
        }
    }
}
