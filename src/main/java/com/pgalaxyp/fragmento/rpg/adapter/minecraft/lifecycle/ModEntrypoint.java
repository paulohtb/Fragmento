package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.bootstrap.RpgNetwork;
import com.pgalaxyp.fragmento.rpg.adapter.minecraft.context.ActorContextServer;
import com.pgalaxyp.fragmento.rpg.core.boot.GameBootstrap;
import com.pgalaxyp.fragmento.rpg.core.boot.RpgWiring;
import com.pgalaxyp.fragmento.rpg.core.id.IdGen;
import com.pgalaxyp.fragmento.rpg.core.loop.GameLoop;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboResolver;
import com.pgalaxyp.fragmento.rpg.gameplay.damage.DamageResolver;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectSpawnSystem;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectSystem;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.ComboEffectObserver;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.missile.MagicMissileDef;
import com.pgalaxyp.fragmento.rpg.gameplay.input.CombatInputBinding;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputRouter;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.TargetRaycastService;
import com.pgalaxyp.fragmento.rpg.gameplay.weapon.WeaponRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnResolverService;
import com.pgalaxyp.fragmento.rpg.platform.api.visual.VisualWorld;
import java.util.Optional;
import java.util.Random;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public final class ModEntrypoint {

    public ModEntrypoint(IEventBus modBus) {

        var bus = new TickBus();

        var actorIdGen = new IdGen(0L);
        var effectIdGen = new IdGen(10_000L);

        var actorIds = new ActorIds(actorIdGen);
        var actorContext = new ActorContextServer(actorIds);

        var actors = new ActorRepository();
        var weapons = new WeaponRepository();

        var visuals = Optional.<VisualWorld>of(new MinecraftVisualWorld());

        var inputRouter = new InputRouter(CombatInputBinding.defaultBinding());

        var sequence = DemoContent.fluteSequence();
        var missileDef = new MagicMissileDef(3.0, 0.5, 0.75);

        var combo = new ComboResolver(sequence, actors, bus);
        new ComboEffectObserver(sequence, bus);

        var effectsRepo = new EffectRepository();
        var effects = new EffectSystem(effectsRepo, visuals, actors);

        var targeting = new TargetRaycastService(actors);
        var spawns = new SpawnResolverService(new Random());

        var effectSpawn = new EffectSpawnSystem(
                actors,
                weapons,
                sequence,
                targeting,
                spawns,
                effects,
                missileDef,
                effectIdGen,
                new Random(),
                bus
        );

        var damage = new DamageResolver(actors, bus);
        new DamageBridge(actorContext, actorIds, bus);

        var snapshotBridge = new SnapshotBridge(actorContext, actors, effectsRepo, bus);

        RpgNetwork.register(modBus, bus);

        var wiring = new RpgWiring(
                inputRouter,
                combo,
                effectSpawn,
                effects,
                damage
        );

        var bootstrap = new GameBootstrap(System::nanoTime, wiring, bus);
        GameLoop loop = bootstrap.build();

        NeoForge.EVENT_BUS.register(new GameLoopBridge(loop));
        NeoForge.EVENT_BUS.register(new PlayerSyncBridge(actorIds, actors, weapons));
        NeoForge.EVENT_BUS.register(new LivingSyncBridge(actorIds, actors));
        NeoForge.EVENT_BUS.register(snapshotBridge);
    }
}