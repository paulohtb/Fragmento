package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.behavior.TimedSpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import com.pgalaxyp.fragmento.system.entity.movement.SpiritMovementPatterns;
import net.minecraft.world.entity.LivingEntity;

public final class FluteBasicBehavior extends TimedSpiritBehavior<FluteBasicBehavior.Phase> {

    enum Phase { SPAWN, TRAVEL, DESPAWN }

    private static final int SPAWN_TICKS = 8;
    private static final int TRAVEL_TICKS = 10;
    private static final int DESPAWN_TICKS = 8;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        startPhase(Phase.SPAWN, SPAWN_TICKS);
    }

    @Override
    protected void tickInternal(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        Phase p = phase();

        if (p == Phase.SPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.SPAWN);
            if (time() >= duration()) {
                startPhase(Phase.TRAVEL, TRAVEL_TICKS);
            }
            return;
        }

        if (p == Phase.TRAVEL) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            LivingEntity target = ctx.target;
            if (!SpiritMovementPatterns.chaseLivingTarget(
                    ctx,
                    movement,
                    look,
                    target,
                    time(),
                    duration(),
                    0.0,
                    false
            )) {
                startPhase(Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }

            if (time() >= duration()) {
                startPhase(Phase.DESPAWN, DESPAWN_TICKS);
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
        startPhase(Phase.DESPAWN, DESPAWN_TICKS);
    }

    @Override
    protected void onTickPhase(Phase phase, int time, int duration) {
    }

    @Override
    protected void onEnterPhase(Phase phase) {
    }
}