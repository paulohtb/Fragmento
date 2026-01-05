package com.pgalaxyp.fragmento.rpg.gameplay.effects;

import com.pgalaxyp.fragmento.rpg.core.id.IdGen;
import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.Updatable;
import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.BasicSequence;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.missile.MagicMissileDef;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.missile.MagicMissileEffect;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.missile.MissileGuidance;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.missile.MissileLifetime;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.TargetRaycastService;
import com.pgalaxyp.fragmento.rpg.gameplay.time.TimeDrivenMover;
import com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnQuery;
import com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnResolverService;
import com.pgalaxyp.fragmento.rpg.platform.api.player.PlayerView;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

public final class EffectSpawnSystem implements Updatable {

    private final PlayerView player;
    private final ActorRepository actors;
    private final BasicSequence sequence;
    private final TargetRaycastService targeting;
    private final SpawnResolverService spawns;
    private final EffectSystem effects;
    private final MagicMissileDef missileDef;
    private final IdGen ids;
    private final Random rng;

    private final Queue<SpawnEffectRequest> queue = new ArrayDeque<>();

    private final TimeDrivenMover mover = new TimeDrivenMover();
    private final MissileGuidance guidance = new MissileGuidance();
    private final MissileLifetime lifetime = new MissileLifetime();

    public EffectSpawnSystem(
            PlayerView player,
            ActorRepository actors,
            BasicSequence sequence,
            TargetRaycastService targeting,
            SpawnResolverService spawns,
            EffectSystem effects,
            MagicMissileDef missileDef,
            IdGen ids,
            Random rng,
            TickBus bus
    ) {
        this.player = Objects.requireNonNull(player);
        this.actors = Objects.requireNonNull(actors);
        this.sequence = Objects.requireNonNull(sequence);
        this.targeting = Objects.requireNonNull(targeting);
        this.spawns = Objects.requireNonNull(spawns);
        this.effects = Objects.requireNonNull(effects);
        this.missileDef = Objects.requireNonNull(missileDef);
        this.ids = Objects.requireNonNull(ids);
        this.rng = Objects.requireNonNull(rng);

        bus.subscribe(SpawnEffectRequest.class, queue::add);
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
        while (!queue.isEmpty()) {
            var req = queue.poll();
            spawn(req, tick, bus);
        }
    }

    private void spawn(SpawnEffectRequest req, GameTick tick, TickBus bus) {
        var step = sequence.steps().stream()
                .filter(s -> Objects.equals(s.stepId(), req.stepId()))
                .findFirst()
                .orElse(null);

        EffectEnded effectEnded = new EffectEnded(req.actorId(), req.stepId());

        if (step == null) {
            bus.publish(effectEnded);
            return;
        }

        var combo = actors.combo(req.actorId());
        var target = targeting.resolve(player);

        var spawn = spawns.resolve(new SpawnQuery(
                req.actorId(),
                target,
                step.spawnRule(),
                combo.lastSpawnSide(),
                5.0
        ));

        if (!spawn.success()) {
            bus.publish(effectEnded);
            return;
        }

        combo.setLastSpawnSide(spawn.usedSide());

        var effect = new MagicMissileEffect(
                new EffectId(ids.next()),
                req.actorId(),
                req.stepId(),
                missileDef,
                target,
                spawn.position(),
                mover,
                guidance,
                lifetime,
                rng
        );

        effects.add(effect, tick, bus);
    }
}