package com.pgalaxyp.fragmento.NEW;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NewFluteWeapon extends NewAbstractWeapon {

    public NewFluteWeapon(Properties properties) {
        super(properties, NewFluteProjectile::new);
    }

    @Override
    public void useNormalAbility(ServerPlayer player, ItemStack stack) {
        super.useNormalAbility(player, stack);
        player.sendSystemMessage(Component.literal("Esquerdo clicado, normal ability"));
    }

    @Override
    protected void applySpecialAbilityEffectToTarget(ServerPlayer server, ItemStack stack, LivingEntity target) {
        server.sendSystemMessage(Component.literal("Direito clicado, special ability"));
        if (target instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5));

            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5));
        }
    }
}
