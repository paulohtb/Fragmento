package com.pgalaxyp.fragmento.event;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = Fragmento.MODID)
public class EffectEvents {

    @SubscribeEvent
    public static void onProjectileAttack(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (!target.hasEffect(EffectsRegistry.PROJECTILE_REJECTION)) return;

        Entity attacker = event.getSource().getDirectEntity();
        if (!(attacker instanceof Projectile)) return;

        event.setCanceled(true);
        if (!target.level().isClientSide()) {
            attacker.setDeltaMovement(0, -0.4, 0);
        }
    }

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

    @SubscribeEvent
    public static void onEntityDamagesAnother(LivingDamageEvent.Pre event) {

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        if (!attacker.hasEffect(EffectsRegistry.HEALING_TOUCH)) {
            return;
        }

        attacker.heal(1.0F);
    }
}