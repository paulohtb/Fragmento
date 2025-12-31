package com.pgalaxyp.fragmento.combat.engine.bard.flute;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.engine.runtime.WeaponController;
import com.pgalaxyp.fragmento.combat.engine.time.TickSource;
import com.pgalaxyp.fragmento.combat.rule.runtime.WeaponCombatRuntime;

public final class FluteWeaponController {

    private final WeaponController controller;
    private final WeaponCombatRuntime runtime;
    private final TickSource tickSource;

    public FluteWeaponController(
            WeaponCombatRuntime runtime,
            TickSource tickSource
    ) {
        this.runtime = runtime;
        this.tickSource = tickSource;
        this.controller = new WeaponController(runtime, tickSource);
    }

    public void tick() {
        controller.tick();
    }

    public void onPrimaryAttack() {
        controller.onPrimaryAttack();
    }

    public void onSkillPress(int skillId) {
        controller.onSkillPress(skillId);
    }

    public void onSkillCancel(int skillId) {
        controller.onSkillCancel(skillId);
    }

    public void onHitConfirmed(int actionLocalId) {
        runtime.onHitConfirmed(
                actionLocalId,
                CombatTime.ofTicks(tickSource.gameTick())
        );
    }

    public void interruptAll() {
        controller.interruptAll();
    }
}