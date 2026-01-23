package com.pgalaxyp.fragmento.combat.bootstrap;

import com.pgalaxyp.fragmento.combat.ability.system.AbilityCombatEngine;
import com.pgalaxyp.fragmento.combat.ability.system.AbilityInput;
import com.pgalaxyp.fragmento.combat.ability.system.AbilityView;
import com.pgalaxyp.fragmento.combat.actor.minecraft.McActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.actor.system.ActorSyncSystem;
import com.pgalaxyp.fragmento.combat.combo.DefaultCombatOrchestrator;
import com.pgalaxyp.fragmento.combat.content.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.core.ids.ClassId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.damage.integration.DefaultSnapshotProvider;
import com.pgalaxyp.fragmento.combat.damage.system.DefaultDamageService;
import com.pgalaxyp.fragmento.combat.effect.system.EffectEngine;
import com.pgalaxyp.fragmento.combat.effect.system.EffectExecution;
import com.pgalaxyp.fragmento.combat.engine.GameEngine;
import com.pgalaxyp.fragmento.combat.engine.IntentQueue;
import com.pgalaxyp.fragmento.combat.engine.ServerIntentQueue;
import com.pgalaxyp.fragmento.combat.flow.FlowPipeline;
import com.pgalaxyp.fragmento.combat.flow.FrameContext;
import com.pgalaxyp.fragmento.combat.skill.api.SkillModule;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingService;
import com.pgalaxyp.fragmento.combat.targeting.platform.McTargetingModule;
import com.pgalaxyp.fragmento.combat.transport.SnapshotPort;
import java.util.List;
import java.util.Objects;

import com.pgalaxyp.fragmento.combat.world.WorldCommandPort;
import net.minecraft.server.MinecraftServer;

public final class McCombatServerBootstrap {

    public record ServerRuntime(GameEngine engine, ServerIntentQueue intents) {}

    public static ServerRuntime create(MinecraftServer server, WorldCommandPort world, SnapshotPort snapshots, ContentCatalog catalog, ClassId defaultClassId) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(world);
        Objects.requireNonNull(snapshots);
        Objects.requireNonNull(catalog);
        Objects.requireNonNull(defaultClassId);

        TargetingService targeting = McTargetingModule.createServer(server);

        var actorPort = new McActorSnapshotPort(server, defaultClassId);
        var actorSync = new ActorSyncSystem(actorPort);

        var abilityEngine = new AbilityCombatEngine(catalog.abilities(), targeting);
        var skillModule = SkillModule.of(catalog.skills());
        var effects = new EffectEngine(catalog.effects(), new DefaultDamageService(), new DefaultSnapshotProvider());

        var pipeline = new FlowPipeline(List.of(
                actorSync,
                new AbilityInput(),
                skillModule.system(),
                new DefaultCombatOrchestrator(abilityEngine),
                new EffectExecution(effects),
                new AbilityView(abilityEngine)
        ));

        var intentQueue = new IntentQueue();
        var intents = new ServerIntentQueue(intentQueue);
        var initial = GameState.empty(new FrameContext(0L, 0));
        var engine = new GameEngine(intents, pipeline, world, snapshots, initial);
        return new ServerRuntime(engine, intents);
    }

    private McCombatServerBootstrap() {}
}