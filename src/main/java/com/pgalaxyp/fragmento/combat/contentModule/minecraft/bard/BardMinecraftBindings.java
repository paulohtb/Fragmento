package com.pgalaxyp.fragmento.combat.contentModule.minecraft.bard;

import com.pgalaxyp.fragmento.combat.contentModule.bard.BardIds;
import com.pgalaxyp.fragmento.combat.contentModule.minecraft.MinecraftWeaponBindingEntry;
import java.util.List;

public final class BardMinecraftBindings {
    public static List<MinecraftWeaponBindingEntry> bindings() {
        return List.of(new MinecraftWeaponBindingEntry(BardItems.FLUTE::value, BardIds.FLUTE));
    }
    private BardMinecraftBindings() {}
}