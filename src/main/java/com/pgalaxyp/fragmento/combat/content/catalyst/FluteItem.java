package com.pgalaxyp.fragmento.combat.content.catalyst;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class FluteItem extends Item {

    public FluteItem(Properties properties) {
        super(properties);
    }

    public static boolean isFlute(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return stack.getItem() instanceof FluteItem;
    }
}