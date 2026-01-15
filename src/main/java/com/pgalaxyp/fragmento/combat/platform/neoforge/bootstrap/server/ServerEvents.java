package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.FragmentoMod;
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
        ServerModRuntime rt = new ServerModRuntime(server);
        ServerModRuntime.activate(rt);
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        ServerModRuntime.deactivate();
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ServerModRuntime rt = ServerModRuntime.getActive();
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
        ServerModRuntime rt = ServerModRuntime.getActive();
        if (rt == null) {
            return;
        }
        rt.onPlayerJoin(new ActorId(sp.getUUID()));
    }

    private ServerEvents() {}
}