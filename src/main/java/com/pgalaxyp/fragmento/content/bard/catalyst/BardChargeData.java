package com.pgalaxyp.fragmento.content.bard.catalyst;

import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class BardChargeData {

    private BardChargeData() {
    }

    public static int getCharge(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (data.isEmpty()) return 0;

        return data.copyTag().getInt(BardInstrumentConstants.NBT_CHARGE);
    }

    public static void setCharge(ItemStack stack, int value) {
        if (stack == null || stack.isEmpty()) return;

        CustomData.update(
                DataComponents.CUSTOM_DATA,
                stack,
                tag -> tag.putInt(BardInstrumentConstants.NBT_CHARGE, clamp(value))
        );
    }

    public static void increment(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        setCharge(stack, getCharge(stack) + 1);
    }

    public static void reset(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        setCharge(stack, 0);
    }

    public static boolean isCharged(ItemStack stack) {
        return getCharge(stack) >= BardInstrumentConstants.MAX_CHARGE;
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(v, BardInstrumentConstants.MAX_CHARGE));
    }
}
