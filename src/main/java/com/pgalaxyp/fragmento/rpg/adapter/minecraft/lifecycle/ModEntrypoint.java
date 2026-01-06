package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import net.neoforged.bus.api.IEventBus;

public final class ModEntrypoint {

    public ModEntrypoint(IEventBus modBus) {

        var bus = new com.pgalaxyp.fragmento.rpg.core.loop.TickBus();

        var actorIds = new ActorIds(new com.pgalaxyp.fragmento.rpg.core.id.IdGen(0L));
        ServerInputIntentHandler.init(actorIds);

        var effectIds = new com.pgalaxyp.fragmento.rpg.core.id.IdGen(10_000L);

        var actors = new com.pgalaxyp.fragmento.rpg.gameplay.state.ActorRepository();
        var weapons = new com.pgalaxyp.fragmento.rpg.gameplay.state.WeaponRepository();

        WeaponBootstrap.register(WeaponContent.flute(), weapons);

        var visuals = java.util.Optional.<com.pgalaxyp.fragmento.rpg.platform.api.visual.VisualWorld>of(new MinecraftVisualWorld());

        var sequence = com.pgalaxyp.fragmento.rpg.content.sequences.DemoContent.fluteSequence();
        var missileDef = new com.pgalaxyp.fragmento.rpg.gameplay.effects.missile.MagicMissileDef(3.0, 0.5, 0.75);

        var world = new MinecraftWorldView(actorIds);
        var targeting = new com.pgalaxyp.fragmento.rpg.gameplay.targeting.TargetRaycastService(actors, world);
        var spawns = new com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnResolverService(new java.util.Random(), world);

        var combo = new com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboResolver(sequence, actors, weapons, bus);
        new com.pgalaxyp.fragmento.rpg.gameplay.effects.ComboEffectObserver(sequence, bus);

        var effectsRepo = new com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectRepository();
        var effects = new com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectSystem(effectsRepo, world, visuals, actors);

        var effectSpawn = new com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectSpawnSystem(
                actors,
                sequence,
                targeting,
                spawns,
                effects,
                missileDef,
                effectIds,
                new java.util.Random(),
                bus
        );

        var damage = new com.pgalaxyp.fragmento.rpg.gameplay.damage.DamageResolver(actors, bus);
        new DamageBridge(actorIds, bus);

        RpgNetwork.register(modBus, bus);

        var wiring = new com.pgalaxyp.fragmento.rpg.core.boot.RpgWiring(
                combo,
                effectSpawn,
                effects,
                damage
        );

        var loop = new com.pgalaxyp.fragmento.rpg.core.boot.GameBootstrap(System::nanoTime, wiring, bus).build();

        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new GameLoopBridge(loop));
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new PlayerSyncBridge(actorIds, actors, weapons));
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new LivingSyncBridge(actorIds, actors));
    }
}