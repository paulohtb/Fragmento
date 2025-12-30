package com.pgalaxyp.fragmento.cosmetic.server.sync;

import com.pgalaxyp.fragmento.cosmetic.server.service.CosmeticServices;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = "fragmento")
public final class CosmeticsTrackingSyncEvents {

    private CosmeticsTrackingSyncEvents() {}

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event == null) return;
        if (!(event.getEntity() instanceof ServerPlayer tracker)) return;
        if (!(event.getTarget() instanceof ServerPlayer owner)) return;
        if (CosmeticServices.service() == null) return;

        UUID ownerId = owner.getUUID();
        UUID trackerId = tracker.getUUID();
        if (ownerId == null || trackerId == null) return;

        CosmeticServices.service().onStartTracking(ownerId, trackerId);
    }
}