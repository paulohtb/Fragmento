package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.combat.action.system.*;
import com.pgalaxyp.fragmento.combat.combo.skill.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import com.pgalaxyp.fragmento.combat.combo.system.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.content.defaults.DefaultContent;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.cycle.system.*;
import com.pgalaxyp.fragmento.combat.damage.integration.*;
import com.pgalaxyp.fragmento.combat.damage.system.*;
import com.pgalaxyp.fragmento.combat.effect.system.*;
import com.pgalaxyp.fragmento.combat.engine.*;
import com.pgalaxyp.fragmento.combat.engine.intent.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.world.NfWorldCommands;
import com.pgalaxyp.fragmento.combat.ports.*;
import java.util.*;

import com.pgalaxyp.fragmento.combat.targeting.minecraft.McTargetingModule;
import net.minecraft.server.*;

public final class ServerModRuntime implements ServerIntentReceiverPort {
    private static volatile ServerModRuntime active;
    public static void activate(ServerModRuntime rt) { active = rt; NfRuntimeRefs.setServerReceiver(rt); }
    public static void deactivate() { active = null; NfRuntimeRefs.clearServerReceiver(); }
    public static ServerModRuntime getActive() { return active; }

    private final IntentQueue queue = new IntentQueue();
    private final GameEngine engine;
    private int tickIndex;

    public ServerModRuntime(MinecraftServer server) {
        GameContent content = DefaultContent.create();
        var targeting = McTargetingModule.createServer(server);
        var damage = new DefaultDamageService();
        var snapshots = new DefaultSnapshotProvider();
        ComboTracker comboTracker = new InMemoryComboTracker();
        ComboSkillResolver comboSkills = new ComboSkillResolver(List.of());
        var combo = new ComboEngine();
        var cycles = new ActionCycleEngine(content.cycles());
        var actions = new DefaultActionService(id -> content.actions().action(id));
        var effects = new EffectEngine(content, damage, snapshots, targeting.service());

        engine = new GameEngine(
                queue,
                content,
                comboTracker,
                comboSkills,
                combo,
                cycles,
                actions,
                effects,
                new NfWorldCommands(server),
                new NfEventSink(),
                new NfSnapshotSink(),
                new GameState(new FrameContext(0, 0), new TreeMap<>())
        );
    }

    @Override
    public void enqueue(IntentEnvelope envelope) { queue.push(envelope); }

    public void onPlayerJoin(ActorId actorId) { enqueue(IntentEnvelope.of(actorId, new ActorJoinIntent())); }

    public void tick() { engine.step(tickIndex++); }
}
