package com.pgalaxyp.fragmento.features.bard_class.instrument;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class InstrumentChargeData {

    private InstrumentChargeData() {}

    public static int getCharge(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (data.isEmpty()) return 0;
        return data.copyTag().getInt(InstrumentConstants.NBT_CHARGE);
    }

    public static void setCharge(ItemStack stack, int value) {
        CustomData.update(
                DataComponents.CUSTOM_DATA,
                stack,
                tag -> tag.putInt(InstrumentConstants.NBT_CHARGE, clamp(value))
        );
    }

    public static void increment(ItemStack stack) {
        setCharge(stack, getCharge(stack) + 1);
    }

    public static void reset(ItemStack stack) {
        setCharge(stack, 0);
    }

    public static boolean isCharged(ItemStack stack) {
        return getCharge(stack) >= InstrumentConstants.MAX_CHARGE;
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(v, InstrumentConstants.MAX_CHARGE));
    }
}
