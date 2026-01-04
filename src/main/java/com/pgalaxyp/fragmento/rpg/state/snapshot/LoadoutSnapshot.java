package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg.domain.id.CatalystId;

public record LoadoutSnapshot(
        CatalystId equippedCatalyst,
        CatalystFamilyId family,
        boolean offhandEmpty
) {}