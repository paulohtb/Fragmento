package com.pgalaxyp.fragmento.combat.contentModule.minecraft;

import java.util.*;
import net.neoforged.bus.api.IEventBus;

public record MinecraftContentRegistry(List<MinecraftContentPack> packs) {
    public MinecraftContentRegistry {
        packs = List.copyOf(Objects.requireNonNull(packs));
        for (var p : packs) Objects.requireNonNull(p);
    }

    public void register(IEventBus modBus) {
        Objects.requireNonNull(modBus);
        for (var p : packs) p.register(modBus);
    }

    public List<MinecraftWeaponBindingEntry> clientWeaponBindings() {
        ArrayList<MinecraftWeaponBindingEntry> out = new ArrayList<>();
        for (var p : packs) {
            var list = p.clientWeaponBindings();
            if (list == null || list.isEmpty()) continue;
            for (var e : list) {
                if (e == null) throw new IllegalArgumentException();
                out.add(e);
            }
        }
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }
}