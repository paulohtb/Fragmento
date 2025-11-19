package com.pgalaxyp.fragmento.effect;

import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class InsomniaEffect extends MobEffect {

    public InsomniaEffect() { super(MobEffectCategory.NEUTRAL, 0xAACCCC); }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        MobEffectInstance insomniaInstance = entity.getEffect(EffectsRegistry.INSOMNIA);
        if (insomniaInstance == null) return false;
        int duration = insomniaInstance.getDuration();

        if (entity.hasEffect(EffectsRegistry.SLEEP)) {
            entity.removeEffect(EffectsRegistry.SLEEP);
        }

        return true;
    }
}