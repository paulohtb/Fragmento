package com.pgalaxyp.fragmento.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class NormalAbilityComboHandler {

    private static final int MAX_COMBO_HITS = 1;
    private int currentComboIndex;

    public NormalAbilityComboHandler(int currentComboIndex) {
        this.currentComboIndex = currentComboIndex;
    }

    public boolean isFinalComboHit() {
        return (currentComboIndex + 1) % MAX_COMBO_HITS == 0;
    }

    public void advanceCombo() {
        currentComboIndex = (currentComboIndex + 1) % MAX_COMBO_HITS;
    }

    public static NormalAbilityComboHandler fromItemStack(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return new NormalAbilityComboHandler(tag.getInt("COMBO"));
    }

    public void writeToItemStack(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putInt("COMBO", currentComboIndex);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}