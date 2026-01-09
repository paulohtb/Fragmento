package com.pgalaxyp.fragmento.rpg.platform.minecraft.runtime;

import com.pgalaxyp.fragmento.rpg.core.content.DefaultRpgContent;
import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.intent.ActorJoinIntent;
import com.pgalaxyp.fragmento.rpg.core.event.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.engine.RpgEngine;
import com.pgalaxyp.fragmento.rpg.engine.intent.IntentQueue;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.port.*;
import com.pgalaxyp.fragmento.rpg.port.JournalPort;
import com.pgalaxyp.fragmento.rpg.port.WorldCommandPort;
import com.pgalaxyp.fragmento.rpg.port.WorldQueryPort;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import java.util.TreeMap;

public final class FragmentoServerRuntime {

    private static volatile FragmentoServerRuntime active;

    private final MinecraftServer server;
    private final IntentQueue queue;
    private final RpgEngine engine;
    private int tickIndex;

    public FragmentoServerRuntime(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }
        this.server = server;

        this.queue = new IntentQueue();
        RpgContent content = DefaultRpgContent.create();

        WorldQueryPort worldQueries = new MinecraftWorldQueryPortNeoForge(server);
        WorldCommandPort worldCommands = new MinecraftWorldCommandPortNeoForge(server);
        JournalPort journal = new MinecraftJournalPortStub();

        this.engine = new RpgEngine(
                queue,
                worldQueries,
                worldCommands,
                new MinecraftEventSinkPortNeoForge(),
                journal,
                new MinecraftSnapshotPortNeoForge(),
                content,
                new GameState(new FrameContext(0, 0), new TreeMap<>())
        );
    }

    public static void activate(FragmentoServerRuntime rt) {
        if (rt == null) {
            throw new IllegalArgumentException();
        }
        active = rt;
    }

    public static void deactivate() {
        active = null;
    }

    public static FragmentoServerRuntime getActive() {
        return active;
    }

    public static FragmentoServerRuntime get(IPayloadContext context) {
        return active;
    }

    public void enqueue(IntentEnvelope env) {
        if (env == null) {
            throw new IllegalArgumentException();
        }
        queue.push(env);
    }

    public void onPlayerJoin(ActorId actorId) {
        enqueue(IntentEnvelope.of(actorId, new ActorJoinIntent()));
    }

    public void tick() {
        engine.step(tickIndex);
        tickIndex = Math.addExact(tickIndex, 1);
    }

    public MinecraftServer server() {
        return server;
    }
}