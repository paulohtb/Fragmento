package com.pgalaxyp.fragmento.system.entity.controller;

import com.pgalaxyp.fragmento.content.bard.entity.BardSpiritImpactService;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SpiritImpactController {

    private static final double HIT_RADIUS = 0.45;
    private static final double HIT_RADIUS_SQR = HIT_RADIUS * HIT_RADIUS;

    private boolean impacted;

    public void reset() {
        impacted = false;
    }

    public void tick(
            ServerLevel level,
            NewwSpiritEntityBase entity,
            SpiritBehavior behavior,
            SpiritContext ctx
    ) {
        if (impacted) return;

        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) return;

        Vec3 center = target.getBoundingBox().getCenter();
        double d2 = entity.position().distanceToSqr(center);

        if (d2 > HIT_RADIUS_SQR) return;

        impacted = true;

        ImpactResult impact =
                ImpactResult.entity(target, entity.position());

        behavior.onImpact(ctx, impact);

        BardSpiritImpactService.handle(
                entity,
                ctx,
                target
        );
    }
}