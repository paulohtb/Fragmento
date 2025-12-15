package com.pgalaxyp.fragmento.gameplay.skill;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class SkillActionRegistry {

    private static final List<Entry> ENTRIES = new ArrayList<>(4);

    private SkillActionRegistry() {
    }

    public static void registerForItemClass(Class<? extends Item> itemClass, SkillActionHandler handler) {
        if (itemClass == null || handler == null) return;
        ENTRIES.add(new Entry(itemClass, handler));
    }

    public static SkillActionHandler resolve(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;

        Item item = stack.getItem();
        for (Entry e : ENTRIES) {
            if (e.itemClass.isInstance(item)) {
                return e.handler;
            }
        }
        return null;
    }

    private record Entry(Class<? extends Item> itemClass, SkillActionHandler handler) {

    }
}
