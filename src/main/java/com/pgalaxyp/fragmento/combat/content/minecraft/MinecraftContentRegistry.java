package com.pgalaxyp.fragmento.combat.content.minecraft;

import com.pgalaxyp.fragmento.combat.input.platform.ItemWeaponBinding;
import java.util.*;
import net.neoforged.bus.api.IEventBus;

public record MinecraftContentRegistry(List<MinecraftContentPack> packs) {
    public MinecraftContentRegistry {
        packs = List.copyOf(Objects.requireNonNull(packs));
        packs.forEach(Objects::requireNonNull);
    }

    public void register(IEventBus modBus) {
        Objects.requireNonNull(modBus);
        packs.forEach(p -> p.register(modBus));
    }

    public ItemWeaponBinding clientWeaponBinding() {
        var binding = new ItemWeaponBinding();
        packs.forEach(p -> p.registerClientBindings(binding));
        return binding;
    }
}