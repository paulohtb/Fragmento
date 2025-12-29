package com.pgalaxyp.fragmento.cosmetics.server.state;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;

public record SlotCosmetic(CosmeticId base) {

    public CosmeticId effective() {
        return base;
    }

    public boolean isEmpty() {
        return base == null;
    }
}