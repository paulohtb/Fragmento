package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class ItemWeaponBinding {

    private final NavigableMap<ResourceLocation, WeaponId> byItemId = new TreeMap<>();

    public void register(Item item, WeaponId weaponId) {
        if (item == null || weaponId == null) {
            throw new IllegalArgumentException();
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        if (key == null) {
            throw new IllegalArgumentException();
        }
        byItemId.put(key, weaponId);
    }

    public Optional<WeaponId> resolve(ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException();
        }
        Item item = stack.getItem();
        if (item == null) {
            return Optional.empty();
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        if (key == null) {
            return Optional.empty();
        }
        WeaponId wid = byItemId.get(key);
        return wid == null ? Optional.empty() : Optional.of(wid);
    }
}