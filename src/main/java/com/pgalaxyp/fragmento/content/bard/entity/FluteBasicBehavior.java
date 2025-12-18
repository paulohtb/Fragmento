package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.behavior.TimedSpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class FluteBasicBehavior extends TimedSpiritBehavior<FluteBasicBehavior.Phase> {

    private static final int SPAWN_TICKS = 8;
    private static final int TRAVEL_TICKS = 10;
    private static final int DESPAWN_TICKS = 8;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        startPhase(Phase.SPAWN, SPAWN_TICKS);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
    }

    @Override
    protected void tickInternal(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        if (phase() == Phase.SPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.SPAWN);
            if (time() >= duration()) {
                startPhase(Phase.TRAVEL, TRAVEL_TICKS);
            }
            return;
        }

        if (phase() == Phase.TRAVEL) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            LivingEntity target = ctx.target;
            if (target == null || !target.isAlive()) {
                startPhase(Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }

            Vec3 targetPos = target.getBoundingBox().getCenter();
            Vec3 toTarget = targetPos.subtract(ctx.pos);

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = targetPos;

            movement.kind = MovementPlan.Kind.VELOCITY;

            double progress = (double) time() / (double) duration();
            double speedMultiplier = progress >= 0.5 ? 1.75 : 1.0;

            movement.desiredVelocity = toTarget.normalize().scale(speedMultiplier);

            if (time() >= duration()) {
                startPhase(Phase.DESPAWN, DESPAWN_TICKS);
            }
            return;
        }

        if (phase() == Phase.DESPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.DESPAWN);
            if (time() >= duration()) {
                ctx.self.requestDespawn();
            }
        }
    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
        startPhase(Phase.DESPAWN, DESPAWN_TICKS);
    }

    @Override
    protected void onTickPhase(Phase phase, int time, int duration) {
    }

    enum Phase {
        SPAWN,
        TRAVEL,
        DESPAWN
    }
}