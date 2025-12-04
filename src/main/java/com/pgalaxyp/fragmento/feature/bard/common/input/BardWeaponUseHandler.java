package com.pgalaxyp.fragmento.feature.bard.common.input;

import com.pgalaxyp.fragmento.feature.bard.common.data.BardWeaponChargeData;
import com.pgalaxyp.fragmento.feature.bard.common.weapon.WeaponBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class BardWeaponUseHandler {

    private BardWeaponUseHandler() {
    }

    public static void handleBasic(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(stack.getItem() instanceof WeaponBase weapon)) {
            return;
        }

        int charge = BardWeaponChargeData.getCharge(stack);

        if (charge >= BardWeaponChargeData.getMaxCharge()) {
            boolean used = weapon.useNormalCharged(player, stack);
            if (used) {
                BardWeaponChargeData.reset(stack);
            }
        } else {
            weapon.useNormalBasic(player, stack);
        }
    }

    public static void handleCharged(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof WeaponBase weapon) {
            weapon.useNormalCharged(player, stack);
        }
    }
}