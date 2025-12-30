package com.pgalaxyp.fragmento.combat.old.system.entity.behavior;

import com.pgalaxyp.fragmento.combat.old.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.combat.old.system.entity.movement.MovementPlan;

public interface SpiritBehavior {

    void tick(
            SpiritContext ctx,
            MovementPlan movement,
            LookPlan look
    );

    void onImpact(
            SpiritContext ctx,
            ImpactResult impact
    );

    default void onCasted(SpiritContext ctx) {
    }

    default void onCancelled(SpiritContext ctx) {
    }
}