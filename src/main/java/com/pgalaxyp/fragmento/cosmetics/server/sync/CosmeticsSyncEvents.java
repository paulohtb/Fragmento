package com.pgalaxyp.fragmento.cosmetics.server.sync;

import com.pgalaxyp.fragmento.cosmetics.server.service.CosmeticServices;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = "fragmento")
public final class CosmeticsSyncEvents {

    private CosmeticsSyncEvents() {}

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (CosmeticServices.service() == null) return;
        UUID id = sp.getUUID();
        CosmeticServices.service().onLogin(id);
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (CosmeticServices.service() == null) return;
        UUID id = sp.getUUID();
        CosmeticServices.service().onLogout(id);
    }
}