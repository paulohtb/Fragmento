package com.pgalaxyp.fragmento.rpg.platform.neoforge.bootstrap.client;

import com.pgalaxyp.fragmento.rpg.core.content.DefaultContent;
import com.pgalaxyp.fragmento.rpg.host.api.LocalActorProvider;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.items.FragmentoItems;
import com.pgalaxyp.fragmento.rpg.input.minecraft.GameInputBootstrap;
import com.pgalaxyp.fragmento.rpg.input.minecraft.ItemWeaponBinding;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = FragmentoMod.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRpgRuntime.install();

        event.enqueueWork(() -> {
            ItemWeaponBinding mapping = new ItemWeaponBinding();
            mapping.register(FragmentoItems.FLUTE.get(), DefaultContent.FLUTE);

            LocalActorProvider provider = ClientRpgRuntime::localActorId;

            GameInputBootstrap.ClientModule input =
                    GameInputBootstrap.createClient(mapping, 2, provider);

            input.register(NeoForge.EVENT_BUS);
        });
    }

    private ClientModEvents() {}
}