package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class FluteSpecialBehavior implements SpiritBehavior {

    private static final int ORBIT_TICKS = 40;
    private static final double ORBIT_RADIUS = 2.0;

    @Override
    public void tick(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        LivingEntity target = ctx.target;
        if (target == null || !target.isAlive()) {
            ctx.self.requestDespawn();
            return;
        }

        double angle = ctx.lifetimeTicks * 0.25;
        Vec3 center = target.getBoundingBox().getCenter();

        Vec3 orbitPos = center.add(
                Math.cos(angle) * ORBIT_RADIUS,
                1.6,
                Math.sin(angle) * ORBIT_RADIUS
        );

        Vec3 delta = orbitPos.subtract(ctx.pos);

        movement.kind = MovementPlan.Kind.VELOCITY;
        movement.desiredVelocity = delta.scale(0.35);

        look.kind = LookPlan.Kind.TO_POS;
        look.lookAtPos = center;

        if (ctx.casted || ctx.lifetimeTicks >= ORBIT_TICKS) {
            ctx.self.requestDespawn();
        }
    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
    }
}