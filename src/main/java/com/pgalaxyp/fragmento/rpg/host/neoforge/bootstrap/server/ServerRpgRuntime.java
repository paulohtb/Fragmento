package com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.rpg.action.executor.*;
import com.pgalaxyp.fragmento.rpg.action.registry.*;
import com.pgalaxyp.fragmento.rpg.action.runtime.*;
import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.core.content.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.domain.time.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.damage.api.*;
import com.pgalaxyp.fragmento.rpg.damage.integration.*;
import com.pgalaxyp.fragmento.rpg.damage.system.*;
import com.pgalaxyp.fragmento.rpg.engine.*;
import com.pgalaxyp.fragmento.rpg.engine.intent.*;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.*;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.persist.*;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.world.command.*;
import com.pgalaxyp.fragmento.rpg.ports.*;
import com.pgalaxyp.fragmento.rpg.targeting.minecraft.*;
import java.util.*;
import net.minecraft.server.*;

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

        var damageSnapshots = new CoreDamageSnapshotProvider();
        WorldCommandPort worldCommands = new NeoForgeWorldCommandPort(server);
        JournalPort journal = new NeoForgeJournalPortStub();
        ActionRegistry registry = new ActionRegistry();
        registry.register(ActionType.INSTANT, new InstantActionExecutor());
        registry.register(ActionType.COMBO, new ComboActionExecutor());
        ActionRuntimeStore actions = new ActionRuntimeStore(registry);

        this.engine = new RpgEngine(queue, actions, targeting.service(), targeting.world(), damageService, damageSnapshots, worldCommands, new NeoForgeEventSinkPort(), journal, new NeoForgeSnapshotPort(), content, new GameState(new FrameContext(0, 0), new TreeMap<>()));
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