package com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.rpg.core.content.DefaultRpgContent;
import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.intent.ActorJoinIntent;
import com.pgalaxyp.fragmento.rpg.core.events.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.damage.api.DamageService;
import com.pgalaxyp.fragmento.rpg.damage.integration.CoreDamageSnapshotProvider;
import com.pgalaxyp.fragmento.rpg.damage.system.DefaultDamageService;
import com.pgalaxyp.fragmento.rpg.engine.RpgEngine;
import com.pgalaxyp.fragmento.rpg.engine.intent.IntentQueue;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.NeoForgeEventSinkPort;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.NeoForgeNetRuntimeRefs;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.NeoForgeSnapshotPort;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.persist.NeoForgeJournalPortStub;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.world.command.NeoForgeWorldCommandPort;
import com.pgalaxyp.fragmento.rpg.ports.JournalPort;
import com.pgalaxyp.fragmento.rpg.ports.ServerIntentReceiverPort;
import com.pgalaxyp.fragmento.rpg.ports.WorldCommandPort;
import com.pgalaxyp.fragmento.rpg.targeting.minecraft.GameTargetingBootstrap;
import java.util.TreeMap;
import net.minecraft.server.MinecraftServer;

public final class ServerRpgRuntime implements ServerIntentReceiverPort {

    private static volatile ServerRpgRuntime active;

    private final IntentQueue queue;
    private final RpgEngine engine;
    private int tickIndex;

    public ServerRpgRuntime(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }

        this.queue = new IntentQueue();
        RpgContent content = DefaultRpgContent.create();

        var targeting = GameTargetingBootstrap.createServer(server);

        DamageService damageService = new DefaultDamageService();
        com.pgalaxyp.fragmento.rpg.damage.snapshot.DamageSnapshotProvider damageSnapshots = new CoreDamageSnapshotProvider();

        WorldCommandPort worldCommands = new NeoForgeWorldCommandPort(server);
        JournalPort journal = new NeoForgeJournalPortStub();

        this.engine = new RpgEngine(
                queue,
                targeting.service(),
                targeting.world(),
                damageService,
                damageSnapshots,
                worldCommands,
                new NeoForgeEventSinkPort(),
                journal,
                new NeoForgeSnapshotPort(),
                content,
                new GameState(new FrameContext(0, 0), new TreeMap<>())
        );
    }

    public static void activate(ServerRpgRuntime rt) {
        if (rt == null) {
            throw new IllegalArgumentException();
        }
        active = rt;
        NeoForgeNetRuntimeRefs.setServerReceiver(rt);
    }

    public static void deactivate() {
        NeoForgeNetRuntimeRefs.clearServerReceiver();
        active = null;
    }

    public static ServerRpgRuntime getActive() {
        return active;
    }

    @Override
    public void enqueue(IntentEnvelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException();
        }
        queue.push(envelope);
    }

    public void onPlayerJoin(ActorId actorId) {
        enqueue(IntentEnvelope.of(actorId, new ActorJoinIntent()));
    }

    public void tick() {
        engine.step(tickIndex);
        tickIndex = Math.addExact(tickIndex, 1);
    }
}
