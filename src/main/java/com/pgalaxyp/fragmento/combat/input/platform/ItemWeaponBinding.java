package com.pgalaxyp.fragmento.combat.input.platform;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

public final class ItemWeaponBinding {

    private final NavigableMap<ResourceLocation, WeaponId> byItemId = new TreeMap<>();

    public void register(Item item, WeaponId weaponId) {
        if (item == null || weaponId == null) throw new IllegalArgumentException();
        byItemId.put(BuiltInRegistries.ITEM.getKey(item), weaponId);
    }

    public Optional<WeaponId> resolve(ItemStack stack) {
        if (stack == null) throw new IllegalArgumentException();
        return Optional.ofNullable(byItemId.get(BuiltInRegistries.ITEM.getKey(stack.getItem())));
    }
}