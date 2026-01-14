package com.pgalaxyp.fragmento.rpg.platform.neoforge.items;

import com.pgalaxyp.fragmento.rpg.platform.neoforge.bootstrap.FragmentoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FragmentoItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, FragmentoMod.MOD_ID);

    public static final DeferredHolder<Item, Item> FLUTE = REGISTRY.register(
            "flute",
            () -> new FluteItem(new Item.Properties().stacksTo(1))
    );

    public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(FLUTE.get());
        }
    }

    private FragmentoItems() {}
}