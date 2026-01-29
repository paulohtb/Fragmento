package com.pgalaxyp.fragmento.combat.contentModule.minecraft;

import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;

public record MinecraftWeaponBindingEntry(Supplier<Item> item, WeaponId weaponId) {
    public MinecraftWeaponBindingEntry {
        Objects.requireNonNull(item);
        Objects.requireNonNull(weaponId);
    }
}