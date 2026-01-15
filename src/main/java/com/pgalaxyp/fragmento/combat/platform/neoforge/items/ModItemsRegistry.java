package com.pgalaxyp.fragmento.combat.platform.neoforge.items;

import net.minecraft.world.item.*;
import net.neoforged.neoforge.event.*;
import net.minecraft.core.registries.*;
import net.neoforged.neoforge.registries.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.*;

public final class ModItemsRegistry {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, FragmentoMod.MOD_ID);

    public static final DeferredHolder<Item, Item> FLUTE = REGISTRY.register(
            "flute", () -> new ModItemsProperties(new Item.Properties().stacksTo(1))
    );

    public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) { event.accept(FLUTE.get());}
    }

    private ModItemsRegistry() {}
}