package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityDef;
import com.pgalaxyp.fragmento.combat.actor.system.DefaultActorService;
import com.pgalaxyp.fragmento.combat.combo.system.ComboEngine;
import com.pgalaxyp.fragmento.combat.content.GameContent;
import com.pgalaxyp.fragmento.combat.content.defaults.DefaultContent;
import com.pgalaxyp.fragmento.combat.content.defaults.DefaultIds;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.damage.integration.DefaultSnapshotProvider;
import com.pgalaxyp.fragmento.combat.damage.system.DefaultDamageService;
import com.pgalaxyp.fragmento.combat.effect.system.EffectEngine;
import com.pgalaxyp.fragmento.combat.engine.GameEngine;
import com.pgalaxyp.fragmento.combat.engine.intent.IntentQueue;
import com.pgalaxyp.fragmento.combat.flow.FlowPipeline;
import com.pgalaxyp.fragmento.combat.intent.ActorJoinIntent;
import com.pgalaxyp.fragmento.combat.intent.IntentEnvelope;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire.NfRuntimeRefs;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire.NfSnapshotSink;
import com.pgalaxyp.fragmento.combat.platform.neoforge.world.NfWorldCommands;
import com.pgalaxyp.fragmento.combat.skill.system.DefaultSkillResolver;
import com.pgalaxyp.fragmento.combat.systems.*;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingFallback;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingMode;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingSpec;
import com.pgalaxyp.fragmento.combat.targeting.platform.minecraft.McTargetingModule;
import java.util.List;
import java.util.Map;
import net.minecraft.server.MinecraftServer;

public final class ServerModRuntime implements com.pgalaxyp.fragmento.combat.ports.ServerIntentReceiverPort {
    private static volatile ServerModRuntime active;
    public static void activate(ServerModRuntime rt) { active = rt; NfRuntimeRefs.setServerReceiver(rt); }
    public static void deactivate() { active = null; NfRuntimeRefs.clearServerReceiver(); }
    public static ServerModRuntime getActive() { return active; }

    private final IntentQueue queue = new IntentQueue();
    private final GameEngine engine;
    private int tickIndex;

    public ServerModRuntime(MinecraftServer server) {
        GameContent content = DefaultContent.create();

        var targeting = McTargetingModule.createServer(server).service();
        var effects = new EffectEngine(content, new DefaultDamageService(), new DefaultSnapshotProvider());

        var abilities = new com.pgalaxyp.fragmento.combat.ability.system.AbilityCombatEngine(
                Map.of(
                        DefaultIds.ABILITY_FLUTE,
                        new AbilityDef(
                                DefaultIds.ABILITY_FLUTE,
                                15,
                                0,
                                DefaultIds.EFFECT_FLUTE_MAGIC,
                                new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 8.0, TargetingFallback.SELF)
                        )
                ),
                targeting
        );

        var combos = new ComboEngine();
        var skills = DefaultSkillResolver.create();

        var pipeline = new FlowPipeline(List.of(
                new ActorJoinSystem(new DefaultActorService(), content),
                new ActionIntentToComboInputSystem(content),
                new ComboExecutionSystem(combos, content),
                new ComboTickSystem(combos),
                CombatOrchestratorSystem.plan(content, skills),
                new AbilityExecutionSystem(abilities),
                new AbilityTickSystem(abilities),
                CombatOrchestratorSystem.react(),
                new EffectExecutionSystem(effects),
                new AbilityViewSystem(abilities)
        ));

        engine = new GameEngine(
                queue,
                pipeline,
                new NfWorldCommands(server),
                new NfSnapshotSink(),
                GameState.empty(new FrameContext(0, 0))
        );
    }

    @Override public void enqueue(IntentEnvelope envelope) { queue.push(envelope); }
    public void onPlayerJoin(ActorId actorId) { enqueue(IntentEnvelope.of(actorId, new ActorJoinIntent())); }
    public void tick() { engine.step(tickIndex++); }
}