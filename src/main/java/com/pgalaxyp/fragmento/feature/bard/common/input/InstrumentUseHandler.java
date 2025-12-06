package com.pgalaxyp.fragmento.feature.bard.common.input;

import com.pgalaxyp.fragmento.feature.bard.common.data.AbilityChargeData;
import com.pgalaxyp.fragmento.feature.bard.common.weapon.InstrumentBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class InstrumentUseHandler {

    private InstrumentUseHandler() {
    }

    public static void handleBasic(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(stack.getItem() instanceof InstrumentBase weapon)) {
            return;
        }

        int charge = AbilityChargeData.getCharge(stack);

        if (charge >= AbilityChargeData.getMaxCharge()) {
            boolean used = weapon.useNormalCharged(player, stack);
            if (used) {
                AbilityChargeData.reset(stack);
            }
        } else {
            weapon.useNormalBasic(player, stack);
        }
    }

    public static void handleCharged(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof InstrumentBase weapon) {
            weapon.useNormalCharged(player, stack);
        }
    }
}