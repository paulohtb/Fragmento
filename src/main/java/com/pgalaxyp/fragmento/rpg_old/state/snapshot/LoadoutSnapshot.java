package com.pgalaxyp.fragmento.rpg_old.state.snapshot;

import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystId;

public record LoadoutSnapshot(
        CatalystId equippedCatalyst,
        CatalystFamilyId family,
        boolean offhandEmpty
) {}