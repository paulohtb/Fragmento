package com.pgalaxyp.fragmento.platform.events.lifecycle;

import com.pgalaxyp.fragmento.content.bard.entity.WindVortexLimitService;
import com.pgalaxyp.fragmento.gameplay.channel.ChannelingService;
import com.pgalaxyp.fragmento.gameplay.skill.SkillRateLimitService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.GAME)
public final class SkillChannelLifecycleListener {

    private SkillChannelLifecycleListener() {}

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ChannelingService.clearPlayer(player);
            SkillRateLimitService.clear(player);
            if (player.level() instanceof ServerLevel level) {
                WindVortexLimitService.clearOwner(level, player.getUUID());
            }
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
    }
}
