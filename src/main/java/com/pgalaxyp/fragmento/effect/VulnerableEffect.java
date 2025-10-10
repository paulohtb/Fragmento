package com.pgalaxyp.fragmento.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class VulnerableEffect extends MobEffect {
    public VulnerableEffect() {
        super(MobEffectCategory.HARMFUL, 0xAACCCC);
    }

    @Override
    public void onMobHurt(LivingEntity entity, int amplifier, DamageSource damageSource, float amount) {
        float amplified = amount + (amount);
        entity.hurt(damageSource, amplified);
    }
}