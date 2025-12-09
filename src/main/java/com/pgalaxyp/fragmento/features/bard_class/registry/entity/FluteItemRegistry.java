package com.pgalaxyp.fragmento.features.bard_class.registry.entity;

import com.pgalaxyp.fragmento.features.bard_class.instrument.FluteItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FluteItemRegistry {

    private FluteItemRegistry() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, "fragmento");

    public static final DeferredHolder<Item, Item> FLUTE =
            ITEMS.register(
                    "flute",
                    () -> new FluteItem(
                            new Item.Properties().rarity(Rarity.UNCOMMON)
                    )
            );
}
