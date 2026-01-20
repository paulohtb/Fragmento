package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import java.util.*;

public final class ActionCatalog {

    private final Map<ComboId, List<AbilityId>> abilitiesByCombo;

    public ActionCatalog(Map<ComboId, List<AbilityId>> abilitiesByCombo) {
        this.abilitiesByCombo = Map.copyOf(Objects.requireNonNull(abilitiesByCombo));
    }

    public Optional<AbilityId> abilityForStep(ComboId comboId, int step) {
        List<AbilityId> list = abilitiesByCombo.get(comboId);
        if (list == null || step < 0 || step >= list.size()) return Optional.empty();
        return Optional.ofNullable(list.get(step));
    }
}