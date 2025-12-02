package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.NEW.newnew.NewLuteTestWeapon;
import com.pgalaxyp.fragmento.item.bard.weapon.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItensRegistry {

    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Fragmento.MODID);

    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }


    public static final DeferredHolder<Item, Item> NEW_LYRE =
            ITEMS.register("new_lyre_item",
                    () -> new NewLyreWeapon(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> NEW_DRUM =
            ITEMS.register("new_drum_item",
                    () -> new NewDrumWeapon(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> NEW_FLUTE =
            ITEMS.register("new_flute_item",
                    () -> new NewFluteWeapon(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> NEW_LUTE_TEST =
            ITEMS.register("new_lute_test",
                    () -> new NewLuteTestWeapon(new Item.Properties().rarity(Rarity.UNCOMMON)));




//    public static final DeferredHolder<Item, Item> LYRE =
//            ITEMS.register("lyre",
//                    () -> new LyreWeapon(new Item.Properties().rarity(Rarity.EPIC)));
//    public static final DeferredHolder<Item, Item> LUTE =
//            ITEMS.register("lute",
//                    () -> new LuteWeapon(new Item.Properties().rarity(Rarity.EPIC)));
//    public static final DeferredHolder<Item, Item> DRUM =
//            ITEMS.register("drum",
//                    () -> new DrumWeapon(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Item> FLUTE =
            ITEMS.register("flute",
                    () -> new FluteWeapon(new Item.Properties().rarity(Rarity.EPIC)));
//    public static final DeferredHolder<Item, Item> GUITAR =
//            ITEMS.register("guitar",
//                    () -> new GuitarWeapon(new Item.Properties().rarity(Rarity.EPIC)));
}