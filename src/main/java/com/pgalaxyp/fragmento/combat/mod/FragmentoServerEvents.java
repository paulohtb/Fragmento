package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.transport.*;
import com.pgalaxyp.fragmento.combat.world.port.*;
import com.pgalaxyp.fragmento.combat.engine.GameEngine;
import com.pgalaxyp.fragmento.combat.content.bard.BardSpawnDefaultsProvider;
import com.pgalaxyp.fragmento.combat.bootstrap.minecraft.McCombatServerBootstrap;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = FragmentoMod.MODID)
public final class FragmentoServerEvents {
    private static final Map<MinecraftServer, ServerRuntime> RUNTIMES = new WeakHashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        ServerRuntime runtime = RUNTIMES.computeIfAbsent(server, FragmentoServerEvents::createRuntime);
        runtime.engine.step(runtime.tickIndex.getAndIncrement());
    }

    private static ServerRuntime createRuntime(MinecraftServer server) {
        WorldCommandPort world = new McWorldCommandPort(server);
        SnapshotPort snapshots = server.isDedicatedServer() ? __ -> {} : new LocalSnapshotPort(FragmentoClientEvents.clientReceiver());

        var spawnDefaults = new BardSpawnDefaultsProvider(10, 10);
        var created = McCombatServerBootstrap.create(server, world, snapshots, spawnDefaults);

        if (!server.isDedicatedServer()) { FragmentoMod.INTEGRATED_SERVER_INTENTS.set(created.intents()); }

        return new ServerRuntime(created.engine(), new AtomicInteger());
    }

    private record ServerRuntime(GameEngine engine, AtomicInteger tickIndex) { }

    private FragmentoServerEvents() {}
}