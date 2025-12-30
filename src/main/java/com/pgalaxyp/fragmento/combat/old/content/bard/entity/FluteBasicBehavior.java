package com.pgalaxyp.fragmento.combat.old.content.bard.entity;

import com.pgalaxyp.fragmento.combat.old.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.combat.old.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.combat.old.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.combat.old.system.entity.behavior.TimedSpiritBehavior;
import com.pgalaxyp.fragmento.combat.old.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.combat.old.system.entity.movement.MovementPlan;
import net.minecraft.world.entity.LivingEntity;

public final class FluteBasicBehavior extends TimedSpiritBehavior<FluteBasicBehavior.Phase> {

    enum Phase { SPAWN, TRAVEL, DESPAWN }

    private static final int SPAWN_TICKS = 8;
    private static final int TRAVEL_TICKS = 10;
    private static final int DESPAWN_TICKS = 8;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        startPhase(ctx, Phase.SPAWN, SPAWN_TICKS);
    }

    @Override
    protected void tickInternal(SpiritContext ctx, MovementPlan movement, LookPlan look) {
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
            if (!com.pgalaxyp.fragmento.combat.old.system.entity.movement.SpiritMovementPatterns.chaseLivingTarget(
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
        startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
    }

    @Override
    protected void onTickPhase(SpiritContext ctx, Phase phase, int time, int duration) {
    }

    @Override
    protected void onEnterPhase(SpiritContext ctx, Phase phase, int duration) {
    }
}