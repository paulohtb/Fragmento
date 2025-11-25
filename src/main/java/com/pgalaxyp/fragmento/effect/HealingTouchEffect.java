package com.pgalaxyp.fragmento.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;

public class HealingTouchEffect extends MobEffect {

    public HealingTouchEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xAACCCC);
    }
}