package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import java.util.*;

public final class ComboAbilityCatalog {

    private final Map<ComboId, List<AbilityId>> byCombo;

    public ComboAbilityCatalog(Map<ComboId, List<AbilityId>> byCombo) {
        this.byCombo = Map.copyOf(byCombo);
    }

    public Optional<AbilityId> abilityForStep(ComboId comboId, int step) {
        List<AbilityId> list = byCombo.get(comboId);
        if (list == null || step < 0 || step >= list.size()) return Optional.empty();
        return Optional.of(list.get(step));
    }
}
