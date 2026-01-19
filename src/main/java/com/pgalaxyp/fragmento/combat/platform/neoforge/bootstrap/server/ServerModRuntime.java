package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.server;

import com.pgalaxyp.fragmento.combat.ability.system.*;
import com.pgalaxyp.fragmento.combat.actor.system.*;
import com.pgalaxyp.fragmento.combat.combo.system.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.content.defaults.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.damage.integration.*;
import com.pgalaxyp.fragmento.combat.damage.system.*;
import com.pgalaxyp.fragmento.combat.effect.system.*;
import com.pgalaxyp.fragmento.combat.engine.*;
import com.pgalaxyp.fragmento.combat.engine.intent.*;
import com.pgalaxyp.fragmento.combat.engine.system.*;
import com.pgalaxyp.fragmento.combat.intent.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.world.*;
import com.pgalaxyp.fragmento.combat.skill.system.*;
import com.pgalaxyp.fragmento.combat.targeting.platform.minecraft.*;
import java.util.*;
import net.minecraft.server.*;

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
        var abilities = new AbilityService(
                Map.of(
                        DefaultIds.ABILITY_FLUTE,
                        new com.pgalaxyp.fragmento.combat.ability.api.AbilityDef(
                                DefaultIds.ABILITY_FLUTE,
                                15,
                                DefaultIds.EFFECT_FLUTE_MAGIC,
                                new com.pgalaxyp.fragmento.combat.targeting.api.TargetingSpec(
                                        com.pgalaxyp.fragmento.combat.targeting.api.TargetingMode.RAYCAST_SINGLE,
                                        8.0,
                                        com.pgalaxyp.fragmento.combat.targeting.api.TargetingFallback.SELF
                                )
                        )
                ),
                targeting,
                effects
        );

        CombatFlowProcessor flow = new CombatFlowProcessor(
                content,
                new DefaultActorService(),
                new ComboEngine(),
                DefaultSkillResolver.create(),
                abilities
        );

        engine = new GameEngine(
                queue,
                content,
                flow,
                new NfWorldCommands(server),
                new NfSnapshotSink(),
                GameState.empty(new FrameContext(0, 0))
        );
    }

    @Override
    public void enqueue(IntentEnvelope envelope) { queue.push(envelope); }

    public void onPlayerJoin(ActorId actorId) { enqueue(IntentEnvelope.of(actorId, new ActorJoinIntent())); }

    public void tick() { engine.step(tickIndex++); }
}
