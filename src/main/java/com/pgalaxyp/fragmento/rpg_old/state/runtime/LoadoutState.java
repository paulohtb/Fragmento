package com.pgalaxyp.fragmento.rpg_old.state.runtime;

import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystId;

public record LoadoutState(
        CatalystId equippedCatalyst,
        CatalystFamilyId family,
        boolean offhandEmpty
) {

    public static LoadoutState empty() {
        return new LoadoutState(null, null, true);
    }

    public boolean valid() {
        return equippedCatalyst != null && family != null && offhandEmpty;
    }
}