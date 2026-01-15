package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.input.minecraft.*;
import com.pgalaxyp.fragmento.combat.content.defaults.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.items.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.lifecycle.*;

public final class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent evt) {
        ItemWeaponBinding mapping = new ItemWeaponBinding();
        mapping.register(ModItemsRegistry.FLUTE.get(), DefaultWeapons.FLUTE);
    }

    private ClientModEvents() {}
}