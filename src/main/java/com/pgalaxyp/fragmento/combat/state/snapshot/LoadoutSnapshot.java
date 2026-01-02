package com.pgalaxyp.fragmento.combat.state.snapshot;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.combat.domain.id.CatalystId;

public record LoadoutSnapshot(
        CatalystId equippedCatalyst,
        CatalystFamilyId family,
        boolean offhandEmpty
) {}