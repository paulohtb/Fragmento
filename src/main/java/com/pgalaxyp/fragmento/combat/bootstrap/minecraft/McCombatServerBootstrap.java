package com.pgalaxyp.fragmento.combat.bootstrap.minecraft;

import com.pgalaxyp.fragmento.combat.ability.system.AbilityCombatEngine;
import com.pgalaxyp.fragmento.combat.ability.system.AbilityInput;
import com.pgalaxyp.fragmento.combat.ability.system.AbilityView;
import com.pgalaxyp.fragmento.combat.actor.ActorJoinInputSystem;
import com.pgalaxyp.fragmento.combat.actor.ActorJoinSystem;
import com.pgalaxyp.fragmento.combat.actor.AutoActorJoinFromIntentsSystem;
import com.pgalaxyp.fragmento.combat.actor.DefaultActorService;
import com.pgalaxyp.fragmento.combat.content.bard.*;
import com.pgalaxyp.fragmento.combat.core.def.SpawnDefaultsProvider;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.damage.integration.DefaultSnapshotProvider;
import com.pgalaxyp.fragmento.combat.damage.system.DefaultDamageService;
import com.pgalaxyp.fragmento.combat.effect.system.EffectEngine;
import com.pgalaxyp.fragmento.combat.effect.system.EffectExecution;
import com.pgalaxyp.fragmento.combat.engine.*;
import com.pgalaxyp.fragmento.combat.flow.FlowPipeline;
import com.pgalaxyp.fragmento.combat.orchestrator.DefaultCombatOrchestrator;
import com.pgalaxyp.fragmento.combat.transport.SnapshotPort;
import com.pgalaxyp.fragmento.combat.world.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.skill.api.SkillModule;
import com.pgalaxyp.fragmento.combat.targeting.platform.minecraft.McTargetingModule;
import java.util.List;
import java.util.Objects;
import net.minecraft.server.MinecraftServer;

public final class McCombatServerBootstrap {
    public record ServerRuntime(GameEngine engine, ServerIntentQueue intents) {}

    public static ServerRuntime create(MinecraftServer server, WorldCommandPort world, SnapshotPort snapshots, SpawnDefaultsProvider spawnDefaults) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(world);
        Objects.requireNonNull(snapshots);
        Objects.requireNonNull(spawnDefaults);

        var targeting = McTargetingModule.createServer(server);
        var abilityEngine = new AbilityCombatEngine(BardAbilities.abilities(), targeting.service());
        var skillModule = SkillModule.of(BardSkills.rules());
        var effects = new EffectEngine(BardEffects.effects(), new DefaultDamageService(), new DefaultSnapshotProvider());
        var actors = new DefaultActorService();
        var pipeline = new FlowPipeline(List.of(
                new ActorJoinInputSystem(),
                new AutoActorJoinFromIntentsSystem(),
                new ActorJoinSystem(actors, spawnDefaults),
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