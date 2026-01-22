package com.pgalaxyp.fragmento.combat.input.platform;

import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
                Item resolvedItem = e.getKey().get();
                if (resolvedItem == null) return false;
                resolved.put(resolvedItem, e.getValue());
                return true;
            });
            return Optional.ofNullable(resolved.get(item));
        }

        return Optional.empty();
    }
}