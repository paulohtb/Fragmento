package com.pgalaxyp.fragmento.client.lifecycle;

import com.pgalaxyp.fragmento.cosmetics.client.CosmeticsClientState;
import com.pgalaxyp.fragmento.tiers.client.TierClientState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT)
public final class FragmentoClientSessionReset {

    private FragmentoClientSessionReset() {
    }

    @SubscribeEvent
    public static void onLoggingIn(final ClientPlayerNetworkEvent.LoggingIn event) {
        TierClientState.clear();
        CosmeticsClientState.clearAll();
    }

    @SubscribeEvent
    public static void onLoggingOut(final ClientPlayerNetworkEvent.LoggingOut event) {
        TierClientState.clear();
        CosmeticsClientState.clearAll();
    }
}