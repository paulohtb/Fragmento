package com.pgalaxyp.fragmento.feature.bard.common.data;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class BardWeaponChargeData {

    private static final String KEY_CHARGE = "fragmento_bard_charge";
    private static final int MAX_CHARGE = 2;

    private BardWeaponChargeData() {
    }

    public static int getCharge(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (data.isEmpty()) {
            return 0;
        }
        CompoundTag tag = data.getUnsafe();
        return tag.getInt(KEY_CHARGE);
    }

    public static void setCharge(ItemStack stack, int value) {
        int clamped = value;
        if (clamped < 0) {
            clamped = 0;
        }
        if (clamped > MAX_CHARGE) {
            clamped = MAX_CHARGE;
        }
        int finalValue = clamped;
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(KEY_CHARGE, finalValue));
    }

    public static int incrementCharge(ItemStack stack) {
        int current = getCharge(stack);
        int next = current + 1;
        if (next > MAX_CHARGE) {
            next = MAX_CHARGE;
        }
        setCharge(stack, next);
        return next;
    }

    public static void reset(ItemStack stack) {
        setCharge(stack, 0);
    }

    public static int getMaxCharge() {
        return MAX_CHARGE;
    }
}
