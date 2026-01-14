package com.pgalaxyp.fragmento.rpg.platform.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.rpg.ports.*;
import com.pgalaxyp.fragmento.rpg.engine.*;
import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.damage.api.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.combo.state.*;
import com.pgalaxyp.fragmento.rpg.combo.skill.*;
import com.pgalaxyp.fragmento.rpg.core.content.*;
import com.pgalaxyp.fragmento.rpg.engine.intent.*;
import com.pgalaxyp.fragmento.rpg.content.*;
import com.pgalaxyp.fragmento.rpg.damage.system.*;
import com.pgalaxyp.fragmento.rpg.combo.registry.*;
import com.pgalaxyp.fragmento.rpg.action.runtime.*;
import com.pgalaxyp.fragmento.rpg.combo.internal.*;
import com.pgalaxyp.fragmento.rpg.action.executor.*;
import com.pgalaxyp.fragmento.rpg.action.registry.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.time.*;
import com.pgalaxyp.fragmento.rpg.core.events.intent.*;
import com.pgalaxyp.fragmento.rpg.damage.integration.*;
import com.pgalaxyp.fragmento.rpg.targeting.minecraft.*;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.persist.*;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.*;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.world.command.*;
import java.util.*;
import net.minecraft.server.*;

public final class ServerRpgRuntime implements ServerIntentReceiverPort {

    private static volatile ServerRpgRuntime active;
    private final IntentQueue queue;
    private final GameEngine engine;
    private int tickIndex;

    public ServerRpgRuntime(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }
        this.queue = new IntentQueue();

        GameContent content = DefaultContent.create();
        var targeting = GameTargetingBootstrap.createServer(server);
        DamageService damageService = new DefaultDamageService();
        var damageSnapshots = new CoreDamageSnapshotProvider();
        WorldCommandPort worldCommands = new NeoForgeWorldCommandPort(server);
        JournalPort journal = new NeoForgeJournalPortStub();
        ActionRegistry registry = new ActionRegistry();
        registry.register(ActionType.INSTANT, new InstantActionExecutor());
        registry.register(ActionType.TIMED_SEQUENCE, new TimedSequenceActionExecutor());
        ActionStore actions = new ActionStore(registry);
        InMemoryComboTracker tracker = new InMemoryComboTracker();
        InMemoryComboCatalog catalog = new InMemoryComboCatalog();
        DefaultCombos.register(catalog);
        ComboSkillResolver skills = new ComboSkillResolver(List.of());
        ComboService combo = new ComboEngine(tracker, catalog, skills);

        this.engine = new GameEngine(queue, combo, actions, targeting.service(), targeting.world(), damageService, damageSnapshots, worldCommands, new NeoForgeEventSinkPort(), journal, new NeoForgeSnapshotPort(), content, new GameState(new FrameContext(0, 0), new TreeMap<>()));
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