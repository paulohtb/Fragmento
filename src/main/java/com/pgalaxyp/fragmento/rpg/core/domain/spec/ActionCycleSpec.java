package com.pgalaxyp.fragmento.rpg.core.domain.spec;

import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingSpec;

public record ActionCycleSpec(
        ComboSpec combo,
        TargetingSpec targeting,
        StepWindowSpec stepWindow
) {
    public ActionCycleSpec {
        if (combo == null || targeting == null || stepWindow == null) {
            throw new IllegalArgumentException();
        }
    }
}