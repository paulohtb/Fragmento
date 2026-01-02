package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.combat.domain.id.CatalystId;

public record LoadoutRuntimeState(
        CatalystId equippedCatalyst,
        CatalystFamilyId family,
        boolean offhandEmpty
) {

    public static LoadoutRuntimeState empty() {
        return new LoadoutRuntimeState(null, null, true);
    }

    public boolean valid() {
        return equippedCatalyst != null && family != null && offhandEmpty;
    }
}