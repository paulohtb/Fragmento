package com.pgalaxyp.fragmento.rpg.client.event;

import com.pgalaxyp.fragmento.rpg.client.input.ClientKeyBindings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import static com.pgalaxyp.fragmento.bootstrap.FragmentoMod.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientKeybindEvents {

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(ClientKeyBindings.NORMAL_SKILL);
        event.register(ClientKeyBindings.SPECIAL_SKILL);
    }

    private ClientKeybindEvents() {}
}