package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.intentModule.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.port.*;
import com.pgalaxyp.fragmento.combat.actorModule.system.*;
import com.pgalaxyp.fragmento.combat.effectModule.system.*;
import com.pgalaxyp.fragmento.combat.damageModule.system.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingService;
import com.pgalaxyp.fragmento.combat.abilityModule.system.AbilityFrameSystem;
import com.pgalaxyp.fragmento.combat.inputModule.port.ServerIntentReceiverPort;
import com.pgalaxyp.fragmento.combat.damageModule.port.DefaultSnapshotProvider;
import com.pgalaxyp.fragmento.combat.targetingModule.minecraft.McTargetingModule;
import java.util.*;
import net.minecraft.server.MinecraftServer;

public final class McCombatServerBootstrap {
    public record ServerRuntime(GameEngine engine, ServerIntentReceiverPort intents) {}

    public static ServerRuntime create(MinecraftServer server, WorldCommandPort world, SnapshotPort snapshots, ContentCatalog catalog, ClassId defaultClassId) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(world);
        Objects.requireNonNull(snapshots);
        Objects.requireNonNull(catalog);
        Objects.requireNonNull(defaultClassId);
        TargetingService targeting = McTargetingModule.createServer(server);
        var actorSync = new ActorSyncSystem(new com.pgalaxyp.fragmento.combat.actorModule.minecraft.McActorSnapshotPort(server, defaultClassId));
        var pipeline = getFlowPipeline(catalog, targeting, actorSync);
        var intents = new ServerIntentQueue(new IntentQueue());
        var engine = new GameEngine(intents, pipeline, world, snapshots, GameState.empty(new FrameContext(0, 0)));

        return new ServerRuntime(engine, intents);
    }

    private static FlowPipeline getFlowPipeline(ContentCatalog catalog, TargetingService targeting, ActorSyncSystem actorSync) {
        var abilities = new AbilityFrameSystem(catalog.abilities(), catalog.abilityRules(), catalog.primaryBindings());
        var triggers = new com.pgalaxyp.fragmento.combat.effectModule.system.AbilityEffectTriggerSystem(catalog.abilityEffects(), targeting);
        var effects = new EffectSystem(new CatalogEffectService(catalog.effects()));
        var effectToDamage = new EffectAppliedDamageBridgeSystem();
        var damage = new DamageExecution(new DamageEngine(new DefaultDamageService(), new DefaultSnapshotProvider()));
        return new FlowPipeline(List.of(actorSync, abilities, triggers, effects, effectToDamage, damage, new DamageHealthBridgeSystem(), new ActorCommitSystem()));
    }

    private McCombatServerBootstrap() {}
}