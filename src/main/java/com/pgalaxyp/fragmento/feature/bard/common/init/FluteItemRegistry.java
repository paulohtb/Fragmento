package com.pgalaxyp.fragmento.feature.bard.common.init;

import com.pgalaxyp.fragmento.feature.bard.common.weapon.FluteInstrumentItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FluteItemRegistry {

    private FluteItemRegistry() {
    }

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, "fragmento");

    public static final DeferredHolder<Item, Item> FLUTE =
            ITEMS.register(
                    "flute",
                    () -> new FluteInstrumentItem(
                            new Item.Properties().rarity(Rarity.UNCOMMON)
                    )
            );
}
