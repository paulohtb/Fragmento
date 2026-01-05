package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.boot.GameBootstrap;
import com.pgalaxyp.fragmento.rpg.core.boot.RpgWiring;
import com.pgalaxyp.fragmento.rpg.core.id.IdGen;
import com.pgalaxyp.fragmento.rpg.core.loop.GameLoop;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboResolver;
import com.pgalaxyp.fragmento.rpg.gameplay.damage.DamageResolver;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.*;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.missile.MagicMissileDef;
import com.pgalaxyp.fragmento.rpg.gameplay.input.CombatInputBinding;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputRouter;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.TargetRaycastService;
import com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnResolverService;
import com.pgalaxyp.fragmento.rpg.platform.api.visual.VisualWorld;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Optional;
import java.util.Random;

public final class ModEntrypoint {

    public ModEntrypoint(IEventBus modBus) {

        long actorId = 1L;

        var bus = new TickBus();
        var ids = new IdGen(0L);
        var actors = new ActorRepository();

        var world = new MinecraftWorldView();
        var visuals = Optional.<VisualWorld>of(new MinecraftVisualWorld());

        var player = new MinecraftPlayerView(actorId);
        var inputView = new MinecraftInputView();

        var sequence = DemoContent.fluteSequence();
        var missileDef = new MagicMissileDef(3.0, 0.5, 0.75);

        var inputRouter = new InputRouter(
                inputView,
                player,
                CombatInputBinding.defaultBinding()
        );

        var combo = new ComboResolver(sequence, actors, bus);

        new ComboEffectObserver(actorId, sequence, bus);

        var targeting = new TargetRaycastService(world, actors);
        var spawns = new SpawnResolverService(world, new Random());

        var effectsRepo = new EffectRepository();
        var effects = new EffectSystem(effectsRepo, world, visuals, actors);

        var effectSpawn = new EffectSpawnSystem(
                player,
                actors,
                sequence,
                targeting,
                spawns,
                effects,
                missileDef,
                ids,
                new Random(),
                bus
        );

        var damage = new DamageResolver(actors, bus);

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
        NeoForge.EVENT_BUS.register(new PlayerSyncBridge(player, actors));
    }
}