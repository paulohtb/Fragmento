package com.pgalaxyp.fragmento.system.entity.tick;

import com.pgalaxyp.fragmento.system.entity.component.AreaEffectComponent;
import com.pgalaxyp.fragmento.system.spatial.SpatialQueryService;
import com.pgalaxyp.fragmento.system.temporal.DegradationPolicy;
import com.pgalaxyp.fragmento.system.temporal.TickBudgetService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class AreaEffectSystem {

    private final SpatialQueryService spatial;
    private final TickBudgetService budget;
    private final DegradationPolicy degradation;

    public AreaEffectSystem(
            SpatialQueryService spatial,
            TickBudgetService budget,
            DegradationPolicy degradation
    ) {
        this.spatial = spatial;
        this.budget = budget;
        this.degradation = degradation;
    }

    public void execute(
            ServerLevel level,
            AreaEffectComponent component,
            Predicate<LivingEntity> filter,
            BiConsumer<LivingEntity, ServerLevel> effect
    ) {
        if (!budget.tryBegin(TickBudgetService.Phase.AREA)) {
            return;
        }

        try {
            if (!degradation.allowHeavyStep(budget.remaining())) {
                return;
            }

            List<LivingEntity> targets =
                    spatial.queryLiving(
                            level,
                            component.area(),
                            filter,
                            component.maxTargets()
                    );

            for (LivingEntity e : targets) {
                effect.accept(e, level);
            }
        } finally {
            budget.end(TickBudgetService.Phase.AREA);
        }
    }
}