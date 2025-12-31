package com.pgalaxyp.fragmento.combat.rule.combo;

import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.hit.HitResult;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;

public final class ComboApplier {

    private final ComboRule rule;

    public ComboApplier(ComboRule rule) {
        this.rule = rule;
    }

    public void applyHit(
            ServerCombatState state,
            ComboDefinition combo,
            HitResult hitResult
    ) {
        if (state == null || combo == null || hitResult == null) {
            return;
        }

        int current = state.weapon().comboIndex();
        int next = rule.nextComboIndex(combo, current, hitResult);

        state.weapon().setComboIndex(next);
        state.bumpVersion();
    }
}