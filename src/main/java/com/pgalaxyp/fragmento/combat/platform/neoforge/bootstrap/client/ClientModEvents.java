package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client;

import com.pgalaxyp.fragmento.combat.content.defaults.*;
import com.pgalaxyp.fragmento.combat.input.minecraft.ItemWeaponBinding;
import com.pgalaxyp.fragmento.combat.input.minecraft.McInputModule;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.input.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.items.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.fml.common.EventBusSubscriber.*;

@EventBusSubscriber(modid = FragmentoMod.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    private static final int DEFAULT_PRIMARY_DEBOUNCE_FRAMES = 4;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> {
            ClientModRuntime.install();

            ItemWeaponBinding mapping = new ItemWeaponBinding();
            mapping.register(ModItemsRegistry.FLUTE.get(), DefaultWeapons.FLUTE);

            var module = McInputModule.createClient(
                    mapping,
                    DEFAULT_PRIMARY_DEBOUNCE_FRAMES,
                    () -> ClientModRuntime.localActorId(),
                    new NfSnapshotSource(),
                    new NfIntentSender()
            );
            module.register(NeoForge.EVENT_BUS);
        });
    }

    private ClientModEvents() {}
}
