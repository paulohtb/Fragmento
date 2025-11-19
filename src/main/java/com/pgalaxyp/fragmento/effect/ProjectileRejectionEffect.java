package com.pgalaxyp.fragmento.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ProjectileRejectionEffect extends MobEffect {

    public ProjectileRejectionEffect() { super(MobEffectCategory.BENEFICIAL, 0xAACCCC); }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}