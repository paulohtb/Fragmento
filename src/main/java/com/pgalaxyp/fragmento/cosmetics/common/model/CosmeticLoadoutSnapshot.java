package com.pgalaxyp.fragmento.cosmetics.common.model;

import java.util.Objects;

public record CosmeticLoadoutSnapshot(CosmeticLoadout loadout, long version) {

    public CosmeticLoadoutSnapshot {
        Objects.requireNonNull(loadout, "loadout");
    }

    public static CosmeticLoadoutSnapshot of(CosmeticLoadout loadout, long version) {
        return new CosmeticLoadoutSnapshot(loadout, version);
    }
}