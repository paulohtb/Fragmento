package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.commandModule.api.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.flowModule.api.FlowPipeline;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameEngine;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamagePort;
import com.pgalaxyp.fragmento.combat.actorModule.system.ActorModule;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentCatalog;
import com.pgalaxyp.fragmento.combat.actionModule.system.ActionModule;
import com.pgalaxyp.fragmento.combat.damageModule.system.DamageModule;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.abilityModule.system.AbilityModule;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingService;
import com.pgalaxyp.fragmento.combat.flowModule.system.AbilityToDamageSystem;
import com.pgalaxyp.fragmento.combat.actorModule.minecraft.McActorSnapshotPort;
import com.pgalaxyp.fragmento.combat.targetingModule.minecraft.McTargetingModule;
import com.pgalaxyp.fragmento.combat.classModule.system.DefaultClassAssignmentSystem;
import net.minecraft.server.MinecraftServer;
import java.util.*;

public final class McCombatServerBootstrap {
    public record ServerRuntime(GameEngine engine, CommandSinkPort commands) {}

    public static ServerRuntime create(MinecraftServer server, WorldCommandPort world, ContentCatalog catalog, ClassId defaultClassId) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(world);
        Objects.requireNonNull(catalog);
        Objects.requireNonNull(defaultClassId);
        TargetingService targeting = McTargetingModule.createServer(server);
        DamagePort damage = DamageModule.createDefault();
        var actorSync = ActorModule.createSnapshotSystem(new McActorSnapshotPort(server));
        var classAssign = new DefaultClassAssignmentSystem(defaultClassId);
        var actions = ActionModule.createPrimaryActionSystem(catalog.classKits());
        var abilitySystems = AbilityModule.create(catalog.abilities());
        var pipeline = new FlowPipeline(List.of(actorSync, classAssign, actions, abilitySystems.commands(), abilitySystems.runtime(), new AbilityToDamageSystem(catalog.abilityTriggers(), targeting, damage)));
        var commands = new ServerCommandQueue();
        var engine = new GameEngine(commands, pipeline, world, new FrameContext(0L, 0).frameId());
        return new ServerRuntime(engine, commands);
    }

    private McCombatServerBootstrap() {}
}