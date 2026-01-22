package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.mod.FragmentoMod;
import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BardItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, FragmentoMod.MODID);
    public static final DeferredHolder<Item, Item> FLUTE = ITEMS.register("flute", () -> new Item(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus bus) {
        ITEMS.register(Objects.requireNonNull(bus));
    }

    private BardItems() {}
}