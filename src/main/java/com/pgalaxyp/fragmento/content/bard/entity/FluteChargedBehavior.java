package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.behavior.TimedSpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import net.minecraft.world.entity.LivingEntity;

public final class FluteChargedBehavior extends TimedSpiritBehavior<FluteChargedBehavior.Phase> {

    enum Phase { SPAWN, TRAVEL, ASCENT, HOVER, DESPAWN }

    private static final int SPAWN_TICKS = 8;
    private static final int TRAVEL_TICKS = 10;
    private static final int ASCENT_TICKS = 6;
    private static final int HOVER_TICKS = 12;
    private static final int DESPAWN_TICKS = 8;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        startPhase(ctx, Phase.SPAWN, SPAWN_TICKS);
    }

    @Override
    public void tickInternal(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        Phase p = phase();

        if (p == Phase.SPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.SPAWN);
            if (time() >= duration()) {
                startPhase(ctx, Phase.TRAVEL, TRAVEL_TICKS);
            }
            return;
        }

        if (p == Phase.TRAVEL) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            LivingEntity target = ctx.target;
            if (!com.pgalaxyp.fragmento.system.entity.movement.SpiritMovementPatterns.chaseLivingTarget(
                    ctx,
                    movement,
                    look,
                    target,
                    time(),
                    duration(),
                    0.0,
                    false
            )) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }

            if (time() >= duration()) {
                startPhase(ctx, Phase.ASCENT, ASCENT_TICKS);
            }
            return;
        }

        if (p == Phase.ASCENT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            LivingEntity target = ctx.target;
            if (!com.pgalaxyp.fragmento.system.entity.movement.SpiritMovementPatterns.chaseLivingTarget(
                    ctx,
                    movement,
                    look,
                    target,
                    time(),
                    duration(),
                    2.0,
                    true
            )) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }

            if (time() >= duration()) {
                startPhase(ctx, Phase.HOVER, HOVER_TICKS);
            }
            return;
        }

        if (p == Phase.HOVER) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            LivingEntity target = ctx.target;
            if (target == null || !target.isAlive()) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }

            movement.kind = MovementPlan.Kind.NONE;

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = target.getBoundingBox().getCenter();

            if (time() >= duration()) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            }
            return;
        }

        ctx.self.setAnimKey(BardAnimKeys.DESPAWN);
        if (time() >= duration()) {
            ctx.self.requestDespawn();
        }
    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
        if (phase() == Phase.TRAVEL) {
            startPhase(ctx, Phase.ASCENT, ASCENT_TICKS);
        }
    }

    @Override
    protected void onTickPhase(SpiritContext ctx, Phase phase, int time, int duration) {
    }

    @Override
    protected void onEnterPhase(SpiritContext ctx, Phase phase, int duration) {
    }
}