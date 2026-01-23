package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.inputModule.minecraft.ItemWeaponBinding;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;

public final class BardMinecraftBindings {

    public static void registerFlute(ItemWeaponBinding binding, Supplier<Item> fluteItem) {
        if (binding == null || fluteItem == null) throw new IllegalArgumentException();
        binding.register(fluteItem, BardIds.FLUTE);
    }

    private BardMinecraftBindings() {}
}