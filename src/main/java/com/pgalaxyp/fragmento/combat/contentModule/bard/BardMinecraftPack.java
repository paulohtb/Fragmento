package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.contentModule.minecraft.MinecraftContentPack;
import com.pgalaxyp.fragmento.combat.inputModule.minecraft.ItemWeaponBinding;
import java.util.Objects;
import net.neoforged.bus.api.IEventBus;

public enum BardMinecraftPack implements MinecraftContentPack {
    INSTANCE;

    @Override
    public void register(IEventBus modBus) {
        BardItems.register(Objects.requireNonNull(modBus));
    }

    @Override
    public void registerClientBindings(ItemWeaponBinding binding) {
        BardMinecraftBindings.registerFlute(Objects.requireNonNull(binding), BardItems.FLUTE::value);
    }
}