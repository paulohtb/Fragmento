package com.pgalaxyp.fragmento.effect;

import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class SleepEffect extends MobEffect {
    public SleepEffect() {
        super(MobEffectCategory.NEUTRAL, 0xAACCCC);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Mob mob)) return true;

        if (mob.hasEffect(EffectsRegistry.INSOMNIA)) {
            mob.removeEffect(EffectsRegistry.SLEEP);
            return false;
        }

        MobEffectInstance instance = mob.getEffect(EffectsRegistry.SLEEP);
        if (instance == null) return true;

        int duration = instance.getDuration();
        if (!mob.getPersistentData().contains("fragmento.sleepOriginalDuration")) {
            mob.getPersistentData().putInt("fragmento.sleepOriginalDuration", duration);
        }

        if (duration <= 1) {
            mob.setNoAi(false);
            mob.setXRot(0.0f);

            int originalDuration = mob.getPersistentData().getInt("fragmento.sleepOriginalDuration");
            if (originalDuration > 0) {
                mob.addEffect(new MobEffectInstance(EffectsRegistry.INSOMNIA, originalDuration * 2));
            }

            mob.getPersistentData().remove("fragmento.sleepOriginalDuration");
        } else {
            mob.setNoAi(true);
            mob.getNavigation().stop();
            mob.setDeltaMovement(Vec3.ZERO);
            mob.setAggressive(false);
            mob.setTarget(null);
            mob.setXRot(45.0f);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}