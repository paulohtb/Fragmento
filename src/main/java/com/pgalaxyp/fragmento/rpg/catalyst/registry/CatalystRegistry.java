package com.pgalaxyp.fragmento.rpg.catalyst.registry;

import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CatalystRegistry {

    private final List<CatalystDefinition> definitions = new ArrayList<>();

    public void register(CatalystDefinition def) {
        definitions.add(Objects.requireNonNull(def));
    }

    public CatalystDefinition resolve(ItemStack mainHand) {
        if (mainHand == null || mainHand.isEmpty()) return null;

        for (CatalystDefinition def : definitions) {
            if (def.matches(mainHand)) return def;
        }
        return null;
    }
}