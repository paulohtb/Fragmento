package com.pgalaxyp.fragmento.cosmetic.client.state;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

import java.util.UUID;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT)
public final class CosmeticsClientTrackingEvents {

    private CosmeticsClientTrackingEvents() {}

    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) return;

        UUID id = player.getUUID();
        ClientCosmetics.remove(id);
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientCosmetics.clearAll();
    }
}