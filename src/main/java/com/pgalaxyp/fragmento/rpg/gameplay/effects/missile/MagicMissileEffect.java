package com.pgalaxyp.fragmento.rpg.gameplay.effects.missile;

import com.pgalaxyp.fragmento.rpg.gameplay.damage.DamageRequest;
import com.pgalaxyp.fragmento.rpg.gameplay.damage.DamageType;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.Effect;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectEnded;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectId;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectWorld;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.Target;
import com.pgalaxyp.fragmento.rpg.gameplay.time.TimeDrivenMover;
import java.util.Objects;
import java.util.Random;

public final class MagicMissileEffect implements Effect {
    private final EffectId id;
    private final long sourceActorId;
    private final String stepId;
    private final MagicMissileDef def;
    private final Target target;
    private final Vec3 origin;

    private final TimeDrivenMover mover;
    private final MissileGuidance guidance;
    private final MissileLifetime lifetime;

    private final double totalLifetimeSeconds;

    private double elapsedSeconds;
    private Vec3 position;
    private Vec3 lastAimPoint;

    public MagicMissileEffect(
            EffectId id,
            long sourceActorId,
            String stepId,
            MagicMissileDef def,
            Target target,
            Vec3 origin,
            TimeDrivenMover mover,
            MissileGuidance guidance,
            MissileLifetime lifetime,
            Random rng
    ) {
        this.id = Objects.requireNonNull(id);
        this.sourceActorId = sourceActorId;
        this.stepId = Objects.requireNonNull(stepId);
        this.def = Objects.requireNonNull(def);
        this.target = Objects.requireNonNull(target);
        this.origin = Objects.requireNonNull(origin);
        this.mover = Objects.requireNonNull(mover);
        this.guidance = Objects.requireNonNull(guidance);
        this.lifetime = Objects.requireNonNull(lifetime);

        var t = def.minLifetimeSeconds() + (def.maxLifetimeSeconds() - def.minLifetimeSeconds()) * Objects.requireNonNull(rng).nextDouble();
        this.totalLifetimeSeconds = Math.max(def.minLifetimeSeconds(), Math.min(def.maxLifetimeSeconds(), t));
        this.elapsedSeconds = 0.0;
        this.position = origin;
        this.lastAimPoint = target.position();
    }

    @Override
    public EffectId id() {
        return id;
    }

    @Override
    public void start(EffectWorld world) {
        lastAimPoint = guidance.aimPoint(target, world.actors());
        world.visuals().ifPresent(v -> v.debugPoint(origin));
    }

    @Override
    public boolean update(EffectWorld world) {
        var dt = world.tick().deltaSeconds();
        elapsedSeconds += dt;

        var aim = guidance.aimPoint(target, world.actors());
        lastAimPoint = aim;

        var p = mover.progress(elapsedSeconds, totalLifetimeSeconds);
        position = mover.position(origin, aim, p);

        world.visuals().ifPresent(v -> {
            v.debugPoint(position);
            v.debugLine(origin, aim);
        });

        if (lifetime.shouldEnd(elapsedSeconds, totalLifetimeSeconds, position, target, aim)) {
            if (target.isReal()) {
                var targetId = target.actorIdOrZero();
                if (targetId != 0L) {
                    world.bus().publish(new DamageRequest(sourceActorId, targetId, def.damage(), DamageType.MAGIC));
                }
            }
            return false;
        }

        return true;
    }

    @Override
    public void end(EffectWorld world) {
        world.bus().publish(new EffectEnded(sourceActorId, stepId));
    }
}