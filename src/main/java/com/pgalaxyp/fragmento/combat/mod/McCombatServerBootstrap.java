package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.actorModule.system.*;
import com.pgalaxyp.fragmento.combat.damageModule.system.*;
import com.pgalaxyp.fragmento.combat.abilityModule.system.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.random.*;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingService;
import com.pgalaxyp.fragmento.combat.damageModule.port.DefaultSnapshotProvider;
import com.pgalaxyp.fragmento.combat.actorModule.minecraft.McActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.targetingModule.minecraft.McTargetingModule;
import java.util.*;
import net.minecraft.server.MinecraftServer;

public final class McCombatServerBootstrap {
    public record ServerRuntime(GameEngine engine, ServerIntentQueue intents) { }

    public static ServerRuntime create(MinecraftServer server, WorldCommandPort world, SnapshotPort snapshots, ContentCatalog catalog, ClassId defaultClassId) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(world);
        Objects.requireNonNull(snapshots);
        Objects.requireNonNull(catalog);
        Objects.requireNonNull(defaultClassId);

        TargetingService targeting = McTargetingModule.createServer(server);
        var actorSync = new ActorSyncSystem(new McActorSnapshotPort(server, defaultClassId));
        var pipeline = getPipeline(catalog, targeting, actorSync);
        var intentQueue = new IntentQueue();
        var intents = new ServerIntentQueue(intentQueue);
        var engine = new GameEngine(intents, pipeline, world, snapshots, GameState.empty(new FrameContext(0L, 0)));

        return new ServerRuntime(engine, intents);
    }

    private static FlowPipeline getPipeline(ContentCatalog catalog, TargetingService targeting, ActorSyncSystem actorSync) {
        var abilities = new AbilityEngine(catalog.abilities(), targeting);
        var abilitySystem = new AbilityExecution(abilities, new AbilityResolver(catalog.abilityRules(), catalog.primaryBindings()));
        var abilityEffects = new AbilityStartEffectDispatch();
        var damageEngine = new DamageEngine(new DefaultDamageService(), new DefaultSnapshotProvider());
        var damageSystem = new DamageExecution(damageEngine);
        var damageFromEffects = new DamageFromEffectExecution(catalog.effects());
        var actorCommit = new ActorCommitSystem();

        return new FlowPipeline(List.of(actorSync, abilitySystem, abilityEffects, damageFromEffects, damageSystem, actorCommit, new AbilityView(abilities)));
    }

    private McCombatServerBootstrap() {}
}