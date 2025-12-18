package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class FluteChargedBehavior implements SpiritBehavior {

    private static final int SPAWN_TICKS = 8;
    private static final int TRAVEL_TICKS = 10;
    private static final int ASCENT_TICKS = 6;
    private static final int HOVER_TICKS = 12;
    private static final int DESPAWN_TICKS = 8;

    private Phase phase = Phase.SPAWN;
    private int phaseTime = 0;

    @Override
    public void tick(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        LivingEntity target = ctx.target;
        phaseTime++;

        if (target == null || !target.isAlive()) {
            ctx.self.requestDespawn();
            return;
        }

        if (phase == Phase.SPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.SPAWN);

            if (phaseTime >= SPAWN_TICKS) {
                startPhase(Phase.TRAVEL);
            }
            return;
        }

        if (phase == Phase.TRAVEL) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            Vec3 targetPos = target.getBoundingBox().getCenter();
            Vec3 toTarget = targetPos.subtract(ctx.pos);

            movement.kind = MovementPlan.Kind.VELOCITY;
            movement.desiredVelocity = toTarget.normalize().scale(0.6);

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = targetPos;

            if (phaseTime >= TRAVEL_TICKS) {
                startPhase(Phase.ASCENT);
            }
            return;
        }

        if (phase == Phase.ASCENT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            Vec3 ascentPos = target.getBoundingBox().getCenter().add(0.0, 2.0, 0.0);
            Vec3 delta = ascentPos.subtract(ctx.pos);

            movement.kind = MovementPlan.Kind.VELOCITY;
            movement.desiredVelocity = delta.scale(0.25);

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = target.getBoundingBox().getCenter();

            if (phaseTime >= ASCENT_TICKS) {
                startPhase(Phase.HOVER);
            }
            return;
        }

        if (phase == Phase.HOVER) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            movement.kind = MovementPlan.Kind.NONE;

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = target.getBoundingBox().getCenter();

            if (phaseTime >= HOVER_TICKS) {
                startPhase(Phase.DESPAWN);
            }
            return;
        }

        if (phase == Phase.DESPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.DESPAWN);

            if (phaseTime >= DESPAWN_TICKS) {
                ctx.self.requestDespawn();
            }
        }
    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
        if (phase == Phase.TRAVEL) {
            startPhase(Phase.ASCENT);
        }
    }

    private void startPhase(Phase next) {
        phase = next;
        phaseTime = 0;
    }

    enum Phase {
        SPAWN,
        TRAVEL,
        ASCENT,
        HOVER,
        DESPAWN
    }
}