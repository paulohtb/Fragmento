package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
        if (target instanceof Mob mob) {
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5, 0));
            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 0));
        }
    }
}
