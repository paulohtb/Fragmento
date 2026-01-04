package com.pgalaxyp.fragmento.rpg.content.catalyst;

import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class ItemCatalystRegistry {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FragmentoMod.MODID);

    public static final DeferredHolder<Item, FluteItem> FLUTE =
            ITEMS.register("flute", () ->
                    new FluteItem(
                            new Item.Properties()
                                    .stacksTo(1))
            );

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    private ItemCatalystRegistry() {}
}