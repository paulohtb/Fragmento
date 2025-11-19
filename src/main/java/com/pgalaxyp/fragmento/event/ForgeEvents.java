package com.pgalaxyp.fragmento.event;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = Fragmento.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeEvents {

    @SubscribeEvent
    public static void onPlayerDealDamage(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof Player player &&
                player.hasEffect(EffectsRegistry.HEALING_TOUCH)) {

            float dealtDamage = event.getNewDamage();
            float healAmount = dealtDamage * 0.2F;

            if (!player.level().isClientSide()) {
                player.heal(healAmount);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(EffectsRegistry.CLEANSE)) {
            MobEffect incomingEffect = event.getEffectInstance().getEffect().value();

            if (incomingEffect.getCategory() == MobEffectCategory.HARMFUL) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        Holder<MobEffect> addedEffect = event.getEffectInstance().getEffect();

        if (addedEffect.equals(EffectsRegistry.CLEANSE)) {
            for (var activeEffect : entity.getActiveEffects()) {
                MobEffect active = activeEffect.getEffect().value();
                if (active.getCategory() == MobEffectCategory.HARMFUL) {
                    entity.removeEffect(activeEffect.getEffect());
                }
            }
        }
    }
}