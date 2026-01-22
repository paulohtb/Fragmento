package com.pgalaxyp.fragmento.combat.content.minecraft;

import com.pgalaxyp.fragmento.combat.input.platform.ItemWeaponBinding;
import net.neoforged.bus.api.IEventBus;

public interface MinecraftContentPack {
    void register(IEventBus modBus);
    default void registerClientBindings(ItemWeaponBinding binding) {}
}