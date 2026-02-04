package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.*;
import java.util.function.Supplier;
import net.minecraft.world.item.*;

public final class ItemWeaponBinding {
    private final Map<Item, WeaponId> resolved = new HashMap<>();
    private final Map<Supplier<Item>, WeaponId> pending = new HashMap<>();

    public void register(Supplier<Item> item, WeaponId weaponId) {
        if (item == null || weaponId == null) throw new IllegalArgumentException();
        pending.put(item, weaponId);
    }

    public Optional<WeaponId> resolve(ItemStack stack) {
        if (stack == null) throw new IllegalArgumentException();
        Item item = stack.getItem();
        WeaponId direct = resolved.get(item);
        if (direct != null) return Optional.of(direct);
        if (!pending.isEmpty()) {
            pending.entrySet().removeIf(e -> {
                Item it = e.getKey().get();
                if (it == null) return false;
                resolved.put(it, e.getValue());
                return true;
            });
        }
        return Optional.ofNullable(resolved.get(item));
    }
}