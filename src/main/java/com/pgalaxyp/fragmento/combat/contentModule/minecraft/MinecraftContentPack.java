package com.pgalaxyp.fragmento.combat.contentModule.minecraft;

import com.pgalaxyp.fragmento.combat.inputModule.minecraft.ItemWeaponBinding;
import net.neoforged.bus.api.IEventBus;

public interface MinecraftContentPack {
    void register(IEventBus modBus);
    default void registerClientBindings(ItemWeaponBinding binding) {}
}