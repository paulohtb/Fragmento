package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.content.minecraft.MinecraftContentPack;
import com.pgalaxyp.fragmento.combat.input.platform.ItemWeaponBinding;
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