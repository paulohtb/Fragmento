package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.engineModule.api.*;
import com.pgalaxyp.fragmento.combat.intentModule.api.*;
import com.pgalaxyp.fragmento.combat.flowModule.system.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.flowModule.api.FlowPipeline;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.actorModule.system.ActorModule;
import com.pgalaxyp.fragmento.combat.actorModule.port.ActorHealthPort;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.damageModule.system.DamageModule;
import com.pgalaxyp.fragmento.combat.abilityModule.system.AbilityModule;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingService;
import com.pgalaxyp.fragmento.combat.actorModule.minecraft.McActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.targetingModule.minecraft.McTargetingModule;
import java.util.*;
import net.minecraft.server.MinecraftServer;

public final class McCombatServerBootstrap {
    public record ServerRuntime(GameEngine engine, IntentSinkPort intents) {}

    public static ServerRuntime create(MinecraftServer server, WorldCommandPort world, ContentCatalog catalog, ClassId defaultClassId) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(world);
        Objects.requireNonNull(catalog);
        Objects.requireNonNull(defaultClassId);
        TargetingService targeting = McTargetingModule.createServer(server);
        var actorSync = new ActorSnapshotSystem(new McActorSnapshotPort(server, defaultClassId));
        DamagePort damage = DamageModule.createDefault();
        ActorHealthPort health = ActorModule.createHealthPort();
        var abilities = AbilityModule.create(catalog.abilities());
        FlowPipeline pipeline = new FlowPipeline(List.of(actorSync, new PrimaryActionSystem(catalog.classKits(), abilities), new AbilityToDamageSystem(catalog.abilityTriggers(), targeting, damage), new DamageToActorHealthSystem(health)));
        var intents = new ServerIntentQueue();
        var engine = new GameEngine(intents, pipeline, world, GameState.empty(new FrameContext(0L, 0)));
        return new ServerRuntime(engine, intents);
    }

    private McCombatServerBootstrap() {}
}