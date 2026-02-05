package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.engineModule.api.GameEngine;
import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.contentModule.FragmentoDomainContent;
import com.pgalaxyp.fragmento.combat.damageModule.minecraft.McDamageWorldCommandPort;
import com.pgalaxyp.fragmento.combat.intentModule.minecraft.IntegratedServerIntentBridge;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@EventBusSubscriber(modid = FragmentoPlatform.MODID)
public final class FragmentoServerEvents {
    private static final Map<MinecraftServer, ServerRuntime> RUNTIMES = new HashMap<>();

    @SubscribeEvent public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        ServerRuntime runtime = RUNTIMES.computeIfAbsent(server, FragmentoServerEvents::createRuntime);
        runtime.engine.step(runtime.tickIndex.getAndIncrement());
    }

    @SubscribeEvent public static void onServerStopped(ServerStoppedEvent event) {
        MinecraftServer server = event.getServer();
        RUNTIMES.remove(server);
        if (!server.isDedicatedServer()) IntegratedServerIntentBridge.set(null);
    }

    private static ServerRuntime createRuntime(MinecraftServer server) {
        var damagePort = new McDamageWorldCommandPort(server);
        WorldCommandPort world = new McCombatWorldCommandSystem(damagePort);
        var created = McCombatServerBootstrap.create(server, world, FragmentoDomainContent.CATALOG, FragmentoDomainContent.DEFAULT_CLASS_ID);
        if (!server.isDedicatedServer()) IntegratedServerIntentBridge.set(created.intents());
        return new ServerRuntime(created.engine(), new AtomicInteger());
    }

    private record ServerRuntime(GameEngine engine, AtomicInteger tickIndex) {}

    private FragmentoServerEvents() {}
}