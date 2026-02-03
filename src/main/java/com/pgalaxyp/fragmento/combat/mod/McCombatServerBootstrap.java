package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.*;
import com.pgalaxyp.fragmento.combat.flowModule.system.*;
import com.pgalaxyp.fragmento.combat.engineModule.port.*;
import com.pgalaxyp.fragmento.combat.actorModule.system.*;
import com.pgalaxyp.fragmento.combat.effectModule.system.*;
import com.pgalaxyp.fragmento.combat.damageModule.system.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingService;
import com.pgalaxyp.fragmento.combat.abilityModule.system.AbilityFrameSystem;
import com.pgalaxyp.fragmento.combat.targetingModule.minecraft.McTargetingModule;
import java.util.*;
import net.minecraft.server.MinecraftServer;

public final class McCombatServerBootstrap {
    public record ServerRuntime(GameEngine engine, IntentSinkPort intents) {}

    public static ServerRuntime create(MinecraftServer server, WorldCommandPort world, SnapshotPort snapshots, ContentCatalog catalog, ClassId defaultClassId) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(world);
        Objects.requireNonNull(snapshots);
        Objects.requireNonNull(catalog);
        Objects.requireNonNull(defaultClassId);
        TargetingService targeting = McTargetingModule.createServer(server);
        var actorSync = new ActorSyncSystem(new com.pgalaxyp.fragmento.combat.actorModule.minecraft.McActorSnapshotPort(server, defaultClassId));
        FlowPipeline pipeline = flow(catalog, targeting, actorSync);
        var intents = new ServerIntentQueue(new IntentQueue());
        var engine = new GameEngine(intents, pipeline, world, snapshots, GameState.empty(new FrameContext(0L, 0)));
        return new ServerRuntime(engine, intents);
    }

    private static FlowPipeline flow(ContentCatalog catalog, TargetingService targeting, ActorSyncSystem actorSync) {
        var intentToAbility = new PrimaryActionToAbilityRequestSystem();
        var abilities = new AbilityFrameSystem(catalog.abilities(), catalog.abilityRules(), catalog.primaryBindings(), catalog.abilityTuning());
        var triggers = new AbilityToEffectTriggerSystem(catalog.abilityTriggers(), targeting);
        var effects = new EffectFrameSystem(EffectModule.create(catalog.effects().keySet()));
        var effectToDamage = new EffectToDamageRequestSystem(catalog.effects());
        var damage = new DamageFrameSystem(DamageModule.createDefault());
        var damageToHealth = new DamageToHealthBridgeSystem();
        var commit = new ActorCommitSystem();
        return new FlowPipeline(List.of(actorSync, intentToAbility, abilities, triggers, effects, effectToDamage, damage, damageToHealth, commit));
    }

    private McCombatServerBootstrap() {}
}