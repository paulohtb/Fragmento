package com.pgalaxyp.fragmento.cosmetics.client.bootstrap;

import com.pgalaxyp.fragmento.cosmetics.client.network.CosmeticsClientNetwork;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT)
public final class CosmeticsClientJoinSync {

    private CosmeticsClientJoinSync() {}

    @SubscribeEvent
    public static void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        CosmeticsClientNetwork.requestSync();
    }
}