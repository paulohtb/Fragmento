package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class FluteChargedBehavior implements SpiritBehavior {

    private static final int TRAVEL_TICKS = 16;
    private static final int HOVER_TICKS = 60;

    private int phase;

    @Override
    public void tick(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        LivingEntity target = ctx.target;
        if (target == null || !target.isAlive()) {
            ctx.self.requestDespawn();
            return;
        }

        if (phase == 0) {
            Vec3 desired = target.getBoundingBox().getCenter();
            Vec3 delta = desired.subtract(ctx.pos);

            movement.kind = MovementPlan.Kind.VELOCITY;
            movement.desiredVelocity = delta.scale(0.3);

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = desired;

            if (ctx.lifetimeTicks >= TRAVEL_TICKS) {
                phase = 1;
            }
            return;
        }

        if (phase == 1) {
            Vec3 hoverPos = target.getBoundingBox().getCenter().add(0.0, 2.2, 0.0);
            Vec3 delta = hoverPos.subtract(ctx.pos);

            movement.kind = MovementPlan.Kind.VELOCITY;
            movement.desiredVelocity = delta.scale(0.08);

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = target.getBoundingBox().getCenter();

            if (ctx.lifetimeTicks >= TRAVEL_TICKS + HOVER_TICKS) {
                ctx.self.requestDespawn();
            }
        }
    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
    }
}