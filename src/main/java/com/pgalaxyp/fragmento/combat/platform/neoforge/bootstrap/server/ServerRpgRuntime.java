package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.combat.engine.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import com.pgalaxyp.fragmento.combat.combo.skill.*;
import com.pgalaxyp.fragmento.combat.combo.system.*;
import com.pgalaxyp.fragmento.combat.engine.intent.*;
import com.pgalaxyp.fragmento.combat.action.runtime.*;
import com.pgalaxyp.fragmento.combat.damage.system.*;
import com.pgalaxyp.fragmento.combat.damage.integration.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.content.bridge.*;
import com.pgalaxyp.fragmento.combat.targeting.minecraft.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.core.events.intent.*;
import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.world.command.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.persist.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire.*;
import java.util.*;
import net.minecraft.server.*;

public final class ServerRpgRuntime implements ServerIntentReceiverPort {

    private static volatile ServerRpgRuntime active;

    public static void activate(ServerRpgRuntime rt) {
        active = rt;
        NFRuntimeRefs.setServerReceiver(rt);
    }

    public static void deactivate() {
        active = null;
        NFRuntimeRefs.clearServerReceiver();
    }

    public static ServerRpgRuntime getActive() {
        return active;
    }

    private final IntentQueue queue = new IntentQueue();
    private final GameEngine engine;
    private int tickIndex;

    public ServerRpgRuntime(MinecraftServer server) {
        GameContent content = DefaultContent.create();

        var targeting = MCTargetingModule.createServer(server);
        var damage = new DefaultDamageService();
        var snapshots = new CoreDamageSnapshotProvider();

        DefaultComboCatalog comboCatalog = new DefaultComboCatalog();
        ComboDefinitionSource comboSource = new ContentComboSource(comboCatalog);
        ComboService combo =
                new ComboEngine(
                        new InMemoryComboTracker(),
                        comboSource,
                        new ComboSkillResolver(List.of())
                );

        ActionStore actions = new ActionStore(content::action);

        engine =
                new GameEngine(
                        queue,
                        combo,
                        null,
                        actions,
                        new com.pgalaxyp.fragmento.combat.effect.system.EffectEngine(
                                content,
                                damage,
                                snapshots,
                                targeting.service()
                        ),
                        new NFWorldCommands(server),
                        new NFEventSink(),
                        new NeoForgeJournalPortStub(),
                        new NFSnapshotSink(),
                        content,
                        new GameState(new FrameContext(0, 0), new TreeMap<>())
                );
    }

    @Override
    public void enqueue(IntentEnvelope envelope) {
        queue.push(envelope);
    }

    public void onPlayerJoin(ActorId actorId) {
        enqueue(IntentEnvelope.of(actorId, new ActorJoinIntent()));
    }

    public void tick() {
        engine.step(tickIndex++);
    }
}