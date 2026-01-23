package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.bootstrap.McCombatServerBootstrap;
import com.pgalaxyp.fragmento.combat.content.FragmentoDomainContent;
import com.pgalaxyp.fragmento.combat.content.bard.BardIds;
import com.pgalaxyp.fragmento.combat.engine.GameEngine;
import com.pgalaxyp.fragmento.combat.transport.LocalSnapshotPort;
import com.pgalaxyp.fragmento.combat.transport.NoopSnapshotPort;
import com.pgalaxyp.fragmento.combat.transport.SnapshotPort;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.pgalaxyp.fragmento.combat.world.McWorldCommandPort;
import com.pgalaxyp.fragmento.combat.world.WorldCommandPort;
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

        SnapshotPort snapshots;
        if (server.isDedicatedServer()) {
            snapshots = new NoopSnapshotPort();
        } else {
            snapshots = new LocalSnapshotPort(FragmentoClientEvents.clientReceiver());
        }

        var created = McCombatServerBootstrap.create(server, world, snapshots, FragmentoDomainContent.CATALOG, BardIds.BARD);

        if (!server.isDedicatedServer()) {
            FragmentoMod.INTEGRATED_SERVER_INTENTS.set(created.intents());
        }

        return new ServerRuntime(created.engine(), new AtomicInteger());
    }

    private record ServerRuntime(GameEngine engine, AtomicInteger tickIndex) {}

    private FragmentoServerEvents() {}
}