package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.*;
import com.pgalaxyp.fragmento.combat.flowModule.system.*;
import com.pgalaxyp.fragmento.combat.engineModule.port.*;
import com.pgalaxyp.fragmento.combat.actorModule.system.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorHealthPort;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.damageModule.system.DamageModule;
import com.pgalaxyp.fragmento.combat.abilityModule.system.AbilityModule;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingService;
import com.pgalaxyp.fragmento.combat.actorModule.minecraft.McActorSnapshotPort;
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
        var actorSync = new ActorSyncSystem(new McActorSnapshotPort(server, defaultClassId));
        DamagePort damage = DamageModule.createDefault();
        ActorHealthPort health = ActorModule.createHealthPort();
        FlowPipeline pipeline = flow(catalog, targeting, actorSync, damage, health);
        var intents = new ServerIntentQueue();
        var engine = new GameEngine(intents, pipeline, world, snapshots, GameState.empty(new FrameContext(0L, 0)));
        return new ServerRuntime(engine, intents);
    }

    private static FlowPipeline flow(ContentCatalog catalog, TargetingService targeting, ActorSyncSystem actorSync, DamagePort damage, ActorHealthPort health) {
        var abilityPort = AbilityModule.create(catalog.abilities(), catalog.abilityRules(), catalog.primaryBindings(), catalog.abilityTuning());
        return new FlowPipeline(List.of(actorSync, new PrimaryActionAbilitySystem(abilityPort), new AbilityToDamageSystem(catalog.abilityTriggers(), targeting, damage), new DamageToActorHealthSystem(health)));
    }

    private McCombatServerBootstrap() {}
}