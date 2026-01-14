package com.pgalaxyp.fragmento.rpg.platform.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.bootstrap.FragmentoMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = FragmentoMod.MOD_ID, bus = Bus.GAME)
public final class ServerEvents {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        var server = event.getServer();
        ServerRpgRuntime rt = new ServerRpgRuntime(server);
        ServerRpgRuntime.activate(rt);
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        ServerRpgRuntime.deactivate();
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ServerRpgRuntime rt = ServerRpgRuntime.getActive();
        if (rt == null) {
            return;
        }
        rt.tick();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof net.minecraft.server.level.ServerPlayer sp)) {
            return;
        }
        ServerRpgRuntime rt = ServerRpgRuntime.getActive();
        if (rt == null) {
            return;
        }
        rt.onPlayerJoin(new ActorId(sp.getUUID()));
    }

    private ServerEvents() {}
}