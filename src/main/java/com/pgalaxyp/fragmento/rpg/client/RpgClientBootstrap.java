package com.pgalaxyp.fragmento.rpg.client;

import com.pgalaxyp.fragmento.rpg.client.event.ClientInputController;
import com.pgalaxyp.fragmento.rpg.client.event.ClientInputTickEvent;
import com.pgalaxyp.fragmento.rpg.client.network.ClientIntentSender;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import static com.pgalaxyp.fragmento.bootstrap.FragmentoMod.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public final class RpgClientBootstrap {

    private static ClientInputController controller;

    @SubscribeEvent
    public static void onClientJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientIntentSender sender = new ClientIntentSender();
        controller = new ClientInputController(sender);
        ClientInputTickEvent.bind(controller);
    }

    @SubscribeEvent
    public static void onClientLeave(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientInputTickEvent.bind(null);
        controller = null;
    }

    private RpgClientBootstrap() {}
}