package com.pgalaxyp.fragmento.combat.rule.combo;

import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.hit.HitResult;

public final class ComboRule {

    public int nextComboIndex(
            ComboDefinition combo,
            int currentIndex,
            HitResult hitResult
    ) {
        if (combo == null || hitResult == null) {
            return 0;
        }

        if (!hitResult.hit() || !hitResult.damageApplied()) {
            return 0;
        }

        int next = currentIndex + 1;
        if (next >= combo.size()) {
            return 0;
        }

        return next;
    }
}