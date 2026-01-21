package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.input.platform.ItemWeaponBinding;
import net.minecraft.world.item.Item;

public final class BardMinecraftBindings {
    public static void registerFlute(ItemWeaponBinding binding, Item fluteItem) {
        if (binding == null || fluteItem == null) throw new IllegalArgumentException();
        binding.register(fluteItem, BardIds.FLUTE);
    }

    private BardMinecraftBindings() {}
}