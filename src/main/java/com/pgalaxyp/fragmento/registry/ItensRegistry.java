package com.pgalaxyp.fragmento.registry;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;

public class ItensRegistry {

    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Fragmento.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final DeferredHolder<Item, Item> LUTE =
            ITEMS.register("lute",
                    () -> new LuteWeapon(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> DRUM =
            ITEMS.register("drum",
                    () -> new DrumWeaponItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> FLUTE =
            ITEMS.register("flute",
                    () -> new FluteWeapon(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> GUITAR =
            ITEMS.register("guitar",
                    () -> new GuitarWeaponItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> LIRA =
            ITEMS.register("lira",
                    () -> new LiraWeapon(new Item.Properties().rarity(Rarity.EPIC)));

    public static Collection<DeferredHolder<Item, ? extends Item>> getFragmentoItens() {
        return ITEMS.getEntries();
    }
}