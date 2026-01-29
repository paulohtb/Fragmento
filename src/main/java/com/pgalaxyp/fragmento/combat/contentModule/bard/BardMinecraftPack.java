package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.contentModule.minecraft.*;
import java.util.*;
import net.neoforged.bus.api.IEventBus;

public enum BardMinecraftPack implements MinecraftContentPack {
    INSTANCE;

    @Override public void register(IEventBus modBus) {
        BardItems.register(Objects.requireNonNull(modBus));
    }

    @Override public List<MinecraftWeaponBindingEntry> clientWeaponBindings() {
        return BardMinecraftBindings.bindings();
    }
}