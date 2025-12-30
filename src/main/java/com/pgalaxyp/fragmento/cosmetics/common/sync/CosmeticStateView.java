package com.pgalaxyp.fragmento.cosmetics.common.sync;

import com.pgalaxyp.fragmento.common.sync.VersionedState;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;

public record CosmeticStateView(
        CosmeticLoadout loadout,
        long version
) implements VersionedState {}