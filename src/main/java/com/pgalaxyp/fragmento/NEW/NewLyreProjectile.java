package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class NewLyreProjectile extends NewAbstractProjectile {

    public NewLyreProjectile(EntityType<? extends NewLyreProjectile> type, Level level) {
        super(type, level);
    }

    public NewLyreProjectile(Level level, LivingEntity owner, boolean startCharged) {
        super(EntitiesRegistry.NEW_LYRE_PROJECTILE.get(), level, owner, startCharged);
    }

    @Override
    protected boolean shouldDealDamage(LivingEntity target) {
        return !this.isInChargedState();
    }

    @Override
    protected void applyChargedHitEffects(LivingEntity target) {
        if (target instanceof Mob mob) {
            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 0));
            mob.heal(1F);
        }
    }

    @Override
    protected float getChargedDamageAmount() {
        return 0F;
    }
}