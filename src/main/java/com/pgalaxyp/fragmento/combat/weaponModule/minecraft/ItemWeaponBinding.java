package com.pgalaxyp.fragmento.combat.weaponModule.minecraft;

import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.*;
import java.util.function.Supplier;
import net.minecraft.world.item.*;

public final class ItemWeaponBinding {
    private final Map<Item, WeaponId> resolved = new HashMap<>();
    private final Map<Supplier<Item>, WeaponId> pending = new HashMap<>();
    private boolean frozen;

    public void register(Supplier<Item> item, WeaponId weaponId) {
        Objects.requireNonNull(item);
        Objects.requireNonNull(weaponId);
        if (frozen) throw new IllegalStateException();
        pending.put(item, weaponId);
    }

    public void freeze() {
        if (frozen) return;
        frozen = true;
        resolvePending();
    }

    public Optional<WeaponId> resolve(ItemStack stack) {
        Objects.requireNonNull(stack);
        resolvePending();
        return Optional.ofNullable(resolved.get(stack.getItem()));
    }

    private void resolvePending() {
        if (pending.isEmpty()) return;
        for (var it = pending.entrySet().iterator(); it.hasNext(); ) {
            var e = it.next();
            var item = e.getKey().get();
            if (item == null) continue;
            resolved.put(item, e.getValue());
            it.remove();
        }
    }
}