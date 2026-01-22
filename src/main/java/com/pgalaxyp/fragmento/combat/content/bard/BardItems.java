package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.mod.FragmentoMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.registries.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

@EventBusSubscriber(modid = FragmentoMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class BardItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, FragmentoMod.MODID);
    public static final DeferredHolder<Item, Item> FLUTE = ITEMS.register("flute", () -> new Item(new Item.Properties().stacksTo(1)));
    public static void register(IEventBus bus) { ITEMS.register(bus); }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() ->
                BardMinecraftBindings.registerFlute(
                        McClientBindings.WEAPON_BINDING,
                        FLUTE::value
                )
        );
    }

    private BardItems() {}
}