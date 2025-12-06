package com.pgalaxyp.fragmento.feature.bard_class.common.input;

import com.pgalaxyp.fragmento.feature.bard_class.common.data.InstrumentChargeData;
import com.pgalaxyp.fragmento.feature.bard_class.common.weapon.InstrumentBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class InstrumentUseHandler {

    private InstrumentUseHandler() {
    }

    public static void handleBasic(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(stack.getItem() instanceof InstrumentBase instrument)) {
            return;
        }

        int charge = InstrumentChargeData.getCharge(stack);

        if (charge >= InstrumentChargeData.getMaxCharge()) {
            boolean usedCharged = instrument.useNormalCharged(player, stack);
            if (usedCharged) {
                InstrumentChargeData.reset(stack);
            }
            return;
        }

        instrument.useNormalBasic(player, stack);
    }

    public static void handleCharged(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(stack.getItem() instanceof InstrumentBase instrument)) {
            return;
        }

        boolean used = instrument.useNormalCharged(player, stack);
        if (used) {
            InstrumentChargeData.reset(stack);
        }
    }
}
