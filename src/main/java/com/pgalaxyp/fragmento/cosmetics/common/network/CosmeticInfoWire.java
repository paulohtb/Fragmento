package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticInfo;

record CosmeticInfoWire(
        CosmeticInfoWireBase base,
        String displayName
) {
    CosmeticInfo toModel() {
        return new CosmeticInfo(
                base.id(),
                base.slot(),
                base.requiredTier(),
                base.sort(),
                base.visibleToSelf(),
                base.modelKey(),
                displayName
        );
    }

    static CosmeticInfoWire fromModel(CosmeticInfo info) {
        return new CosmeticInfoWire(
                new CosmeticInfoWireBase(
                        info.id(),
                        info.slot(),
                        info.requiredTier(),
                        info.sort(),
                        info.visibleToSelf(),
                        info.modelKey()
                ),
                info.displayName()
        );
    }
}