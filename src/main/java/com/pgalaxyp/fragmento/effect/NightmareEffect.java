package com.pgalaxyp.fragmento.effect;

import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.phys.Vec3;

public class NightmareEffect extends MobEffect {

    public NightmareEffect() { super(MobEffectCategory.HARMFUL, 0x552222);}

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Mob mob)) return true;

        MobEffectInstance instance = mob.getEffect(EffectsRegistry.NIGHTMARE);
        if (instance == null) return true;

        int duration = instance.getDuration();
        var data = mob.getPersistentData();

        if (!data.contains("fragmento.nightmareOriginalDuration")) {
            data.putInt("fragmento.nightmareOriginalDuration", duration);
        }

        // Quando o efeito termina
        if (duration <= 1) {
            mob.setNoAi(false);
            mob.setXRot(0.0f);

            // Aplica 10 de dano (5 corações)
            DamageSources sources = mob.damageSources();
            DamageSource source = sources.magic(); // "pesadelo" -> dano mágico
            mob.hurt(source, 10.0F);

            data.remove("fragmento.nightmareOriginalDuration");
        } else {
            // Durante o "sono" do pesadelo
            mob.setNoAi(true);
            if (mob.getNavigation() != null) mob.getNavigation().stop();
            mob.setDeltaMovement(Vec3.ZERO);
            mob.setAggressive(false);
            mob.setTarget(null);
            mob.setXRot(45.0f);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}