package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NewLyreWeapon extends NewAbstractWeapon {

    public NewLyreWeapon(Properties properties) {
        super(properties, NewLyreProjectile::new);
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

            player.heal(1F);
            player.addEffect(new MobEffectInstance(EffectsRegistry.HEALING_TOUCH, 5));
        }
    }
}
