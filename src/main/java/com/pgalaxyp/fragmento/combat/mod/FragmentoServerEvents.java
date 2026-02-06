package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@EventBusSubscriber(modid = FragmentoPlatform.MODID)
public final class FragmentoServerEvents {
    @SubscribeEvent public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        var runtime = FragmentoServerRuntimeRegistry.getOrCreate(server);
        runtime.engine().step(runtime.tickIndex().getAndIncrement());
    }

    @SubscribeEvent public static void onServerStopped(ServerStoppedEvent event) {
        FragmentoServerRuntimeRegistry.remove(event.getServer());
    }

    private FragmentoServerEvents() {}
}