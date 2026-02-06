package com.pgalaxyp.fragmento.combat.contentModule.minecraft;

import java.util.List;
import net.neoforged.bus.api.IEventBus;

public interface MinecraftContentPack {
    void register(IEventBus modBus);
    default List<MinecraftWeaponBindingEntry> weaponBindings() { return List.of(); }
}