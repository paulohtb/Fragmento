package com.pgalaxyp.fragmento.cosmetics.server.sync;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.UUID;

@EventBusSubscriber(modid = "fragmento")
public final class CosmeticSyncEvents {

    private CosmeticSyncEvents() {}

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) {
            return;
        }
        UUID id = sp.getUUID();
        CosmeticSyncRuntime.syncNow(id);
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) {
            return;
        }
        UUID id = sp.getUUID();
        CosmeticSyncRuntime.onLogout(id);
    }
}