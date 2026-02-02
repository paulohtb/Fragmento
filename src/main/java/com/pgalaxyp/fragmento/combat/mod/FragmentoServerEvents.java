package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.engineModule.port.*;
import com.pgalaxyp.fragmento.combat.damageModule.minecraft.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameEngine;
import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import com.pgalaxyp.fragmento.combat.contentModule.FragmentoDomainContent;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = FragmentoPlatform.MODID)
public final class FragmentoServerEvents {
    private static final Map<MinecraftServer, ServerRuntime> RUNTIMES = new WeakHashMap<>();

    @SubscribeEvent public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        ServerRuntime runtime = RUNTIMES.computeIfAbsent(server, FragmentoServerEvents::createRuntime);
        runtime.engine.step(runtime.tickIndex.getAndIncrement());
    }

    private static ServerRuntime createRuntime(MinecraftServer server) {
        var damagePort = new McDamageWorldCommandPort(server);
        WorldCommandPort world = new McDamageWorldCommandSystem(damagePort);
        SnapshotPort snapshots = server.isDedicatedServer() ? new NoopSnapshotPort() : new LocalSnapshotPort(FragmentoMod.CLIENT_RECEIVER);
        var created = McCombatServerBootstrap.create(server, world, snapshots, FragmentoDomainContent.CATALOG, FragmentoDomainContent.DEFAULT_CLASS_ID);
        if (!server.isDedicatedServer()) FragmentoMod.INTEGRATED_SERVER_INTENTS.set(created.intents());
        return new ServerRuntime(created.engine(), new AtomicInteger());
    }

    private record ServerRuntime(GameEngine engine, AtomicInteger tickIndex) {}

    private FragmentoServerEvents() {}
}