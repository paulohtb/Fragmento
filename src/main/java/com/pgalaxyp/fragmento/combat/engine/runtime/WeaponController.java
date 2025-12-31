package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.input.WeaponInput;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.engine.time.TickSource;
import com.pgalaxyp.fragmento.combat.rule.runtime.WeaponCombatRuntime;

public final class WeaponController {

    private final WeaponCombatRuntime runtime;
    private final TickSource tickSource;

    public WeaponController(
            WeaponCombatRuntime runtime,
            TickSource tickSource
    ) {
        this.runtime = runtime;
        this.tickSource = tickSource;
    }

    public void tick() {
        runtime.tick(CombatTime.ofTicks(tickSource.gameTick()));
    }

    public void onPrimaryAttack() {
        runtime.onInput(
                WeaponInput.primaryAttack(),
                CombatTime.ofTicks(tickSource.gameTick())
        );
    }

    public void onSkillPress(int skillId) {
        runtime.onInput(
                WeaponInput.skillPress(skillId),
                CombatTime.ofTicks(tickSource.gameTick())
        );
    }

    public void onSkillCancel(int skillId) {
        runtime.onInput(
                WeaponInput.skillCancel(skillId),
                CombatTime.ofTicks(tickSource.gameTick())
        );
    }

    public void interruptAll() {
        runtime.interruptAll(
                CombatTime.ofTicks(tickSource.gameTick())
        );
    }
}