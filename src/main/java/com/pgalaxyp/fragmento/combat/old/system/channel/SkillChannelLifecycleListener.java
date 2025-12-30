package com.pgalaxyp.fragmento.combat.old.system.channel;

import com.pgalaxyp.fragmento.combat.old.system.skill.ServerSkillStateServices;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillRateLimitService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.GAME)
public final class SkillChannelLifecycleListener {

    private SkillChannelLifecycleListener() {
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ChannelingService.clearPlayer(player);
            SkillRateLimitService.clear(player);
        }
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ChannelingService.clearPlayer(player);
            SkillRateLimitService.clear(player);
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        ChannelingService.clearAllServer();
        ServerSkillStateServices.clearAll();
    }
}