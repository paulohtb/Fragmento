package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client;

import com.pgalaxyp.fragmento.combat.content.DefaultContent;
import com.pgalaxyp.fragmento.combat.platform.neoforge.items.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.bus.api.*;

public final class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent evt) {
        ItemWeaponMapping mapping = ItemWeaponMapping.get();
        mapping.register(ModItemsRegistry.FLUTE.get(), DefaultContent.FLUTE);
    }

    private ClientModEvents() {}
}