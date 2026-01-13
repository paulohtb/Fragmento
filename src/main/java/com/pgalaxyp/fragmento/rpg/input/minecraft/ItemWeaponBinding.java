package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.*;

public final class ItemWeaponBinding {

    private final NavigableMap<ResourceLocation, WeaponId> byItemId = new TreeMap<>();

    public void register(Item item, WeaponId weaponId) {
        if (item == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        byItemId.put(key, weaponId);
    }

    public Optional<WeaponId> resolve(ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException();
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return Optional.ofNullable(byItemId.get(key));
    }
}