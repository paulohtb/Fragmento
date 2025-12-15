package com.pgalaxyp.fragmento.content.bard.registry;

import com.pgalaxyp.fragmento.content.bard.catalyst.FluteCatalystInstance;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FluteCatalystRegistry {

    public static DeferredHolder<Item, FluteCatalystInstance> FLUTE;

    private FluteCatalystRegistry() {
    }

    public static void register(DeferredRegister<Item> items) {
        FLUTE = items.register(
                "flute",
                () -> new FluteCatalystInstance(
                        new Item.Properties().stacksTo(1)
                )
        );
    }
}
